package Learner;

import Learner.TCPExecutionContext;
import Learner.TCPState;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.core.AbstractSul;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.core.SulAdapter;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.core.config.SulConfig;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.core.sulwrappers.DynamicPortProvider;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.core.sulwrappers.ProcessHandler;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.Mapper;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.context.ExecutionContext;
import com.github.protocolfuzzing.protocolstatefuzzer.utils.CleanupTasks;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ThreadLocalRandom;

public class TCPMapperSul
    implements
        AbstractSul<
            TCPInput,
            TCPOutput,
            ExecutionContext<TCPInput, TCPOutput, String>
        > {

    private SocketMapperSul socketSul;

    /** Stores the constructor parameter. */
    protected SulConfig sulConfig;

    /** Stores the constructor parameter. */
    protected CleanupTasks cleanupTasks;

    /** Stores the provided dynamic port provider. */
    protected DynamicPortProvider dynamicPortProvider;

    /** Stores the current execution */
    protected TCPExecutionContext context;

    /** Stores the starting sequence number for the next sequence **/
    protected long startSeq = ThreadLocalRandom.current().nextLong(10000, 30000);

    /** Stores the Mapper instance. */
    protected TCPMapper mapper;

    /** Stores the SulAdapter instance. */
    protected SulAdapter sulAdapter;

    public <T extends SulConfig & TCPMapperConfigProvider> TCPMapperSul(
        T sulConfig,
        CleanupTasks cleanupTasks
    ) throws UnknownHostException, IOException {
        // copied from the commit before the introduction of generics
        // -------------------------------------------------------------------
        this.sulConfig = sulConfig;
        this.cleanupTasks = cleanupTasks;
        // mapper and sulAdapter will be provided in subclasses
        this.mapper = new TCPMapper();
        this.sulAdapter = new TCPSulAdapter();
        // -------------------------------------------------------------------

        String mapperAddress = sulConfig
            .getTCPMapperConfig()
            .getMapperAddress();
        String[] addressSplit = mapperAddress.split("\\:");
        if (addressSplit.length != 2) {
            throw new MapperException(
                "Invalid mapper host, expected hostAddress:hostPort"
            );
        }
        String mapperIpAddress = addressSplit[0];
        Integer mapperPort = Integer.valueOf(addressSplit[1]);
        if (sulConfig.getTCPMapperConfig().getMapperCommand() != null) {
            MapperProcessHandler handler = new MapperProcessHandler(
                sulConfig.getTCPMapperConfig().getMapperCommand(),
                sulConfig.getTCPMapperConfig().getMapperStartWait()
            );
            handler.launchProcess();
        }
        Socket sock = new Socket(mapperIpAddress, mapperPort);
        cleanupTasks.submit(
            new Runnable() {
                @Override
                public void run() {
                    if (socketSul != null) {
                        socketSul.reset();
                    }
                    try {
                        sock.close();
                    } catch (IOException e) {
                        throw new MapperException(e);
                    }
                }
            }
        );

        socketSul = new SocketMapperSul(sock);
    }

    // Before each query we create a blank context
    @Override
    public void pre() {
        socketSul.reset();
        if(this.startSeq > Long.MAX_VALUE - 100000) {
            this.startSeq = ThreadLocalRandom.current().nextLong(10000, 30000);
        }
        this.startSeq = this.startSeq + ThreadLocalRandom.current().nextInt(10000, 30000);
        this.context = new TCPExecutionContext(new TCPState(this.startSeq, 0));
    }

    @Override
    public void post() {}

    private static class MapperProcessHandler extends ProcessHandler {

        protected MapperProcessHandler(String command, long startWait) {
            super(command, startWait);
        }
    }

    @Override
    public TCPOutput step(TCPInput in) {
        // If we send an S or an F we technically send one bit of data
        if(in.getName().contains("S") || in.getName().contains("F")) {
            this.context.getState().setAck(this.context.getState().getAck() + 1);
        }

        this.mapper.updateInput(in, this.context);
        String output;
        // Reset does not need seq and ack numbers
        if (in.getName() == "reset") {
            output = socketSul.sendAndRecv(in.getName());
        } else {
            socketSul.send(in.getName());
            socketSul.send(String.valueOf(in.getSeq()));
            socketSul.send(String.valueOf(in.getAck()));
            output = socketSul.sendAndRecv("");
        }

        if (output.equals("timeout")) {
            return new TCPOutput("timeout");
        } else {
            // Split the returned packet (it has format "seq,ack,flags")
            String[] split = output.split(",");

            if (split[2].contains("R")) {
                // If reset is recevied we reset sequence and acknowledgement numbers                
                this.startSeq = this.startSeq + ThreadLocalRandom.current().nextInt(10000, 30000);
                this.context.getState().setSeq(this.startSeq);
                this.context.getState().setAck(this.startSeq + 1);
            } else if (split[2].contains("A")) {
                // Update seq and ack if ack number is valid
                this.context.getState().setSeq(Long.parseLong(split[1]));
                this.context.getState().setAck(Long.parseLong(split[0]) + 1);
            } else {
                // otherwise we just update our ACK (since input ack is not valid)
                this.context.getState().setAck(Long.parseLong(split[0]) + 1);
            }

            return new TCPOutput(
                split[2],
                Long.parseLong(split[0]),
                Long.parseLong(split[1])
            );
        }
    }

    @Override
    public SulConfig getSulConfig() {
        return sulConfig;
    }

    @Override
    public CleanupTasks getCleanupTasks() {
        return cleanupTasks;
    }

    @Override
    public void setDynamicPortProvider(
        DynamicPortProvider dynamicPortProvider
    ) {
        this.dynamicPortProvider = dynamicPortProvider;
    }

    @Override
    public DynamicPortProvider getDynamicPortProvider() {
        return dynamicPortProvider;
    }

    @Override
    public Mapper<
        TCPInput,
        TCPOutput,
        ExecutionContext<TCPInput, TCPOutput, String>
    > getMapper() {
        return (Mapper) mapper;
    }

    @Override
    public SulAdapter getSulAdapter() {
        return sulAdapter;
    }
}
