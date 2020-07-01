package it.brunorenz.mybank.mybankconfiguration;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import it.brunorenz.mybank.mybankconfiguration.bean.RegisterSMSRequest;
import it.brunorenz.mybank.mybankconfiguration.service.MyBankServerManager;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG =
            SmsBroadcastReceiver.class.getSimpleName();

    public SmsBroadcastReceiver() {
        Log.d(TAG, "Register " + TAG);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String smsSender = "";
        StringBuffer smsBody = new StringBuffer("");
        if (intent.getAction().equals((Telephony.Sms.Intents.SMS_RECEIVED_ACTION))) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                smsSender = smsMessage.getDisplayOriginatingAddress();
                smsBody.append(smsMessage.getMessageBody());
            }

            RegisterSMSRequest request = new RegisterSMSRequest();
            request.setType("SMS");
            request.setSender(smsSender);
            request.setMessage(smsBody.toString());

            //Log.d(TAG, "SMS from " + smsSender);
            //Log.d(TAG, "SMS text " + smsBody.toString());
            MyBankServerManager server = MyBankServerManager.createMyBankServerManager(context);
            server.registerSMS(request, null,true);
            //sendNotification(server.getContext());
        }

    }
/*
    private void sendNotification(Context context)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.iconfinder_walletmoneyshoppingatmcard_192)
                .setContentTitle("MyBank notification")
                .setContentText("SMS inviato a MyBank!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                //.setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
        int id = (new Long(System.currentTimeMillis() / 1000)).intValue();
        notificationManager.notify(id, builder.build());
    }
    */
}
