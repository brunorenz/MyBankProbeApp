package it.brunorenz.mybank.mybankconfiguration.servicebean;

import java.util.ArrayList;
import java.util.List;

import it.brunorenz.mybank.mybankconfiguration.bean.MessageFilterData;

public class GetMessageFilterResponse extends BaseResponse {
    private List<MessageFilterData> data;

    public List<MessageFilterData> getData() {
        if (data == null) data = new ArrayList<>();
        return data;
    }

    public void setData(List<MessageFilterData> data) {
        this.data = data;
    }
}
