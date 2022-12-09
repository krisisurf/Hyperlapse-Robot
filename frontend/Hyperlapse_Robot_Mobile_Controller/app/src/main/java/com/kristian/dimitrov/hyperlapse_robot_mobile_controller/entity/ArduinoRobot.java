package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper.StepMotorEntity;

import java.io.Serializable;

public class ArduinoRobot implements Serializable {

    /**
     * Connection
     */
    private boolean isConnectionEstablished;
    private String ipAddress;
    private String portNumber;

    /**
     * Wheel radius in centimeters.
     */
    private double wheelRadius;

    private StepMotorEntity leftMotor;
    private StepMotorEntity rightMotor;
    private StepMotorEntity cameraPanMotor;
    private StepMotorEntity cameraTiltMotor;

    private final RulesManagerEntity rulesManagerEntity;

    public ArduinoRobot() {
        rulesManagerEntity = new RulesManagerEntity();
    }

    public void setConnectionData(String ipAddress, String portNumber) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
    }

    public void setHardwareData(double wheelRadius, StepMotorEntity leftMotor, StepMotorEntity rightMotor, StepMotorEntity cameraPanMotor, StepMotorEntity cameraTiltMotor) {
        this.wheelRadius = wheelRadius;
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.cameraPanMotor = cameraPanMotor;
        this.cameraTiltMotor = cameraTiltMotor;
    }

    public RulesManagerEntity getRulesManagerEntity() {
        return rulesManagerEntity;
    }

    public boolean isConnectionEstablished() {
        return isConnectionEstablished;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public double getWheelRadius() {
        return wheelRadius;
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
