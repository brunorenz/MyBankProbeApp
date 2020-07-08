package it.brunorenz.mybank.mybankconfiguration.bean;

import java.io.Serializable;

public class MessageFilterData implements Serializable {
    private String type;
    private String sender;
    private String packageName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
