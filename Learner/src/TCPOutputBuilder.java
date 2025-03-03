import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.abstractsymbols.OutputBuilder;

public class TCPOutputBuilder implements OutputBuilder<TCPOutput> {

    @Override
    public TCPOutput buildOutput(String name) {
        return new TCPOutput(name);
    }

}