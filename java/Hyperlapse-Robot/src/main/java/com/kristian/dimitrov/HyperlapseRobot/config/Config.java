package com.kristian.dimitrov.HyperlapseRobot.config;

import com.kristian.dimitrov.HyperlapseRobot.entity.ArduinoRobot;
import com.pi4j.io.i2c.I2CBus;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.kristian.dimitrov.HyperlapseRobot")
public class Config {

    private final int ARDUINO_I2C_ADDRESS = 0x08;
    private final int ARDUINO_I2C_BUS_NUMBER = I2CBus.BUS_1;
    private ArduinoRobot arduinoRobot;

    public int getArduinoI2CAddress() {
        return ARDUINO_I2C_ADDRESS;
    }

    public int getArduinoI2CBusNumber() {
        return ARDUINO_I2C_BUS_NUMBER;
    }

    public ArduinoRobot getArduinoRobot() {
        return arduinoRobot;
    }

    public void setArduinoRobot(ArduinoRobot arduinoRobot) {
        this.arduinoRobot = arduinoRobot;
    }
}