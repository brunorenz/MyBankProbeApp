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
                        dc.getCategories().addAll(dt.getCategories());
                        sendBroadCast(dc);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Errore chiamata servizio ExcludedNotification", e);
        }
    }
}
