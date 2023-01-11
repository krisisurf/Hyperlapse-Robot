package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.utills;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConnectionHTTP {

    private final static OkHttpClient client;

    static {
        client = new OkHttpClient();
    }

    public static String HTTP_GET(Request request) throws IOException {

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static Response HTTP_POST(String url, RequestBody formBody) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        return response;
    }
}
