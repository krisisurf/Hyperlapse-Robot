package com.kristian.dimitrov.HyperlapseRobot.service;

public interface ContinuousRotServoService {
    void stop();
    void rotateClockwise(int frequency, int clockwiseRotationDutyCycle);
    void rotateCounterClockwise(int frequency);
}
