package com.kristian.dimitrov.HyperlapseRobot.entity;

import com.kristian.dimitrov.HyperlapseRobot.entity.stepper.StepMotorEntity;

/**
 * <h1>Predefined static class for a hardware properties of the robot.</h1>
 */
public final class ArduinoRobot {

    /**
     * Wheel radius in centimeters.
     */
    public final double wheelRadius;

    public final StepMotorEntity leftMotor;
    public final StepMotorEntity rightMotor;
    public final StepMotorEntity cameraPanMotor;
    public final StepMotorEntity cameraTiltMotor;

//    static {
//        wheelRadius = 5f;
//        leftMotor = new StepMotorEntity(64, 16);
//        rightMotor = new StepMotorEntity(64, 16);
//        cameraPanMotor = new StepMotorEntity(64, 16);
//        cameraTiltMotor = new StepMotorEntity(64, 16);
//    }

    public ArduinoRobot(double wheelRadius, StepMotorEntity leftMotor, StepMotorEntity rightMotor, StepMotorEntity cameraPanMotor, StepMotorEntity cameraTiltMotor) {
        this.wheelRadius = wheelRadius;
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.cameraPanMotor = cameraPanMotor;
        this.cameraTiltMotor = cameraTiltMotor;
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
