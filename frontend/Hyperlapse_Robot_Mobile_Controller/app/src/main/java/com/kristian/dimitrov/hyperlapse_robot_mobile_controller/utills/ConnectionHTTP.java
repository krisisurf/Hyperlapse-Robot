package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.utills;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectionHTTP {

    private final static OkHttpClient client;

    static {
        client = new OkHttpClient();
    }

    public static String HTTP_GET(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

}
