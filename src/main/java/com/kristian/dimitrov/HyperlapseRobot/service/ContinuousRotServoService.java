package com.kristian.dimitrov.HyperlapseRobot.service;

public interface ContinuousRotServoService {
    void stop();
    void rotateClockwise(int frequency, float clockwiseRotationDutyCycle);
    void rotateCounterClockwise(int frequency);
}
