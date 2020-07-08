package it.brunorenz.mybank.mybankconfiguration.bean;

import java.util.ArrayList;
import java.util.List;

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
