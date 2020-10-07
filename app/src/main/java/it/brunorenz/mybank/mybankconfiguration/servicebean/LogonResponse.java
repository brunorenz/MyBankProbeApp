package it.brunorenz.mybank.mybankconfiguration.servicebean;

public class LogonResponse extends BaseResponse {
    private String uniqueId;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
