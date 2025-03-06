package Learner;

import com.github.protocolfuzzing.protocolstatefuzzer.components.sul.mapper.abstractsymbols.AbstractOutput;

public class TCPOutput extends AbstractOutput<TCPOutput, String> {

    private long seq;
    private long ack;

    public TCPOutput(String name, long seq, long ack) {
        super(name);
        this.seq = seq;
        this.ack = ack;
    }

    public TCPOutput(String name) {
        super(name);
        this.seq = 0;
        this.ack = 0;
    }

    public long getSeq() {
        return this.seq;
    }

    public long getAck() {
        return this.ack;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public void setAck(long ack) {
        this.ack = ack;
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
