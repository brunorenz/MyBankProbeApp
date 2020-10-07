package it.brunorenz.mybank.mybankconfiguration.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyBankConfigurationData implements Serializable {
    private List<BankAccount> accounts;

    public List<BankAccount> getAccounts() {
        if (accounts == null) accounts = new ArrayList<>();
        return accounts;
    }

    public void setAccounts(List<BankAccount> accounts) {
        this.accounts = accounts;
    }
}
