package it.brunorenz.mybank.mybankconfiguration.service;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import it.brunorenz.mybank.mybankconfiguration.bean.Error;
import it.brunorenz.mybank.mybankconfiguration.bean.RegisterSMSResponse;
import it.brunorenz.mybank.mybankconfiguration.bean.RegistrationInfo;
import it.brunorenz.mybank.mybankconfiguration.network.BaseHttpCallback;
import it.brunorenz.mybank.mybankconfiguration.utility.RESTUtil;
import okhttp3.Call;
import okhttp3.Response;

public class RegisterSMSService extends BaseHttpCallback {
    public final static String TAG = RegisterSMSService.class.getName();

    public RegisterSMSService(Context context, String intent, boolean sendNotify) {
        super(context, intent, sendNotify);
    }

    @Override
    protected void onHttpResponse(Call call, Response response) throws IOException {
        Error er = new Error();
        er.setCode(response.code());
        String message =  null;
        if (response.code() == 200) {
            String myResponse = response.body().string();
            Log.d(TAG, myResponse);
            try {
                RegisterSMSResponse resp = RESTUtil.jsonDeserialize(myResponse, RegisterSMSResponse.class);
                er = resp.getError();
                RegistrationInfo ri = resp.getData();
                if (er.getCode() == 0 && ri != null && ri.isAccepted())
                {
                    message = "Messaggio emesso da "+ri.getIssuer()+" accettato da MyBank!";
                }
            } catch (Exception e) {
                Log.e(TAG, "Errore chiamata servizio RegisterSMS", e);
            }
        } else {
            Log.e(TAG, "Errore chiamata servizio RegisterSMS : HTTP Code " + response.code() + " - " + response.message());
        }
        if (isSendNotification() && message != null)
            sendNotification(er,message);

    }
}
