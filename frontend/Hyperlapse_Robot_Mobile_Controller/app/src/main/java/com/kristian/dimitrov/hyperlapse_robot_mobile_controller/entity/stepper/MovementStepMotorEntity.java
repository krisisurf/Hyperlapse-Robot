package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.ArduinoRobot;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.exception.IncompatibleStepMotorArguments;

import java.io.Serializable;

public class MovementStepMotorEntity extends StepMotorEntity implements Serializable {

    private float distance;
    private float executionTime;

    private final double wheelRadius;

    /**
     * <p>Default initialization of step motor entity with rule for distance and execution time.</p>
     * <b>NOTE:</b> THE HARDWARE STEP MOTOR STEPS COUNT ARE FIXED WITH DEFAULT VALUES
     */
    public MovementStepMotorEntity(double wheelRadius) {
        this.wheelRadius = wheelRadius;
    }

    /**
     * Initialization of step motor entity with rule for distance and execution time.
     *
     * @param stepsPerRevolution Hardware limit of the motor steps count for one revolution (360 degrees)
     * @param maxSpeed           Step motor max speed in steps per second
     * @param distance           Distance which will be moved
     * @param executionTime      Time for which the given distance will be traveled
     */
    public MovementStepMotorEntity(double wheelRadius, int stepsPerRevolution, float maxSpeed, float distance, float executionTime) throws IncompatibleStepMotorArguments {
        super(stepsPerRevolution, maxSpeed);
        this.wheelRadius = wheelRadius;
        setData(distance, executionTime);
    }

    /**
     * Sets the distance data and its travel completion time.
     *
     * @param distance      Distance to travel in centimeters
     * @param executionTime Time to complete in seconds
     * @throws IncompatibleStepMotorArguments When it is impossible to travel the given distance for the execution time, because of hardware limitations.
     */
    public void setData(float distance, float executionTime) throws IncompatibleStepMotorArguments {
        double minimalTimeRequired = getMinimalTimeRequired(distance);

        if (minimalTimeRequired > executionTime)
            throw new IncompatibleStepMotorArguments("The given 'executionTime=" + executionTime + "' is too short for reaching the target 'distance=" + distance + "'. Minimal time for this distance is: " + minimalTimeRequired + " seconds");

        this.distance = distance;
        this.executionTime = executionTime;
    }

    public double getMinimalTimeRequired(float distance) {
        int stepsRequired = ArduinoRobot.convertCentimetersToSteps(distance, wheelRadius, stepsPerRevolution);
        return ArduinoRobot.convertStepsToSeconds(stepsRequired, maxSpeed);
    }

    public float getDistance() {
        return distance;
    }

    public float getExecutionTime() {
        return executionTime;
    }

    @Override
    public String toString() {
        return "{" +
                "distance: " + distance +
                ", executionTime: " + executionTime +
                '}';
    }
}
