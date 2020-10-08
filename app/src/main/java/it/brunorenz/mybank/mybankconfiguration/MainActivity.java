package it.brunorenz.mybank.mybankconfiguration;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import it.brunorenz.mybank.mybankconfiguration.bean.GenericDataContainer;
import it.brunorenz.mybank.mybankconfiguration.bean.MessageFilterData;
import it.brunorenz.mybank.mybankconfiguration.fragment.MainFragment;
import it.brunorenz.mybank.mybankconfiguration.fragment.SettingFragment;
import it.brunorenz.mybank.mybankconfiguration.httpservice.MyBankServerManager;
import it.brunorenz.mybank.mybankconfiguration.utility.FileManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int SMS_PERMISSION_CODE = 0;
    public static final String CHANNEL_ID = "MyBankCfgCHID";
    private BroadcastReceiver dataUpdateReceiver;
    private MyBankServerManager myBankServer;

    @Override
    protected void onResume() {
        super.onResume();
        registerMainActivityBroadcastReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterMainActivityBroadcastReceiver();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initApplication();
        // Check permission
        if (!hasReadSmsPermission()) {
            showRequestPermissionsInfoAlertDialog("SMS");
        }
        if (!hasNotificationListenerPermission()) {
            showRequestPermissionsInfoAlertDialog("NOTIFY");
        }
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainPage, new MainFragment());
        transaction.commit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        Log.d(TAG, "Android Version : " + Build.VERSION.SDK_INT);
        Log.d(TAG, "Android KITKAT Version : " + Build.VERSION_CODES.KITKAT);
    }

// https://blog.intive-fdv.com/4-tools-create-graphic-resources-android/
    /**
     * Initial task for MyBank
     */
    private void initApplication() {
        // Create notification channel
        createNotificationChannel();
        // create Broadcast receiver
        registerMainActivityBroadcastReceiver();
        // create ServerManager
        myBankServer = MyBankServerManager.createMyBankServerManager(getContext());
        // do logon
        myBankServer.logon(null,MyBankIntents.DATA_LOGON_RESPONSE);
        // start service
        MyBankSMSService.startService(this);
        /*
        Intent i = new Intent(this, MyBankSMSService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(i);
        } else {
            startService(i);
        }
         */
        //
        myBankServer.getMessageFilter(MyBankIntents.DATA_EXCLUDED_SMS_MESSAGE, "SMS");
        myBankServer.getMessageFilter(MyBankIntents.DATA_EXCLUDED_PUSH_MESSAGE, "PUSH");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.mainPage, new SettingFragment());
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        } else if (id == R.id.refresh_filter) {
            myBankServer.getMessageFilter(MyBankIntents.DATA_EXCLUDED_SMS_MESSAGE, "SMS");
            myBankServer.getMessageFilter(MyBankIntents.DATA_EXCLUDED_PUSH_MESSAGE, "PUSH");
            Toast.makeText(this, "Aggiornamento effettuato", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState ..");
    }

    /**
     * Runtime permission shenanigans
     */
    private boolean hasNotificationListenerPermission() {
        Set<String> lst =NotificationManagerCompat.getEnabledListenerPackages(getContext());
        for (String s : lst)
            if (s.contains("mybankconfiguration")) return true;
        return false;
    }

    private boolean hasReadSmsPermission() {
        return ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Optional informative alert dialog to explain the user why the app needs the Read/Send SMS permission
     */
    private void showRequestPermissionsInfoAlertDialog(String tipo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (tipo.equals("SMS")) {
            builder.setTitle(R.string.permission_sms_alert_dialog_title);
            builder.setMessage(R.string.permission_sms_dialog_message);
            builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    requestReadAndSendSmsPermission();
                }
            });
        } else {
            builder.setTitle(R.string.permission_notify_alert_dialog_title);
            builder.setMessage(R.string.permission_notify_dialog_message);
            builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                    startActivity(intent);
                }
            });

        }
        builder.show();
    }

    private void requestNotificationListenPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE)) {
            Log.d(TAG, "shouldShowRequestPermissionRationale(), no permission requested");
            return;
        }
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE},
                SMS_PERMISSION_CODE);
    }

    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_SMS)) {
            Log.d(TAG, "shouldShowRequestPermissionRationale(), no permission requested");
            return;
        }
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                SMS_PERMISSION_CODE);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.CHANNEL_NAME);
            String description = getString(R.string.CHANNEL_DESCRIPTION);
            //int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_LOW);
            channel.setDescription(description);
            channel.setImportance(NotificationManager.IMPORTANCE_LOW);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private BroadcastReceiver createBroadcastReceiver() {
        Log.d(TAG, "Create MainActivity BroadcastReceiver ..");
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "BroadcastReceiver onReceive.. action " + intent.getAction());
                if (intent.getAction().equals(MyBankIntents.DATA_EXCLUDED_PUSH_MESSAGE) || intent.getAction().equals(MyBankIntents.DATA_EXCLUDED_SMS_MESSAGE)) {
                    // Do stuff - maybe update my view based on the changed DB contents
                    Log.d(TAG, "Update Notifications escluded package ..");
                    GenericDataContainer gc = (GenericDataContainer) intent.getSerializableExtra("DATI");
                    if (gc != null) {
                        boolean sms = intent.getAction().equals(MyBankIntents.DATA_EXCLUDED_SMS_MESSAGE);
                        List<String> msg = new ArrayList<>();
                        for (MessageFilterData d : gc.getMessageFilter())
                            msg.add(sms ? d.getSender() : d.getPackageName());
                        if (!msg.isEmpty()) {
                            String file = getString(sms ? R.string.EXCLUDED_SMS : R.string.EXCLUDED_PUSH);
                            FileManager f = new FileManager(getContext());
                            f.writeFile(file, msg);
                            sendBroadCast(sms ? MyBankIntents.DATA_EXCLUDED_SMS_UPDATE : MyBankIntents.DATA_EXCLUDED_PUSH_UPDATE, null);
                        }
                    }
                } else if (intent.getAction().equals(MyBankIntents.DATA_LOGON_RESPONSE))
                {
                    GenericDataContainer gc = (GenericDataContainer) intent.getSerializableExtra("DATI");
                    if (gc != null)
                    {
                        Log.d(TAG, "Update Logon info .. logon : "+gc.isLogonOk());
                    }
                }
            }
        };
    }

    private void unregisterMainActivityBroadcastReceiver() {
        if (dataUpdateReceiver != null) unregisterReceiver(dataUpdateReceiver);
    }

    private void registerMainActivityBroadcastReceiver() {
        if (dataUpdateReceiver == null) dataUpdateReceiver = createBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyBankIntents.DATA_EXCLUDED_PUSH_MESSAGE);
        intentFilter.addAction(MyBankIntents.DATA_EXCLUDED_SMS_MESSAGE);
        intentFilter.addAction(MyBankIntents.DATA_LOGON_RESPONSE);
        registerReceiver(dataUpdateReceiver, intentFilter);
    }

    protected void sendBroadCast(String intent, Serializable data) {
        if (intent != null) {
            Intent i = new Intent(intent);
            if (data != null)
                i.putExtra("DATI", data);
            Log.d(TAG, "Send broadcast messagge to Intet " + intent);
            getContext().sendBroadcast(i);
        }
    }

    protected Context getContext() {
        return this;
    }
}