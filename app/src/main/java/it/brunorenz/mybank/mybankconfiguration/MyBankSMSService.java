package it.brunorenz.mybank.mybankconfiguration;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import it.brunorenz.mybank.mybankconfiguration.bean.GenericDataContainer;

public class MyBankSMSService extends Service {
    private static final String TAG =
            MyBankSMSService.class.getSimpleName();

    private SmsBroadcastReceiver smsReceiver;
    private NotificationCompat.Builder builder;

    public MyBankSMSService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service Started.");
        /*
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        builder = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.iconfinder_walletmoneyshoppingatmcard_192)
                .setContentTitle("MyBank notification")
                .setContentText("MyBank message probe")
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);
        // rigester the receiver
        final IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.addAction(MyBankIntents.DATA_EXCLUDED_SMS_UPDATE);
        smsReceiver = new SmsBroadcastReceiver();
        this.registerReceiver(smsReceiver, filter);
        int SERVICE_ID = 21011965;
        //builder.add
        startForeground(SERVICE_ID, builder.build());

         */
        // create notification
        createNotification();
        // create SMS receiver
        createSMSBroadCastReceiver();
        //
        cretaLocalReceiver();
    }

    private void cretaLocalReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyBankIntents.DATA_LOGON_RESPONSE);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(MyBankIntents.DATA_LOGON_RESPONSE)) {
                    if (builder != null) {
                        GenericDataContainer gc = (GenericDataContainer) intent.getSerializableExtra("DATI");
                        if (gc.isLogonOk())
                            builder.setSmallIcon(R.drawable.ic_walletmoneyshoppingatmcard_192).setContentText("MyBank message probe").build();
                        else
                            builder.setSmallIcon(R.drawable.iconfinder_walletmoneyshoppingatmcard_192).setContentText("MyBank message probe - offline").build();
                    }
                }
            }
        }, intentFilter);
    }

    private void createSMSBroadCastReceiver() {
        Log.i(TAG, "Create SmsBroadcastReceiver ..");
        final IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.addAction(MyBankIntents.DATA_EXCLUDED_SMS_UPDATE);
        smsReceiver = new SmsBroadcastReceiver();
        this.registerReceiver(smsReceiver, filter);
    }

    private void createNotification() {
        Log.i(TAG, "Create notification ..");
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        builder = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.iconfinder_walletmoneyshoppingatmcard_192)
                .setContentTitle("MyBank notification")
                .setContentText("MyBank message probe - offline")
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);
        int SERVICE_ID = 21011965;
        startForeground(SERVICE_ID, builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        return START_STICKY; // run until explicitly stopped.
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (smsReceiver != null)
            this.unregisterReceiver(smsReceiver);
        Log.i(TAG, "Service Stopped.");
    }
}
