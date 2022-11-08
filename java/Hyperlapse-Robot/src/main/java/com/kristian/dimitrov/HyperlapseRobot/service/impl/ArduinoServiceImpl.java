package com.kristian.dimitrov.HyperlapseRobot.service.impl;

import com.kristian.dimitrov.HyperlapseRobot.service.ArduinoService;
import com.kristian.dimitrov.HyperlapseRobot.utils.Logger;
import org.springframework.stereotype.Service;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

            int chunkLength = 32;
            int chunksCount = json.length() / chunkLength + 1;
            for (int i = 0; i < chunksCount; i++) {
                String chunk = json.substring(i * chunkLength, Math.min((i + 1) * chunkLength, json.length()));
                Logger.makeLog("Writing chunk (No. " + (i + 1) + " ), data: " + chunk, new Throwable());

                byte writeData[] = chunk.getBytes();
                device.write(chunk.getBytes(), 0, writeData.length);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
