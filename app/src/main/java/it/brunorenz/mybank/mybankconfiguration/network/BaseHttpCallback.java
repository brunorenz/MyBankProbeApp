package it.brunorenz.mybank.mybankconfiguration.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import it.brunorenz.mybank.mybankconfiguration.MainActivity;
import it.brunorenz.mybank.mybankconfiguration.R;
import it.brunorenz.mybank.mybankconfiguration.servicebean.Error;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//import android.support.v4.content.LocalBroadcastManager;

public abstract class BaseHttpCallback implements Callback {
    private String intent;
    private Context context;
    private boolean sendNotify;
    private IDataContainer dataContainer;
    private static final String TAG =
            BaseHttpCallback.class.getSimpleName();

    protected Context getContext() {
        return context;
    }

    public void processSynchronousResponse(Response response) throws Exception {
        onHttpResponse(null, response);
    }

    protected abstract void onHttpResponse(String jsonResponse) throws Exception;

    private void onHttpResponse(Call call, Response response) throws Exception {
        String url = call != null ? call.request().url().toString() : "sync call";
        Error er = new Error();
        er.setCode(response.code());

        Log.d(TAG, "Response from " + url + " : " + response.code());
        if (response.code() == 200) {
            String myResponse = response.body().string();
            Log.d(TAG, myResponse);
            try {
                onHttpResponse(myResponse);
            } catch (Exception e) {
                Log.e(TAG, "Errore chiamata servizio " + url, e);
            }
        } else {
            Log.e(TAG, "Errore chiamata servizio " + url + " : HTTP Code " + response.code() + " - " + response.message());
        }
    }

    protected void onHttpFailure(Call call, IOException e) {
        Log.d(TAG, "Failed !!", e);
        Error er = new Error();
        er.setCode(999);
        sendNotification(er, "MyBank error : " + e.getMessage());
    }

    public BaseHttpCallback(Context context, String intent, IDataContainer dataContainer, boolean sendNotify) {
        this.context = context;
        this.intent = intent;
        this.sendNotify = sendNotify;
        this.dataContainer = dataContainer;
    }

    protected IDataContainer getDataContainer() {
        return dataContainer;
    }

    protected boolean isSendNotification() {
        return sendNotify;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        onHttpFailure(call, e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

        try {
            onHttpResponse(call, response);
        } catch (Exception e) {
            Log.e(TAG, "Errore gestione httpRespone", e);
        } finally {
            if (response != null)
                response.close();
        }
    }

    protected void sendNotification(Error errore, String _message) {
        String message = _message;
        if (_message == null) message = "Messaggio inviato a MyBank!";
        if (errore != null && errore.getCode() != 0)
            message += " invio fallito!!";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.iconfinder_walletmoneyshoppingatmcard_192)
                .setContentTitle("MyBank notification")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                //.setContentIntent(pendingIntent)
                .setAutoCancel(false);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
        int id = (new Long(System.currentTimeMillis() / 1000)).intValue();
        notificationManager.notify(id, builder.build());
    }

    protected void sendBroadCast(Serializable data) {
        if (intent != null) {
            Intent i = new Intent(intent);
            i.putExtra("DATI", data);
            context.sendBroadcast(i);
        }

    }
}
