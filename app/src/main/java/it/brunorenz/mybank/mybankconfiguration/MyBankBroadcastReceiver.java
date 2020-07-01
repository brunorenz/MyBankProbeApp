package it.brunorenz.mybank.mybankconfiguration;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class MyBankBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG =
            MyBankBroadcastReceiver.class.getSimpleName();

    public MyBankBroadcastReceiver() {
        Log.d(TAG, "Register " + TAG);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String smsSender = "";
        StringBuffer smsBody = new StringBuffer("");
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent i = new Intent(context, MyBankSMSService.class);
            Log.d(TAG, "Start service  " + MyBankSMSService.class.getName());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(i);
            } else {
                context.startService(i);
            }
        }
    }
}
