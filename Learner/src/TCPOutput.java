import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.abstractsymbols.AbstractOutput;

public class TCPOutput extends AbstractOutput<TCPOutput, String> {

    public TCPOutput(String name) {
        super(name);
    }

    @Override
    protected TCPOutput buildOutput(String name) {
        return new TCPOutput(name);
    }

    @Override
    protected TCPOutput convertOutput() {
        return new TCPOutput(this.name);
    }
}
