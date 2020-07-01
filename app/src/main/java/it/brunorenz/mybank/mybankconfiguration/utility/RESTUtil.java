package it.brunorenz.mybank.mybankconfiguration.utility;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RESTUtil {

    static public <T extends Object> T jsonDeserialize(String msg, Class<T> cl) throws Exception {
        if (msg != null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(msg, cl);
        }
        return null;
    }

    static public String jsonSerialize(Object o) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
        return mapper.writeValueAsString(o);
    }

    static public <T extends Object> List<T> jsonDeserializeCollection(String msg, Class<T> cl) throws Exception {
        if (msg != null) {
            ObjectMapper mapper = new ObjectMapper();
            JavaType type = mapper.constructType(cl);
            CollectionType c = CollectionType.construct(List.class, type);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(msg, c);
        }
        return null;
    }

    static public String callRest(String host, String request, String funzione, String userId, String pwd, boolean trace) throws Exception {
        String response = "";

        return response;
    }

    static public String callRest(String host, String request, String funzione, String userId, String pwd) throws Exception {
        return callRest(host, request, funzione, userId, pwd, true);
    }

    static public String jsonFormatter(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Object jsonObject = mapper.readValue(json, Object.class);
        String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        return prettyJson;
    }

    static public void httpGetXX(String url, String json) {

        OkHttpClient client = new OkHttpClient();
        //.header("Authorization", "replace this text with your token")
        //RequestBody body = RequestBody.create(JSON, postBody);
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String myResponse = response.body().string();
                Log.d("TermpApp", myResponse);

            }
        });
    }

}

