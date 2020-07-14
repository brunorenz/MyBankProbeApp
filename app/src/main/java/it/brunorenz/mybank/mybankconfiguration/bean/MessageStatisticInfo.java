package it.brunorenz.mybank.mybankconfiguration.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageStatisticInfo {
    String currentDate;
    String firstDate;
    MessageStatisticInfoEntry sms;
    MessageStatisticInfoEntry push;
    MessageStatisticInfoEntry totSms;
    MessageStatisticInfoEntry totPush;

    public MessageStatisticInfo() {
        currentDate = _getCurrentDate();
        firstDate = currentDate;
        sms = new MessageStatisticInfoEntry();
        push = new MessageStatisticInfoEntry();
        totSms = new MessageStatisticInfoEntry();
        totPush = new MessageStatisticInfoEntry();

    }

    private String _getCurrentDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");
        return sdf.format(new Date());
    }

    public void checkDate()
    {
        if (!_getCurrentDate().equals(currentDate))
        {
            currentDate = _getCurrentDate();
            sms = new MessageStatisticInfoEntry();
            push = new MessageStatisticInfoEntry();
        }
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(String firstDate) {
        this.firstDate = firstDate;
    }

    public MessageStatisticInfoEntry getSms() {
        return sms;
    }

    public void setSms(MessageStatisticInfoEntry sms) {
        this.sms = sms;
    }

    public MessageStatisticInfoEntry getPush() {
        return push;
    }

    public void setPush(MessageStatisticInfoEntry push) {
        this.push = push;
    }

    public MessageStatisticInfoEntry getTotSms() {
        return totSms;
    }

    public void setTotSms(MessageStatisticInfoEntry totSms) {
        this.totSms = totSms;
    }

    public MessageStatisticInfoEntry getTotPush() {
        return totPush;
    }

    public void setTotPush(MessageStatisticInfoEntry totPush) {
        this.totPush = totPush;
    }
}
