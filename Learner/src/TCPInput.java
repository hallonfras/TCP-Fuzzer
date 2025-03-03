import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.abstractsymbols.AbstractInputXml;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.abstractsymbols.OutputChecker;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.context.ExecutionContext;

public class TCPInput extends AbstractInputXml<TCPOutput, String, ExecutionContext<TCPInput, TCPOutput, String>> {

    public TCPInput(String name) {
        super(name);
    }

    @Override
    public void preSendUpdate(ExecutionContext<TCPInput, TCPOutput, String> context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String generateProtocolMessage(ExecutionContext<TCPInput, TCPOutput, String> context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void postSendUpdate(ExecutionContext<TCPInput, TCPOutput, String> context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void postReceiveUpdate(TCPOutput output, OutputChecker<TCPOutput> outputChecker,
            ExecutionContext<TCPInput, TCPOutput, String> context) {
        throw new UnsupportedOperationException();
    }
}
