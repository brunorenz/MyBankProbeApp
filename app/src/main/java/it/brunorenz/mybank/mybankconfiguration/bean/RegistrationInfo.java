package it.brunorenz.mybank.mybankconfiguration.bean;

public class RegistrationInfo {
    private boolean accepted;
    private String issuer;

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
