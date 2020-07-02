package it.brunorenz.mybank.mybankconfiguration;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DataExchangeBReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(RefreshTask.DATA_EXCLUDED_MESSAGE)) {
            // Do stuff - maybe update my view based on the changed DB contents
        }
    }
}
