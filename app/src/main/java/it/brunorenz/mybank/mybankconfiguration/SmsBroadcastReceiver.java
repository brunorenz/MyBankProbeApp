package it.brunorenz.mybank.mybankconfiguration;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.preference.PreferenceManager;
import it.brunorenz.mybank.mybankconfiguration.bean.RegisterSMSRequest;
import it.brunorenz.mybank.mybankconfiguration.httpservice.MyBankServerManager;
import it.brunorenz.mybank.mybankconfiguration.utility.FileManager;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG =
            SmsBroadcastReceiver.class.getSimpleName();

    public SmsBroadcastReceiver() {
        Log.d(TAG, "Register " + TAG);
    }
    private List<String> filter;
    @Override
    public void onReceive(Context context, Intent intent) {
        String smsSender = "";
        StringBuffer smsBody = new StringBuffer("");
        if (intent.getAction().equals((Telephony.Sms.Intents.SMS_RECEIVED_ACTION))) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                smsSender = smsMessage.getDisplayOriginatingAddress();
                smsBody.append(smsMessage.getMessageBody());
            }
            if (validMessage(smsSender,context)) {
                RegisterSMSRequest request = new RegisterSMSRequest();
                request.setType("SMS");
                request.setSender(smsSender);
                request.setMessage(smsBody.toString());
                MyBankServerManager server = MyBankServerManager.createMyBankServerManager(context);
                server.registerSMS(request, null, true);
            }
        } else if (intent.getAction().equals(MyBankIntents.DATA_EXCLUDED_SMS_UPDATE))
        {
            Log.d(TAG,"Update SMS filter!");
            filter = null;
            getFilter(context);
        }

    }

    private boolean validMessage(String sender,Context context) {
        if (isFilterEnabled(context) && sender != null) {
            List<String> filter = getFilter(context);
            if (filter != null && !filter.isEmpty()) {
                for (String p : filter) {
                    Log.d(TAG,"Check sender "+sender+" - compare to "+p);
                    if (sender.startsWith(p)) return false;
                }
            }
        }
        return true;
    }

    private List<String> getFilter(Context context) {
        if (filter == null) filter = new ArrayList<>();
        if (filter.isEmpty()) {
            FileManager f = new FileManager(context);
            filter = f.readFile(context.getString(R.string.EXCLUDED_SMS));
            Log.d(TAG,"Reads "+filter.size()+" record from SMS filter");
        }
        return filter;
    }

    private boolean isFilterEnabled(Context context)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean filter = pref.getBoolean(context.getString(R.string.PRE_SMS_FILTER), true);
        Log.d(TAG,"Enable SMS filter : "+filter);
        return filter;
    }
}
