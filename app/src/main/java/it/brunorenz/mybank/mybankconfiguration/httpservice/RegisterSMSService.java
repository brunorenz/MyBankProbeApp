package it.brunorenz.mybank.mybankconfiguration.httpservice;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import it.brunorenz.mybank.mybankconfiguration.bean.BaseRequest;
import it.brunorenz.mybank.mybankconfiguration.bean.Error;
import it.brunorenz.mybank.mybankconfiguration.bean.GenericDataContainer;
import it.brunorenz.mybank.mybankconfiguration.bean.RegisterSMSRequest;
import it.brunorenz.mybank.mybankconfiguration.bean.RegisterSMSResponse;
import it.brunorenz.mybank.mybankconfiguration.bean.RegistrationInfo;
import it.brunorenz.mybank.mybankconfiguration.network.BaseHttpCallback;
import it.brunorenz.mybank.mybankconfiguration.network.IDataContainer;
import it.brunorenz.mybank.mybankconfiguration.utility.MessageStatisticManager;
import it.brunorenz.mybank.mybankconfiguration.utility.RESTUtil;
import okhttp3.Call;
import okhttp3.Response;

public class RegisterSMSService extends BaseHttpCallback {
    public final static String TAG = RegisterSMSService.class.getName();
    private String type;

    public RegisterSMSService(Context context, String intent, IDataContainer dataContainer, boolean sendNotify) {
        super(context, intent, dataContainer, sendNotify);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    protected void onHttpFailure(Call call, IOException e) {
        IDataContainer dc = getDataContainer();
        if (dc != null && dc instanceof GenericDataContainer)
        {
            BaseRequest req  = ((GenericDataContainer) dc).getBaseRequest();
            if (req != null && req instanceof RegisterSMSRequest)
            {
                try {
                    String jsonRequest =  RESTUtil.jsonSerialize( (RegisterSMSRequest) req);
                    Log.d(TAG, "Request da salvare : "+jsonRequest);
                } catch (Exception ex)
                {
                    Log.e(TAG, "Errore serializzazione Request", ex);
                }

            }
        }

        super.onHttpFailure(call, e);
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
                boolean accepted = false;
                if (er.getCode() == 0 && ri != null && ri.isAccepted())
                {
                    message = "Messaggio emesso da "+ri.getBankId()+" di importo "+ri.getImporto()+" euro accettato da MyBank!";
                    accepted = true;
                }
                MessageStatisticManager stat = new MessageStatisticManager();
                stat.processMessage(getContext(), getType(),true,accepted);

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
