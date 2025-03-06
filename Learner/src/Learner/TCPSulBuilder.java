package Learner;

import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.core.AbstractSul;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.core.SulBuilder;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.core.config.SulConfig;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.context.ExecutionContext;
import com.github.protocolfuzzing.protocolstatefuzzer.utils.CleanupTasks;
import java.io.IOException;

public class TCPSulBuilder
    implements
        SulBuilder<
            TCPInput,
            TCPOutput,
            ExecutionContext<TCPInput, TCPOutput, String>
        > {

    @Override
    public AbstractSul<
        TCPInput,
        TCPOutput,
        ExecutionContext<TCPInput, TCPOutput, String>
    > build(SulConfig sulConfig, CleanupTasks cleanupTasks) {
        try {
            AbstractSul<
                TCPInput,
                TCPOutput,
                ExecutionContext<TCPInput, TCPOutput, String>
            > tcpSulConfig = null;
            if (sulConfig.isFuzzingClient()) {
                tcpSulConfig = new TCPMapperSul(
                    (TCPSulClientConfig) sulConfig,
                    cleanupTasks
                );
                return tcpSulConfig;
            } else {
                tcpSulConfig = new TCPMapperSul(
                    (TCPSulServerConfig) sulConfig,
                    cleanupTasks
                );
                return tcpSulConfig;
            }
        } catch (IOException e) {
            throw new MapperException("Error creating TCPMapperSul", e);
        }
    }
}
