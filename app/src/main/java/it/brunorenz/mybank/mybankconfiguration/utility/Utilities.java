package it.brunorenz.mybank.mybankconfiguration.utility;

import android.util.Log;

import java.security.MessageDigest;

public class Utilities {
    private static final String TAG =
            Utilities.class.getSimpleName();

    public static String getMD5(String in) {
        String out = in;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(in.getBytes());
            byte[] digest = md.digest();
            out = ByteToHex(digest);
        } catch (Exception e) {
            Log.e(TAG, "Errore in applicazione algoritmo MD5", e);
        }
        return out;
    }

    public static String ByteToHex(byte abyte0[]) {
        int i = 0;
        int j = abyte0.length;
        char ac[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuilder sb = new StringBuilder(128);
        if (abyte0 != null) {
            for (int k = i; k < i + j; k++) {
                sb.append(ac[abyte0[k] >> 4 & 0xf]);
                sb.append(ac[abyte0[k] & 0xf]);
            }
        }
        return sb.toString();
    }
}
