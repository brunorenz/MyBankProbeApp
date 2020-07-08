package it.brunorenz.mybank.mybankconfiguration.service;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import it.brunorenz.mybank.mybankconfiguration.bean.Error;
import it.brunorenz.mybank.mybankconfiguration.bean.GetMessageFilterResponse;
import it.brunorenz.mybank.mybankconfiguration.bean.GenericDataContainer;
import it.brunorenz.mybank.mybankconfiguration.bean.MessageFilterData;
import it.brunorenz.mybank.mybankconfiguration.network.BaseHttpCallback;
import it.brunorenz.mybank.mybankconfiguration.network.IDataContainer;
import it.brunorenz.mybank.mybankconfiguration.utility.RESTUtil;
import okhttp3.Call;
import okhttp3.Response;

public class GetMessageFilterService extends BaseHttpCallback {
    public final static String TAG = GetMessageFilterService.class.getName();

    public GetMessageFilterService(Context context, String intent, IDataContainer dataContainer, boolean sendNotify) {
        super(context, intent, dataContainer, sendNotify);
    }

    @Override
    protected void onHttpResponse(Call call, Response response) throws IOException {
        Error er = new Error();
        er.setCode(response.code());
        String message = null;
        if (response.code() == 200) {
            String myResponse = response.body().string();
            Log.d(TAG, myResponse);
            try {
                GetMessageFilterResponse resp = RESTUtil.jsonDeserialize(myResponse, GetMessageFilterResponse.class);
                er = resp.getError();

                if (er.getCode() == 0) {
                    List<MessageFilterData> ri = resp.getData();
                    Log.i(TAG, "Letti " + ri.size() + " package per esclusione notifiche");
                    // scrivi in memoria interna ?
                    if (getDataContainer() != null && getDataContainer() instanceof GenericDataContainer) {
                        GenericDataContainer dc = (GenericDataContainer) getDataContainer();
                        dc.getMessageFilter().addAll(ri);
                        sendBroadCast(dc);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Errore chiamata servizio ExcludedNotification", e);
            }
        } else {
            Log.e(TAG, "Errore chiamata servizio ExcludedNotification : HTTP Code " + response.code() + " - " + response.message());
        }
    }
}