package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.utills.ConnectionHTTP;

import java.text.MessageFormat;

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

    public ArduinoRobotConnection(int testConnectionDelayMillis) {
        this.testConnDelay = testConnectionDelayMillis;
        connectionThread = new Thread(this);
    }

    public void setConnectionData(String ipAddress, String portNumber) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
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

        String testUrl = MessageFormat.format("http://{0}:{1}/api/testConnection", ipAddress, portNumber);
        try {
            ConnectionHTTP.HTTP_GET(testUrl);
            isConnectionEstablished = true;
        } catch (Exception e) {
            isConnectionEstablished = false;
        }

        if (lastConnStatus != isConnectionEstablished)
            onConnectionStatusListener(isConnectionEstablished);
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
