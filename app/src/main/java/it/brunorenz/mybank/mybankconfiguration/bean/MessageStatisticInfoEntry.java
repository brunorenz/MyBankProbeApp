package it.brunorenz.mybank.mybankconfiguration.bean;

public class MessageStatisticInfoEntry {
    int tot;
    int accepted;
    int sent;

    public MessageStatisticInfoEntry() {
        tot = 0;
        accepted = 0;
        sent = 0;
    }

    public int getTot() {
        return tot;
    }

    private void setTot(int tot) {
        this.tot = tot;
    }

    public int getAccepted() {
        return accepted;
    }

    private void setAccepted(int accepted) {
        this.accepted = accepted;
    }

    public int getSent() {
        return sent;
    }

    private void setSent(int sent) {
        this.sent = sent;
    }

    public void update(boolean sent, boolean accepted) {
        tot++;
        if (sent) this.sent++;
        if (accepted) this.accepted++;
    }
}
