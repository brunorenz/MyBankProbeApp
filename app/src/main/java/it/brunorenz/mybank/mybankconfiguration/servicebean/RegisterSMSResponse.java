package it.brunorenz.mybank.mybankconfiguration.servicebean;

import it.brunorenz.mybank.mybankconfiguration.bean.RegistrationInfo;

public class RegisterSMSResponse extends BaseResponse {
    private RegistrationInfo data;

    public RegistrationInfo getData() {
        return data;
    }

    public void setData(RegistrationInfo data) {
        this.data = data;
    }
}
