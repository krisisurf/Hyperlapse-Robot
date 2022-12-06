package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity;

import java.io.Serializable;

public class ArduinoRobot implements Serializable {

    private boolean isConnectionEstablished;
    private String ipAddress;
    private String portNumber;

    public boolean isConnectionEstablished() {
        return isConnectionEstablished;
    }

    public void setConnectionData(String ipAddress, String portNumber){
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getPortNumber() {
        return portNumber;
    }
}
