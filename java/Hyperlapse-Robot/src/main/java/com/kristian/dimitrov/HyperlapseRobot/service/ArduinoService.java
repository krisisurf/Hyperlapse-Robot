package com.kristian.dimitrov.HyperlapseRobot.service;

import com.kristian.dimitrov.HyperlapseRobot.entity.RulesManagerEntity;

public interface ArduinoService {

    /**
     * Sends json formatted string with rules for the arduino over I2C protocol.
     *
     * @param rules Object with created rules.
     */
    void sendRules(RulesManagerEntity rules);

    /**
     * Request data from the arduino over I2C protocol
     * @param request Requested type of data
     * @return null if the requested data type does not exist in the arduino
     * @return json string with the data
     */
    String requestData(ArduinoRequest request);
}
