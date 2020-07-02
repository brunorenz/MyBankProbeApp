package it.brunorenz.mybank.mybankconfiguration.service;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import it.brunorenz.mybank.mybankconfiguration.bean.Error;
import it.brunorenz.mybank.mybankconfiguration.bean.ExcludedNotificationResponse;
import it.brunorenz.mybank.mybankconfiguration.bean.GenericDataContainer;
import it.brunorenz.mybank.mybankconfiguration.network.BaseHttpCallback;
import it.brunorenz.mybank.mybankconfiguration.network.IDataContainer;
import it.brunorenz.mybank.mybankconfiguration.utility.RESTUtil;
import okhttp3.Call;
import okhttp3.Response;

public class ExcludedNotificationService extends BaseHttpCallback {
    public final static String TAG = ExcludedNotificationService.class.getName();

    public ExcludedNotificationService(Context context, String intent, IDataContainer dataContainer, boolean sendNotify) {
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
                ExcludedNotificationResponse resp = RESTUtil.jsonDeserialize(myResponse, ExcludedNotificationResponse.class);
                er = resp.getError();

                if (er.getCode() == 0) {
                    List<String> ri = resp.getData();
                    Log.i(TAG, "Letti " + ri.size() + " package per esclusione notifiche");
                    // scrivi in memoria interna ?
                    if (getDataContainer() != null && getDataContainer() instanceof GenericDataContainer) {
                        GenericDataContainer dc = (GenericDataContainer) getDataContainer();
                        dc.getExcludedPackage().addAll(ri);
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