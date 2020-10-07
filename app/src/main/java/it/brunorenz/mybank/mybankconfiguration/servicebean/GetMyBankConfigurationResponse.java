package it.brunorenz.mybank.mybankconfiguration.servicebean;

import java.util.ArrayList;
import java.util.List;

import it.brunorenz.mybank.mybankconfiguration.bean.BankAccount;
import it.brunorenz.mybank.mybankconfiguration.bean.MyBankConfigurationData;

public class GetMyBankConfigurationResponse extends BaseResponse {
    private MyBankConfigurationData data;

    public MyBankConfigurationData getData() {
        return data;
    }

    public void setData(MyBankConfigurationData data) {
        this.data = data;
    }
}
