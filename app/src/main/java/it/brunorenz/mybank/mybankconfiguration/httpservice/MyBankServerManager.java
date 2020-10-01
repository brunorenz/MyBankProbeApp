package it.brunorenz.mybank.mybankconfiguration.httpservice;

import android.content.Context;
import android.util.Log;

import it.brunorenz.mybank.mybankconfiguration.R;
import it.brunorenz.mybank.mybankconfiguration.bean.GenericDataContainer;
import it.brunorenz.mybank.mybankconfiguration.bean.RegisterSMSRequest;
import it.brunorenz.mybank.mybankconfiguration.network.HttpManager;
import it.brunorenz.mybank.mybankconfiguration.network.IDataContainer;
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
            GenericDataContainer dataContainer = new GenericDataContainer();
            dataContainer.setBaseRequest(request);
            RegisterSMSService service = new RegisterSMSService(getContext(), intent, dataContainer,true);
            service.setType(request.getType());
            callHttpPost(url, RESTUtil.jsonSerialize(request), service);
        } catch (Exception e)
        {
            Log.d(TAG,"Errore chiamata servizio "+url,e);
        }

    }

    public void getMessageFilter(String intent, String type) {
        String url = createUrl(getContext().getString(R.string.GETEXLNOTY));
        if (type != null) url = url + "?type="+type;
        try {
            //if (dataContainer == null) dataContainer = new GenericDataContainer();
            callHttpGet(url,  new GetMessageFilterService(getContext(), intent, new GenericDataContainer(),false));
        } catch (Exception e)
        {
            Log.d(TAG,"Errore chiamata servizio "+url,e);
        }

    }

}
