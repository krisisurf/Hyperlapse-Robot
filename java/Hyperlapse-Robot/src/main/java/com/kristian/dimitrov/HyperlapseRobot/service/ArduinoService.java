package com.kristian.dimitrov.HyperlapseRobot.service;

import com.kristian.dimitrov.HyperlapseRobot.entity.RulesManagerEntity;

public interface ArduinoService {

    /**
     * Sends json formatted string with rules for the arduino over I2C protocol.
     *
     * @param rules Object with created rules.
     */
    void sendRules(RulesManagerEntity rules);

}
