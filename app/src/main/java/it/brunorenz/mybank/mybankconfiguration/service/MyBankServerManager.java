package it.brunorenz.mybank.mybankconfiguration.service;

import android.content.Context;
import android.util.Log;

import it.brunorenz.mybank.mybankconfiguration.R;
import it.brunorenz.mybank.mybankconfiguration.bean.RegisterSMSRequest;
import it.brunorenz.mybank.mybankconfiguration.network.HttpManager;
import it.brunorenz.mybank.mybankconfiguration.utility.RESTUtil;

public class MyBankServerManager extends HttpManager {
    private String serverUrl;
    private static final String TAG =
            MyBankServerManager.class.getSimpleName();

    private static MyBankServerManager serverManager;

    private MyBankServerManager() {
    }
    public static MyBankServerManager createMyBankServerManager()
    {
        return createMyBankServerManager(null);
    }
    public static MyBankServerManager createMyBankServerManager(Context context)
    {
        if (serverManager == null) {
            serverManager = new MyBankServerManager();
            if (context != null)
                serverManager.setContext(context);
        }
        return serverManager;
    }
    private String createUrl(String function) {
        if (serverUrl == null) serverUrl = getContext().getString(R.string.MYBANKSEVER);
        return serverUrl + function;
    }

    public void registerSMS(RegisterSMSRequest request, String intent, boolean notify) {
        String url = createUrl(getContext().getString(R.string.REGISTERSMS));
        try {
            callHttpPost(url, RESTUtil.jsonSerialize(request), new RegisterSMSService(getContext(), intent,true));
        } catch (Exception e)
        {
            Log.d(TAG,"Errore chiamata servizio "+url,e);
        }

    }
}
