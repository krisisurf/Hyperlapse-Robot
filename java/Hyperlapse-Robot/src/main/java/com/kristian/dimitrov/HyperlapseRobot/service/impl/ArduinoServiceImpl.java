package com.kristian.dimitrov.HyperlapseRobot.service.impl;

import com.kristian.dimitrov.HyperlapseRobot.config.Config;
import com.kristian.dimitrov.HyperlapseRobot.entity.RulesManagerEntity;
import com.kristian.dimitrov.HyperlapseRobot.service.ArduinoService;
import com.kristian.dimitrov.HyperlapseRobot.utils.Logger;
import org.springframework.stereotype.Service;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

@Service
public class ArduinoServiceImpl implements ArduinoService {

    private final int deviceAddress;
    private final int busNumber;
    private final static String STOP_SIGNAL = "\n";

    private final Thread thread;
    private RulesManagerEntity rulesManagerEntity;

    public ArduinoServiceImpl(Config config) {
        deviceAddress = config.getArduinoI2CAddress();
        busNumber = config.getArduinoI2CBusNumber();

        thread = new Thread(this::run);
    }

    @Override
    public void sendRules(RulesManagerEntity rulesManagerEntity) {
        // TODO 1: Problem will appear when you try to send new rules, before the old ones are not completely sent
        if(thread.isAlive())
            return;

        this.rulesManagerEntity = rulesManagerEntity;
        thread.start();
    }

    private void run() {
        try {
            Logger.makeLog("Creating I2C bus at number: " + busNumber, new Throwable());
            I2CBus bus = I2CFactory.getInstance(busNumber);

            Logger.makeLog("Creating I2C device at address: " + deviceAddress, new Throwable());
            I2CDevice device = bus.getDevice(deviceAddress);

            String json = rulesManagerEntity.getShortenedJson();

            // Arduino will know that it should not receive more data after the STOP_SIGNAL character appear.
            json += STOP_SIGNAL;

            int chunkLength = 32;
            int chunksCount = json.length() / chunkLength + 1;
            for (int i = 0; i < chunksCount; i++) {
                String chunk = json.substring(i * chunkLength, Math.min((i + 1) * chunkLength, json.length()));
                Logger.makeLog("Writing string chunk (No. " + (i + 1) + " ), data: " + chunk, new Throwable());

                byte[] writeData = chunk.getBytes();
                device.write(chunk.getBytes(), 0, writeData.length);
            }

            Logger.makeLog("All rules has been sent to Arduino.", new Throwable());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
