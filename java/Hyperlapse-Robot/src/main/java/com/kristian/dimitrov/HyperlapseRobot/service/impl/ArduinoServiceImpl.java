package com.kristian.dimitrov.HyperlapseRobot.service.impl;

import com.kristian.dimitrov.HyperlapseRobot.config.Config;
import com.kristian.dimitrov.HyperlapseRobot.entity.RulesManagerEntity;
import com.kristian.dimitrov.HyperlapseRobot.service.ArduinoRequest;
import com.kristian.dimitrov.HyperlapseRobot.service.ArduinoService;
import com.kristian.dimitrov.HyperlapseRobot.utils.Logger;
import org.springframework.stereotype.Service;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ArduinoServiceImpl implements ArduinoService {

    private final int deviceAddress;
    private final int busNumber;

    private boolean sendingData = false;
    private boolean receivingData = false;

    private I2CBus bus;
    private I2CDevice device;

    public ArduinoServiceImpl(Config config) {
        deviceAddress = config.getArduinoI2CAddress();
        busNumber = config.getArduinoI2CBusNumber();

        try {
            Logger.makeLog("Creating I2C bus at number: " + busNumber, new Throwable());
            bus = I2CFactory.getInstance(busNumber);
            Logger.makeLog("Creating I2C device at address: " + deviceAddress, new Throwable());
            device = bus.getDevice(deviceAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sendRules(RulesManagerEntity rulesManagerEntity) {
        // TODO 1: Problem will appear when you try to send new rules, before the old ones are not completely sent/received
        if (sendingData || receivingData)
            return;

        sendDataToArduino(rulesManagerEntity.getShortenedJson(), '\n');
    }

    @Override
    public String requestData(ArduinoRequest request) {
        // TODO 1: Problem will appear when you try to receive data, before the send/receive process is not finished
        if (sendingData || receivingData)
            return null;

        // 'rd' is shortened from 'requestData'
        String json = "{rd:\"" + request + "\"}";
        sendDataToArduino(json, '^');

        return receiveData('\n');
    }

    private void sendDataToArduino(String data, char STOP_SIGNAL) {
        sendingData = true;
        try {
            Logger.makeLog("Attempting to send data to arduino...", new Throwable());
            // Arduino will know that it should not receive more data after the STOP_SIGNAL character appear.
            data += STOP_SIGNAL;

            int chunkLength = 32;
            int chunksCount = data.length() / chunkLength + 1;
            for (int i = 0; i < chunksCount; i++) {
                String chunk = data.substring(i * chunkLength, Math.min((i + 1) * chunkLength, data.length()));
                Logger.makeLog("Writing string chunk (No. " + (i + 1) + " ), data: " + chunk, new Throwable());

                byte[] writeData = chunk.getBytes();
                device.write(chunk.getBytes(), 0, writeData.length);
            }

            Logger.makeLog("All rules has been sent to Arduino.", new Throwable());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        sendingData = false;
    }

    private String receiveData(char STOP_SIGNAL) {
        receivingData = true;
        String result = null;
        try {
            Logger.makeLog("Attempting to receive data from arduino...", new Throwable());
//            StringBuilder receivedData = new StringBuilder();
//            char lastCharacter;
//            while ((lastCharacter = (char) device.read()) != STOP_SIGNAL) {
//                receivedData.append(lastCharacter);
//            }
//
//            result = receivedData.toString();

            int i = device.read();
            Logger.makeLog("Data has been received from arduino: " + i, new Throwable());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        receivingData = false;
        return result;
    }
}
