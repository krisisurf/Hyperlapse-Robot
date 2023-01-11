package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper.CameraStepMotorEntity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper.MovementStepMotorEntity;

import java.io.Serializable;

public class ArduinoRobot implements Serializable {

    /**
     * Wheel radius in centimeters.
     */
    private double wheelRadius;

    private MovementStepMotorEntity leftMotor;
    private MovementStepMotorEntity rightMotor;
    private CameraStepMotorEntity cameraPanMotor;
    private CameraStepMotorEntity cameraTiltMotor;

    private final RulesManagerEntity rulesManagerEntity;

    public ArduinoRobot() {
        rulesManagerEntity = new RulesManagerEntity();
    }

    public void setHardwareData(double wheelRadius, MovementStepMotorEntity leftMotor, MovementStepMotorEntity rightMotor, CameraStepMotorEntity cameraPanMotor, CameraStepMotorEntity cameraTiltMotor) {
        setWheelRadius(wheelRadius);
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.cameraPanMotor = cameraPanMotor;
        this.cameraTiltMotor = cameraTiltMotor;
    }

    public RulesManagerEntity getRulesManagerEntity() {
        return rulesManagerEntity;
    }

    public void addRule(RuleEntity ruleEntity) {
        rulesManagerEntity.addRule(ruleEntity);
    }

    public double getWheelRadius() {
        return wheelRadius;
    }

    public void setWheelRadius(double wheelRadius) {
        this.wheelRadius = wheelRadius;
        rulesManagerEntity.setWheelRadius(wheelRadius);
    }

    public MovementStepMotorEntity getLeftMotor() {
        return leftMotor;
    }

    public MovementStepMotorEntity getRightMotor() {
        return rightMotor;
    }

    public CameraStepMotorEntity getCameraPanMotor() {
        return cameraPanMotor;
    }

    public CameraStepMotorEntity getCameraTiltMotor() {
        return cameraTiltMotor;
    }

    /**
     * Converts distance to number of steps (step motor), that it takes for a wheel with radius to travel the given distance.
     *
     * @param centimeters              Distance in centimeters
     * @param wheelRadiusInCentimeters Radius of the rotational wheel placed on the stepper motor
     * @param stepsPerRevolution       The number of steps that the motor makes for one revolution (360 degrees)
     * @return The number of steps that it takes to travel the given distance
     */
    public static int convertCentimetersToSteps(double centimeters, double wheelRadiusInCentimeters, final int stepsPerRevolution) {
        double pi = 3.141592;
        double wheelPerimeter = 2 * pi * wheelRadiusInCentimeters;

        double revolutionsToMake = centimeters / wheelPerimeter;

        return (int) (stepsPerRevolution * revolutionsToMake);
    }

    /**
     * Calculates the time in seconds that will take to rotate a step motor for a certain number of steps.
     *
     * @param steps Number of steps
     * @param speed Steps per second, rotational speed of the step motor.
     * @return The number of steps that it takes to travel the given distance
     */
    public static double convertStepsToSeconds(int steps, double speed) {
        return (double) steps / speed;
    }

    /**
     * Calculates the speed at which a motor must move to a certain number of steps in a for a given time.
     *
     * @param steps          Number of steps
     * @param timeToComplete The time for creating the given number of steps
     * @return Speed in steps per second
     */
    public static double convertStepsAndCompletionTimeToSpeed(int steps, double timeToComplete) {
        return (double) steps / timeToComplete;
    }

    /**
     * Converts degrees to number of steps (step motor), that it takes for a motor to move to the given number of degrees.
     *
     * @param degrees            degrees from current position
     * @param stepsPerRevolution The number of steps that the motor makes for one revolution (360 degrees)
     * @return The number of steps that it takes to move the given number of degrees
     */
    public static int convertDegreesToSteps(double degrees, final int stepsPerRevolution) {
        return (int) (stepsPerRevolution * degrees / 360);
    }
}
