package Learner;

import com.beust.jcommander.ParametersDelegate;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.core.config.SulServerConfigStandard;

public class TCPSulServerConfig
    extends SulServerConfigStandard
    implements TCPMapperConfigProvider {

    @ParametersDelegate
    private TCPMapperConfig sshMapperConfig;

    public TCPSulServerConfig() {
        sshMapperConfig = new TCPMapperConfig();
    }

    @Override
    public TCPMapperConfig getTCPMapperConfig() {
        return sshMapperConfig;
    }
}
