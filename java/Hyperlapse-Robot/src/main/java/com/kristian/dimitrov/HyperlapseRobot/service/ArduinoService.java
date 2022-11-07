package com.kristian.dimitrov.HyperlapseRobot.service;

public interface ArduinoService {

    /**
     * Sends json formatted string with rules for the arduino over I2C protocol.
     * @param json json string with rules.
     */
    void sendJsonString(String json);

    /**
     * FOR TEST (TEMPORARY)
     * @param c
     */
    void sendChar(char c);
}
