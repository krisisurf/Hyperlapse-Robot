package com.kristian.dimitrov.HyperlapseRobot.service.impl;

import com.kristian.dimitrov.HyperlapseRobot.config.Config;
import com.kristian.dimitrov.HyperlapseRobot.hardware.ContinuousRotationServoComponent;
import com.kristian.dimitrov.HyperlapseRobot.service.ContinuousRotServoService;

public class ContinuousRotServoServiceImpl implements ContinuousRotServoService {

    private ContinuousRotationServoComponent servo;

    public ContinuousRotServoServiceImpl(Config config){
        servo = config.getContinuousRotServo();
    }

    @Override
    public void stop() {
        servo.stop();
    }

    @Override
    public void rotateClockwise() {
        servo.rotateClockwise();
    }

    @Override
    public void rotateCounterClockwise() {
        servo.rotateClockwise();
    }
}
