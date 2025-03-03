import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.Mapper;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.abstractsymbols.OutputBuilder;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.abstractsymbols.OutputChecker;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.config.MapperConfig;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.context.ExecutionContext;

public class TCPMapper implements Mapper<TCPInput, TCPOutput, ExecutionContext<TCPInput, TCPOutput, String>> {

    @Override
    public TCPOutput execute(TCPInput input, ExecutionContext<TCPInput, TCPOutput, String> context) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

    @Override
    public MapperConfig getMapperConfig() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMapperConfig'");
    }

    @Override
    public OutputBuilder<TCPOutput> getOutputBuilder() {
        return new TCPOutputBuilder();
    }

    @Override
    public OutputChecker<TCPOutput> getOutputChecker() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOutputChecker'");
    }

}
