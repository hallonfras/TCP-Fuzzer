package Learner;

import Learner.TCPExecutionContext;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.Mapper;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.abstractsymbols.OutputBuilder;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.abstractsymbols.OutputChecker;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.config.MapperConfig;
import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.context.ExecutionContext;

public class TCPMapper
    implements Mapper<TCPInput, TCPOutput, TCPExecutionContext> {

    @Override
    public TCPOutput execute(TCPInput input, TCPExecutionContext context) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "Unimplemented method 'execute'"
        );
    }

    /**
     * Takes a TCPInput and updates it's state according to the execution context
     * @param input the input to update
     * @param context the current execution context
     */
    public void updateInput(TCPInput input, TCPExecutionContext context) {
        input.setSeq(context.getState().getSeq());
        input.setAck(context.getState().getAck());
    }

    @Override
    public MapperConfig getMapperConfig() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "Unimplemented method 'getMapperConfig'"
        );
    }

    @Override
    public OutputBuilder<TCPOutput> getOutputBuilder() {
        return new TCPOutputBuilder();
    }

    @Override
    public OutputChecker<TCPOutput> getOutputChecker() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "Unimplemented method 'getOutputChecker'"
        );
    }
}
