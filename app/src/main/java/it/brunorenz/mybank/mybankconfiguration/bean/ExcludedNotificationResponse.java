package it.brunorenz.mybank.mybankconfiguration.bean;

import java.util.ArrayList;
import java.util.List;

public class ExcludedNotificationResponse extends BaseResponse {
    private List<String> data;

    public List<String> getData() {
        if (data == null) data = new ArrayList<>();
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
