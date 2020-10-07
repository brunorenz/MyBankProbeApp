package it.brunorenz.mybank.mybankconfiguration.bean;

import java.util.ArrayList;
import java.util.List;

import it.brunorenz.mybank.mybankconfiguration.bean.MessageFilterData;
import it.brunorenz.mybank.mybankconfiguration.network.IDataContainer;
import it.brunorenz.mybank.mybankconfiguration.servicebean.BaseRequest;

public class GenericDataContainer implements IDataContainer {
    private List<MessageFilterData> excludedPackage;
    private boolean logonOk;
    private BaseRequest baseRequest;
    private List<BankAccount> accounts;

    public List<BankAccount> getAccounts() {
        if (accounts == null) accounts = new ArrayList<>();
        return accounts;
    }

    public List<MessageFilterData> getMessageFilter() {
        if (excludedPackage == null) excludedPackage = new ArrayList<>();
        return excludedPackage;
    }

    public void setBaseRequest(BaseRequest baseRequest) {
        this.baseRequest = baseRequest;
    }

    public BaseRequest getBaseRequest() {
        return baseRequest;
    }

    public boolean isLogonOk() {
        return logonOk;
    }

    public void setLogonOk(boolean logonOk) {
        this.logonOk = logonOk;
    }

}
