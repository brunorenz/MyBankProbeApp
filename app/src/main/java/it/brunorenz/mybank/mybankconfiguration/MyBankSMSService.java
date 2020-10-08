package it.brunorenz.mybank.mybankconfiguration;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import it.brunorenz.mybank.mybankconfiguration.bean.GenericDataContainer;

public class MyBankSMSService extends Service {
    private static final String TAG =
            MyBankSMSService.class.getSimpleName();

    private SmsBroadcastReceiver smsReceiver;
    private PendingIntent pendingIntent;
    private static int SERVICE_ID = 21011965;

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
        // create intent for Notification
        Intent notificationIntent = new Intent(this, MainActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        // start service
        startForeground(SERVICE_ID, buildNotification(false));
        //createNotification();
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
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    GenericDataContainer gc = (GenericDataContainer) intent.getSerializableExtra("DATI");
                    mNotificationManager.notify(SERVICE_ID, buildNotification(gc.isLogonOk()));
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
        /*
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        // check logon status
        builder = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.iconfinder_walletmoneyshoppingatmcard_192)
                .setContentTitle("MyBank notification")
                .setContentText("MyBank message probe - offline")
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);
        //builder.a
        int SERVICE_ID = 21011965;

        startForeground(SERVICE_ID, builder.build());
        */
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

    public static void startService(Context ctx) {
        Log.d(TAG, "Start service  " + MyBankSMSService.class.getName());
        Intent i = new Intent(ctx, MyBankSMSService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ctx.startForegroundService(i);
        } else {
            ctx.startService(i);
        }
    }

    /*
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    mNotificationManager.notify(NOTIF_ID, notification);
     */
    private Notification buildNotification(boolean logon) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                        .setContentTitle("MyBank")
                        .setCategory(NotificationCompat.CATEGORY_SERVICE)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setOngoing(true)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(false);
        if (logon) {
            builder.setSmallIcon(R.drawable.iconfinder_walletmoneyshoppingatmcard_192).setContentText("MyBank message probe");
        } else {
            builder.setSmallIcon(R.drawable.iconfinder_walletmoneyshoppingatmcard_192).setContentText("MyBank message probe - offline");
        }
        return builder.build();
    }
}
