package it.brunorenz.mybank.mybankconfiguration.network;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import it.brunorenz.mybank.mybankconfiguration.MainActivity;
import it.brunorenz.mybank.mybankconfiguration.R;
import it.brunorenz.mybank.mybankconfiguration.bean.Error;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//import android.support.v4.content.LocalBroadcastManager;

public abstract class BaseHttpCallback implements Callback {
    private String intent;
    private Context context;
    private boolean sendNotify;
    private static final String TAG =
            BaseHttpCallback.class.getSimpleName();

    protected abstract void onHttpResponse(Call call, Response response) throws IOException;

    public BaseHttpCallback(Context context, String intent, boolean sendNotify) {
        this.context = context;
        this.intent = intent;
        this.sendNotify = sendNotify;
    }

    protected boolean isSendNotification()
    {
        return sendNotify;
    }
    @Override
    public void onFailure(Call call, IOException e) {
        Log.d(TAG, "Failed !!", e);
        Error er = new Error();
        er.setCode(999);
        sendNotification(er,"MyBank error : "+e.getMessage());
        //call.cancel();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        onHttpResponse(call, response);
        /*
        if (intent != null)
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(intent));
        */
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
}
