package it.brunorenz.mybank.mybankconfiguration.bean;

public class RegistrationInfo {
    private boolean accepted;
    private String bankId;
    private boolean bancomat;
    private boolean pos;
    private boolean cartacredito;
    private String importo;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public boolean isBancomat() {
        return bancomat;
    }

    public void setBancomat(boolean bancomat) {
        this.bancomat = bancomat;
    }

    public boolean isPos() {
        return pos;
    }

    public void setPos(boolean pos) {
        this.pos = pos;
    }

    public boolean isCartacredito() {
        return cartacredito;
    }

    public void setCartacredito(boolean cartacredito) {
        this.cartacredito = cartacredito;
    }

    public String getImporto() {
        return importo;
    }

    public void setImporto(String importo) {
        this.importo = importo;
    }
}

