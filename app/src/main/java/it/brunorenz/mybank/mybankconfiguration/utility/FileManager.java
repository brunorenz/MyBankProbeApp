package it.brunorenz.mybank.mybankconfiguration.utility;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

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

    public FileManager(Context context) {
        this(context, null);
    }

    private File getLogFile() {
        if (fileLog == null) {
            fileLog = new File(context.getFilesDir(), fileName);
            try {
                if (!fileLog.exists()) {
                    fileLog.createNewFile();
                }
                Log.d(TAG, "Apro file " + fileLog.getAbsolutePath());
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

    public void writeLine(String message) {
        OutputStreamWriter writer = getWriter();
        try {
            if (writer != null && message != null) {
                writer.write(TAG + " : " + message);
                writer.flush();
            }
        } catch (Exception e) {

        }
    }

    public String readLine() {
        /*
        getBufferedReader();
        FileReader fr=new FileReader(file);   //reads the file
        BufferedReader br=new BufferedReader(fr);  //cr

         */
        return null;
    }

    public int writeFile(String fileName, List<String> content) {
        int rc = 0;
        try {
            File file = new File(context.getFilesDir(), fileName);
            file.delete();
            file.createNewFile();
            OutputStream ios = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(ios);
            for (String l : content)
                osw.write(l);
            osw.close();
        } catch (Exception e) {
            Log.e(TAG, "Errore in scrittura file " + fileName, e);
            rc = 8;
        }

        return rc;
    }

    public List<String> readFile(String fileName) {
        List<String> c = new ArrayList<>();
        File file = new File(context.getFilesDir(), fileName);
        try {
            if (fileLog.exists()) {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String line;
                while ((line = br.readLine()) != null) c.add(line);
            }

        } catch (Exception e) {
            Log.e(TAG, "Errore in lettura file " + fileName, e);
        }


        return c;
    }

    private BufferedReader getBufferedReader() {
    return null;
    }
}
