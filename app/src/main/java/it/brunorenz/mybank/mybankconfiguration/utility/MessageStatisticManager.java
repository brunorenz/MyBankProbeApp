package it.brunorenz.mybank.mybankconfiguration.utility;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.brunorenz.mybank.mybankconfiguration.bean.MessageStatisticInfo;
import it.brunorenz.mybank.mybankconfiguration.bean.MessageStatisticInfoEntry;

public class MessageStatisticManager {
    private static final String TAG =
            MessageStatisticManager.class.getSimpleName();
    private final static String StatFN = "STAT.json";


    public MessageStatisticInfo readData(Context context) {
        MessageStatisticInfo stat = null;
        try {
            FileManager f = new FileManager(context);
            List<String> l = f.readFile(StatFN);
            if (l != null && !l.isEmpty())
            {
                stat = RESTUtil.jsonDeserialize(l.get(0),MessageStatisticInfo.class);
            }
            if (stat == null)
                stat = new MessageStatisticInfo();
        } catch (Exception e) {
            Log.d(TAG, "Errore in aggiornamento file statistiche", e);
        }
        return stat;
    }

    private void writeData(Context context, MessageStatisticInfo stat) {
        try {
            FileManager f = new FileManager(context);
            List<String> l = new ArrayList<>();
            l.add(RESTUtil.jsonSerialize(stat));
            Log.d(TAG, "Aggiorna statistiche "+l.get(0));
            f.writeFile(StatFN,l);
        } catch (Exception e) {
            Log.d(TAG, "Errore in aggiornamento file statistiche", e);
        }
    }
    private String getCurrentDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
        return sdf.format(new Date());
    }
    public void processMessage(Context context , String type, boolean sent, boolean accepted)
    {
        MessageStatisticInfo stat = readData(context);
        String currentData = getCurrentDate();
        if (!stat.getCurrentDate().equals(currentData))
        {
            stat.setCurrentDate(currentData);
            stat.setSms(new MessageStatisticInfoEntry());
            stat.setPush(new MessageStatisticInfoEntry());
        }
        if (type.equals("SMS"))
        {
            stat.getSms().update(sent,accepted);
            stat.getTotSms().update(sent,accepted);
        } else
        {
            stat.getPush().update(sent,accepted);
            stat.getTotPush().update(sent,accepted);
        }
        writeData(context,stat);
    }
}
