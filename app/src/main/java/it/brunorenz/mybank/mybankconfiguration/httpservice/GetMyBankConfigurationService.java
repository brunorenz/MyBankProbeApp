package it.brunorenz.mybank.mybankconfiguration.httpservice;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import it.brunorenz.mybank.mybankconfiguration.bean.GenericDataContainer;
import it.brunorenz.mybank.mybankconfiguration.bean.MyBankConfigurationData;
import it.brunorenz.mybank.mybankconfiguration.network.BaseHttpCallback;
import it.brunorenz.mybank.mybankconfiguration.network.IDataContainer;
import it.brunorenz.mybank.mybankconfiguration.servicebean.Error;
import it.brunorenz.mybank.mybankconfiguration.servicebean.GetMyBankConfigurationResponse;
import it.brunorenz.mybank.mybankconfiguration.utility.RESTUtil;
import okhttp3.Call;
import okhttp3.Response;

public class GetMyBankConfigurationService extends BaseHttpCallback {
    public final static String TAG = GetMyBankConfigurationService.class.getName();

    public GetMyBankConfigurationService(Context context, String intent, IDataContainer dataContainer, boolean sendNotify) {
        super(context, intent, dataContainer, sendNotify);
    }

    @Override
    protected void onHttpResponse(String jsonResponse) throws IOException {
        try {
            GetMyBankConfigurationResponse resp = RESTUtil.jsonDeserialize(jsonResponse, GetMyBankConfigurationResponse.class);
            Error er = resp.getError();
            if (er.getCode() == 0) {
                MyBankConfigurationData dt = resp.getData();
                if (dt != null) {
                    Log.i(TAG, "Letti " + dt.getAccounts().size() + " record accounts");
                    // scrivi in memoria interna ?
                    if (getDataContainer() != null && getDataContainer() instanceof GenericDataContainer) {
                        GenericDataContainer dc = (GenericDataContainer) getDataContainer();
                        dc.getAccounts().addAll(dt.getAccounts());
                        sendBroadCast(dc);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Errore chiamata servizio ExcludedNotification", e);
        }
    }

    protected void onHttpResponseXX(Call call, Response response) throws IOException {
        Error er = new Error();
        er.setCode(response.code());
        if (response.code() == 200) {
            String myResponse = response.body().string();
            Log.d(TAG, myResponse);
            try {
                GetMyBankConfigurationResponse resp = RESTUtil.jsonDeserialize(myResponse, GetMyBankConfigurationResponse.class);
                er = resp.getError();
                if (er.getCode() == 0) {
                    MyBankConfigurationData dt = resp.getData();
                    if (dt != null) {
                        Log.i(TAG, "Letti " + dt.getAccounts().size() + " record accounts");
                        // scrivi in memoria interna ?
                        if (getDataContainer() != null && getDataContainer() instanceof GenericDataContainer) {
                            GenericDataContainer dc = (GenericDataContainer) getDataContainer();
                            dc.getAccounts().addAll(dt.getAccounts());
                            sendBroadCast(dc);
                        }
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
