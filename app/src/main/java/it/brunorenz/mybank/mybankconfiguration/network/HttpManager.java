package it.brunorenz.mybank.mybankconfiguration.network;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import it.brunorenz.mybank.mybankconfiguration.R;
import okhttp3.Callback;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.TlsVersion;

public class HttpManager {
    
    private static final long CONNECT_TIMEOUT = 5000;   // 2 seconds
    private static final long READ_TIMEOUT = 5000;      // 2 seconds
    private static OkHttpClient okHttpClient = null;
    private static final String TAG =
            HttpManager.class.getSimpleName();

    protected Context getContext() {
        return context;
    }

    protected void setContext(Context context) {
        this.context = context;
    }

    private Context context;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    protected Request getPostRequest(String url, String postBody) {
        RequestBody body = RequestBody.create(postBody, JSON);
        Request request = new Request.Builder()
                .url(url)
                .header("authorization", context.getString(R.string.BASICAUTH))
                .post(body)
                .build();
        return request;
    }

    protected Request getGetRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .header("authorization", context.getString(R.string.BASICAUTH))
                .build();
        return request;
    }

    protected void callHttpGet(String url, Callback cb) {
        Log.d(TAG, "Call GET Url " + url);
        callHttp(getGetRequest(url), cb);
    }

    protected void callHttpPost(String url, String postBody, Callback cb) {
        Log.d(TAG, "Call POST Url " + url + " -> " + postBody);
        callHttp(getPostRequest(url, postBody), cb);
    }

    protected void callHttp(Request request, Callback cb) {
        //Log.d(TAG, "Call .. "+request.toString());
        buildClient().newCall(request).enqueue(cb);
    }

    private OkHttpClient buildClient() {
        if (okHttpClient != null) return okHttpClient;

        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)

                .cipherSuites(

                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384)
                .build();
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
        //.connectionSpecs(Collections.singletonList(spec));

        okHttpClient = okHttpClientBuilder.build();
        return okHttpClient;
    }
}
