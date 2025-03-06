package Learner;

import com.beust.jcommander.Parameter;

public class TCPMapperConfig {

    @Parameter(
        names = "-tcpMapperAddress",
        required = true,
        description = "The address of the TCP mapper. Format: ip:port"
    )
    private String mapperAddress;

    @Parameter(
        names = "-tcpMapperCommand",
        required = false,
        description = "The command used to launch the TCP mapper at the start of a learning experiment"
    )
    private String mapperCommand;

    @Parameter(
        names = "-tcpMapperStartWait",
        required = false,
        description = "The time (ms) to wait for the TCP mapper process to start"
    )
    private Long mapperStartWait = 1000L;

    public String getMapperAddress() {
        return mapperAddress;
    }

    public String getMapperCommand() {
        return mapperCommand;
    }

    public Long getMapperStartWait() {
        return mapperStartWait;
    }
}
