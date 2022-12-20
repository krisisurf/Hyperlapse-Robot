package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.utills.ConnectionHTTP;

import java.io.IOException;
import java.text.MessageFormat;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class ArduinoRobotConnection implements Runnable {

    /**
     * Connection
     */
    private final Thread connectionThread;
    private boolean connectionThreadIsRunning;
    private final int testConnDelay;

    private boolean isConnectionEstablished;
    private String ipAddress;
    private String portNumber;
    private Request testConnectionRequest;

    private Gson gson;

    public ArduinoRobotConnection(int testConnectionDelayMillis) {
        this.testConnDelay = testConnectionDelayMillis;
        connectionThread = new Thread(this);
        gson = new Gson();
        testConnectionRequest = null;
    }

    public void setConnectionData(String ipAddress, String portNumber) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;

        String testUrl = MessageFormat.format("http://{0}:{1}/api/testConnection", ipAddress, portNumber);
        testConnectionRequest = new Request.Builder()
                .url(testUrl)
                .build();

        testConnectionNow();
    }


    public synchronized void start() {
        if (connectionThreadIsRunning)
            return;

        connectionThread.start();
        connectionThreadIsRunning = true;
    }

    public synchronized void stop() {
        if (!connectionThreadIsRunning)
            return;

        try {
            connectionThreadIsRunning = false;
            connectionThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testConnectionNow() {
        final boolean lastConnStatus = isConnectionEstablished;

        if (ipAddress == null || portNumber == null) {
            isConnectionEstablished = false;
        }

        try {
            ConnectionHTTP.HTTP_GET(testConnectionRequest);
            isConnectionEstablished = true;
        } catch (Exception e) {
            isConnectionEstablished = false;
        }

        if (lastConnStatus != isConnectionEstablished)
            onConnectionStatusListener(isConnectionEstablished);
    }

    public boolean tryToSendRules(RulesManagerEntity rulesManagerEntity) {
        if (!isConnectionEstablished) {
            testConnectionNow();
            if (!isConnectionEstablished)
                return false;
        }

        String url = MessageFormat.format("http://{0}:{1}/api/arduino/runRules", ipAddress, portNumber);
        try {
            String json = gson.toJson(rulesManagerEntity);
            RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
            Response response = ConnectionHTTP.HTTP_POST(url, requestBody);

            if (response.code() == 200)
                return true;

        } catch (Exception e) {
            return false;
        }

        return false;
    }

    @Override
    public void run() {
        connectionThreadIsRunning = true;

        while (true) {
            testConnectionNow();

            try {
                Thread.sleep(testConnDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Override this method. It will get called, when connection status is changed.
     */
    public void onConnectionStatusListener(boolean isConnectionEstablished) {
    }

    public boolean isConnectionEstablished() {
        testConnectionNow();
        return isConnectionEstablished;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getPortNumber() {
        return portNumber;
    }


}
