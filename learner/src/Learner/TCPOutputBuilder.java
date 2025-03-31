package Learner;

import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.abstractsymbols.OutputBuilder;

public class TCPOutputBuilder extends OutputBuilder<TCPOutput> {

    @Override
    public TCPOutput buildOutput(String name) {
        return new TCPOutput(name);
    }

    @Override
    public TCPOutput buildOutputExact(String name) {
        return new TCPOutput(name);
    }
}
