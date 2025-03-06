package Learner;

import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.context.ExecutionContext;
import java.util.ArrayList;

public class TCPExecutionContext
    implements ExecutionContext<TCPInput, TCPOutput, TCPState> {

    private TCPState state;
    private ArrayList<TCPInput> inputs = new ArrayList<TCPInput>();
    private ArrayList<TCPOutput> outputs = new ArrayList<TCPOutput>();

    public TCPExecutionContext(TCPState state) {
        this.state = state;
    }

    public TCPState getState() {
        return this.state;
    }

    public void setInput(TCPInput input) {
        this.inputs.add(input);
    }

    public void setOutput(TCPOutput output) {
        this.outputs.add(output);
    }

    public void disableExecution() {}

    public void enableExecution() {}

    public boolean isExecutionEnabled() {
        return true;
    }
}
