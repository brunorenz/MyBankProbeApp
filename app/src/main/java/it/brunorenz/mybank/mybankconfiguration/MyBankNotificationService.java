package it.brunorenz.mybank.mybankconfiguration;

import android.app.Notification;
import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import it.brunorenz.mybank.mybankconfiguration.bean.RegisterSMSRequest;
import it.brunorenz.mybank.mybankconfiguration.service.MyBankServerManager;


public class MyBankNotificationService extends NotificationListenerService {
    private static final String TAG =
            MyBankNotificationService.class.getSimpleName();

    private static final String[] COMMONAPPS = {
            "it.mybank", "com.facebook", "com.whatsapp", "com.instagram"
    };

    public IBinder onBind(Intent intent) {
        Log.d(TAG, ">> ON BIND");
        return super.onBind(intent);
    }

    public void onCreate() {
        super.onCreate();
        Log.d(TAG, ">> ON CREATE");
    }

    public void onNotificationPosted(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();
        if (validMessage(packageName)) {
            Log.d(TAG, "id = " + sbn.getId() + "Package Name" + sbn.getPackageName() +
                    "Post time = " + sbn.getPostTime() + "Tag = " + sbn.getTag());
            //if (!packageName.contains("mybank")) {
            RegisterSMSRequest request = new RegisterSMSRequest();
            request.setType("PUSH");
            request.setSender(sbn.getNotification().extras.getString(Notification.EXTRA_TITLE));
            request.setMessage(sbn.getNotification().extras.getString(Notification.EXTRA_TEXT));
            request.setPackgeName(packageName);

            //Log.d(TAG, "PUSG from " + request.getType());
            //Log.d(TAG, "PUSH text " + request.getMessage());
            MyBankServerManager server = MyBankServerManager.createMyBankServerManager(this);
            server.registerSMS(request, null, true);
            //}
        }

    }

    private boolean validMessage(String packageName) {
        if (packageName != null)
            for (String p : COMMONAPPS) if (packageName.startsWith(p)) return false;
        return true;
    }
}
