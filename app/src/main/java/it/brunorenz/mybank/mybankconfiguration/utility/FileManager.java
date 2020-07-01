package it.brunorenz.mybank.mybankconfiguration.utility;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import it.brunorenz.mybank.mybankconfiguration.MyBankSMSService;

public class FileManager {
    private static final String TAG =
            FileManager.class.getSimpleName();
    private Context context;
    private OutputStreamWriter fileWriter;
    private File fileLog;
    private String fileName;
    public FileManager(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    private File getLogFile() {
        if (fileLog == null) {
            //String filename = "MyBankSMSService.txt";
            fileLog = new File(context.getFilesDir(), fileName);
            try {
                if (!fileLog.exists()) {
                    fileLog.createNewFile();
                }
                Toast.makeText(context, "Apro file " + fileLog.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e(TAG, "Errore apertura file", e);
            }
        }
        return fileLog;
    }

    private OutputStreamWriter getWriter() {
        if (fileWriter == null) {
            try {
                FileOutputStream fOut = new FileOutputStream(getLogFile());
                fileWriter = new OutputStreamWriter(fOut);
            } catch (IOException e) {
                Log.e(TAG, "Errore apertura writer", e);
            }
        }
        return fileWriter;
    }

    public void closeLog() {
        try {
            if (fileWriter != null) fileWriter.close();

        } catch (Exception e) {

        }

    }
    public void writeLog(String message) {
        OutputStreamWriter writer = getWriter();
        try {
            if (writer != null && message != null) {
                writer.write(TAG + " : " + message);
                writer.flush();
            }
        } catch (Exception e) {

        }
    }
}
