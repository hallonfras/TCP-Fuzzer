package Learner;

public class TCPState {

    private long seq, ack;

    public TCPState(long seq, long ack) {
        this.seq = seq;
        this.ack = ack;
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
}
