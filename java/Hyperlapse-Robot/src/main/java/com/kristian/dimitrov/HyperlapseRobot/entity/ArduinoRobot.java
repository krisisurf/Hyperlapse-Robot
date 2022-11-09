package com.kristian.dimitrov.HyperlapseRobot.entity;

public class ArduinoRobot {

    // Wheel radius in centimeters
    private final float wheelRadius;

    // Hardware limit of the motor steps count for one revolution (360 degrees)
    private final int movementMotorStepsPerRevolution;

    // Max speed in steps per second for the Right and left side motors
    private final float movementMotorMaxSpeed;

    // Hardware limit of the motor steps count for one revolution (360 degrees)
    private final int cameraMotorStepsPerRevolution;

    // Max speed in steps per second for the Right and left side motors
    private final float cameraMotorMaxSpeed;

    public ArduinoRobot(float wheelRadius, int movementMotorStepsPerRevolution, float movementMotorMaxSpeed, int cameraMotorStepsPerRevolution, float cameraMotorMaxSpeed) {
        this.wheelRadius = wheelRadius;
        this.movementMotorStepsPerRevolution = movementMotorStepsPerRevolution;
        this.movementMotorMaxSpeed = movementMotorMaxSpeed;
        this.cameraMotorStepsPerRevolution = cameraMotorStepsPerRevolution;
        this.cameraMotorMaxSpeed = cameraMotorMaxSpeed;
    }


}
