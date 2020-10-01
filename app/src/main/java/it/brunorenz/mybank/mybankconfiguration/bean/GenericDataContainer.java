package it.brunorenz.mybank.mybankconfiguration.bean;

import java.util.ArrayList;
import java.util.List;

import it.brunorenz.mybank.mybankconfiguration.network.IDataContainer;

public class GenericDataContainer implements IDataContainer {
    private List<MessageFilterData> excludedPackage;

    private BaseRequest baseRequest;

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
}
