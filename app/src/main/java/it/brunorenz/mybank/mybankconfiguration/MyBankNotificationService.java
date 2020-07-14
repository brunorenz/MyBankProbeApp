package it.brunorenz.mybank.mybankconfiguration;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.preference.PreferenceManager;
import it.brunorenz.mybank.mybankconfiguration.bean.RegisterSMSRequest;
import it.brunorenz.mybank.mybankconfiguration.httpservice.MyBankServerManager;
import it.brunorenz.mybank.mybankconfiguration.utility.FileManager;
import it.brunorenz.mybank.mybankconfiguration.utility.MessageStatisticManager;


public class MyBankNotificationService extends NotificationListenerService {
    private static final String TAG =
            MyBankNotificationService.class.getSimpleName();

    private static final String[] COMMONAPPS = {
            "com.facebook", "com.whatsapp", "com.instagram", "it.brunorenz"
    };

    private List<String> filter;

//    public IBinder onBind(Intent intent) {
//        Log.d(TAG, ">> ON BIND");
//        return super.onBind(intent);
//    }

    public void onCreate() {
        super.onCreate();
        Log.d(TAG, ">> ON CREATE");
        startBroadCastReceiver();
    }

    public void onNotificationPosted(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();
        Log.d(TAG, "Received PUSH notify from " + packageName);
        if (validMessage(packageName)) {
            Log.d(TAG, "id = " + sbn.getId() + "Package Name" + sbn.getPackageName() +
                    "Post time = " + sbn.getPostTime() + "Tag = " + sbn.getTag());
            RegisterSMSRequest request = new RegisterSMSRequest();
            request.setType("PUSH");
            request.setSender(sbn.getNotification().extras.getString(Notification.EXTRA_TITLE));
            request.setMessage(sbn.getNotification().extras.getString(Notification.EXTRA_TEXT));
            request.setPackgeName(packageName);
            MyBankServerManager server = MyBankServerManager.createMyBankServerManager(this);
            server.registerSMS(request, null, true);
        } else
        {
            MessageStatisticManager stat = new MessageStatisticManager();
            stat.processMessage(this, "PUSH",false,false);
        }
    }

    /**
     * verifica se messaggio valido
     *
     * @param packageName
     * @return
     */
    private boolean validMessage(String packageName) {
        if (isFilterEnabled(this) && packageName != null) {
            List<String> filter = getFilter();
            if (filter != null && !filter.isEmpty()) {
                for (String p : filter) {
                    //Log.d(TAG, "Check packageName " + packageName + " - compare to " + p);
                    if (packageName.startsWith(p)) return false;
                }
            } else
                for (String p : COMMONAPPS) if (packageName.startsWith(p)) return false;
        }
        return true;
    }

    /**
     * Verifica se filtri messaggi abilitati
     *
     * @param context
     * @return
     */
    private boolean isFilterEnabled(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean filter = pref.getBoolean(context.getString(R.string.PRE_PUSH_FILTER), true);
        Log.d(TAG, "Enable PUSH filter : " + filter);
        return filter;
    }

    /**
     * Gestine evento rilettura filtri da server
     */
    private void startBroadCastReceiver() {
        final IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(MyBankIntents.DATA_EXCLUDED_PUSH_MESSAGE);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(MyBankIntents.DATA_EXCLUDED_PUSH_MESSAGE)) {
                    Log.d(TAG, "Update PUSH filter!");
                    filter = null;
                    getFilter();
                }
            }
        };
        Log.d(TAG, ">> REGISTER RECEIVER for " + MyBankIntents.DATA_EXCLUDED_PUSH_MESSAGE);
        this.registerReceiver(receiver, iFilter);
    }

    /**
     * Recupera filtri da private storage
     *
     * @return
     */
    private List<String> getFilter() {
        if (filter == null) filter = new ArrayList<>();
        if (filter.isEmpty()) {
            FileManager f = new FileManager(getApplicationContext());
            filter = f.readFile(getApplicationContext().getString(R.string.EXCLUDED_PUSH));
            Log.d(TAG, "Reads " + filter.size() + " record from PUSH filter");
        }
        return filter;
    }
}
