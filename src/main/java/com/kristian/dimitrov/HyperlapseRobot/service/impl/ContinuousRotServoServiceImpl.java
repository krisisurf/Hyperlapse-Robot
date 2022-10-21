package com.kristian.dimitrov.HyperlapseRobot.service.impl;

import com.kristian.dimitrov.HyperlapseRobot.config.Config;
import com.kristian.dimitrov.HyperlapseRobot.hardware.ContinuousRotationServoComponent;
import com.kristian.dimitrov.HyperlapseRobot.service.ContinuousRotServoService;
import com.kristian.dimitrov.HyperlapseRobot.utils.Logger;
import org.springframework.stereotype.Service;

@Service
public class ContinuousRotServoServiceImpl implements ContinuousRotServoService {

    private ContinuousRotationServoComponent servo;

    public ContinuousRotServoServiceImpl(Config config) {
        servo = config.getContinuousRotServo();
    }

    @Override
    public void stop() {
        servo.stop();
        makeLog("STOPPED", new Throwable());
    }

    @Override
    public void rotateClockwise() {
        servo.rotateClockwise();
        makeLog("ROTATING CLOCKWISE", new Throwable());
    }

    @Override
    public void rotateCounterClockwise() {
        servo.rotateCounterclockwise();
        makeLog("ROTATING COUNTER-CLOCKWISE", new Throwable());
    }

    private void makeLog(String message, Throwable t) {
        Logger.makeLog(servo.getName() + " -> " + message + " | Servo Description: " + servo.getDescription() + " | Signal Address: " + servo.getAddress(), t);
    }
}
