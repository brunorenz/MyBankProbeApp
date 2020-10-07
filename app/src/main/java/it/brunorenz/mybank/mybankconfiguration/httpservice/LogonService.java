package it.brunorenz.mybank.mybankconfiguration.httpservice;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import it.brunorenz.mybank.mybankconfiguration.servicebean.Error;
import it.brunorenz.mybank.mybankconfiguration.bean.GenericDataContainer;
import it.brunorenz.mybank.mybankconfiguration.servicebean.LogonResponse;
import it.brunorenz.mybank.mybankconfiguration.network.BaseHttpCallback;
import it.brunorenz.mybank.mybankconfiguration.network.IDataContainer;
import it.brunorenz.mybank.mybankconfiguration.utility.RESTUtil;
import okhttp3.Call;
import okhttp3.Response;

public class LogonService extends BaseHttpCallback {
    public final static String TAG = LogonService.class.getName();

    public LogonService(Context context, String intent, IDataContainer dataContainer, boolean sendNotify) {
        super(context, intent, dataContainer, sendNotify);
    }

    @Override
    protected void onHttpResponse(String jsonResponse) throws IOException {
        boolean ok = false;
        try {
            LogonResponse resp = RESTUtil.jsonDeserialize(jsonResponse, LogonResponse.class);
            Error er = resp.getError();

            if (er.getCode() == 0) {
                Log.i(TAG, "Logon ok con uid "+resp.getUniqueId());
                ok = true;
            }
        } catch (Exception e) {
            Log.e(TAG, "Errore chiamata servizio Logon", e);
        }
        if (getDataContainer() != null && getDataContainer() instanceof GenericDataContainer) {
            GenericDataContainer dc = (GenericDataContainer) getDataContainer();
            dc.setLogonOk(ok);
            sendBroadCast(dc);
        }
    }


    protected void onHttpResponseXX(Call call, Response response) throws IOException {
        Error er = new Error();
        er.setCode(response.code());
        String message = null;
        boolean ok = false;
        if (response.code() == 200) {
            String myResponse = response.body().string();
            Log.d(TAG, myResponse);
            try {
                LogonResponse resp = RESTUtil.jsonDeserialize(myResponse, LogonResponse.class);
                er = resp.getError();

                if (er.getCode() == 0) {
                    Log.i(TAG, "Logon ok con uid "+resp.getUniqueId());
                    ok = true;
                }
            } catch (Exception e) {
                Log.e(TAG, "Errore chiamata servizio Logon", e);
            }
        } else {
            Log.e(TAG, "Errore chiamata servizio Logon : HTTP Code " + response.code() + " - " + response.message());
        }
        if (getDataContainer() != null && getDataContainer() instanceof GenericDataContainer) {
            GenericDataContainer dc = (GenericDataContainer) getDataContainer();
            dc.setLogonOk(ok);
            sendBroadCast(dc);
        }

    }
}