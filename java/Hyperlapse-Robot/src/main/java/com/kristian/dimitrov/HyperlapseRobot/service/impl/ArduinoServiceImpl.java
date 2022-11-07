package com.kristian.dimitrov.HyperlapseRobot.service.impl;

import com.kristian.dimitrov.HyperlapseRobot.service.ArduinoService;
import com.kristian.dimitrov.HyperlapseRobot.utils.Logger;
import org.springframework.stereotype.Service;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

@Service
public class ArduinoServiceImpl implements ArduinoService {

    private final int DEVICE_ADDRESS = 0x08;
    private final int BUS_NUMBER = I2CBus.BUS_1;

    @Override
    public void sendJsonString(String json) {
        try {
            Logger.makeLog("Creating I2C bus", new Throwable());
            I2CBus bus = I2CFactory.getInstance(BUS_NUMBER);

            Logger.makeLog("Creating I2C device", new Throwable());
            I2CDevice device = bus.getDevice(DEVICE_ADDRESS);
            byte[] writeData = json.getBytes();

            Logger.makeLog("Writing bytes: " + writeData, new Throwable());
            device.write(writeData, 0, writeData.length);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void sendChar(char c) {
        try {
            Logger.makeLog("Creating I2C bus", new Throwable());
            I2CBus bus = I2CFactory.getInstance(BUS_NUMBER);

            Logger.makeLog("Creating I2C device", new Throwable());
            I2CDevice device = bus.getDevice(DEVICE_ADDRESS);
            byte[] writeData = new byte[1];
            writeData[0] = (byte) c;

            Logger.makeLog("Writing bytes: " + writeData, new Throwable());
            device.write(writeData, 0, writeData.length);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
