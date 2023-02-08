package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.ArduinoRobot;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.exception.IncompatibleStepMotorArguments;

import java.io.Serializable;

public class MovementStepMotorEntity extends StepMotorEntity implements Serializable {

    private double distance;
    private double executionTime;

    private double wheelRadius;

    public void setWheelRadius(double wheelRadius) {
        this.wheelRadius = wheelRadius;
    }

    /**
     * <p>Default initialization of step motor entity with rule for distance and execution time.</p>
     */
    public MovementStepMotorEntity(double wheelRadius, int stepsPerRevolution, double maxSpeed) {
        super(stepsPerRevolution, maxSpeed);
        this.wheelRadius = wheelRadius;
    }

    /**
     * Clone constructor
     *
     * @param movementStepMotorEntity
     */
    public MovementStepMotorEntity(MovementStepMotorEntity movementStepMotorEntity) {
        this(movementStepMotorEntity.wheelRadius, movementStepMotorEntity.stepsPerRevolution, movementStepMotorEntity.maxSpeed);
    }

    /**
     * Sets the distance data and its travel completion time.
     *
     * @param distance      Distance to travel in centimeters
     * @param executionTime Time to complete in seconds
     * @throws IncompatibleStepMotorArguments When it is impossible to travel the given distance for the execution time, because of hardware limitations.
     */
    @Override
    public void setData(double distance, double executionTime) throws IncompatibleStepMotorArguments {
        double minimalTimeRequired = getMinimalTimeRequired(distance);

        if (minimalTimeRequired > executionTime)
            throw new IncompatibleStepMotorArguments("The given 'executionTime=" + executionTime + "' is too short for reaching the target 'distance=" + distance + "'. Minimal time for this distance is: " + minimalTimeRequired + " seconds");

        this.distance = distance;
        this.executionTime = executionTime;
    }

    @Override
    public double getMinimalTimeRequired(double distance) {
        int stepsRequired = ArduinoRobot.convertCentimetersToSteps(distance, wheelRadius, stepsPerRevolution);
        return ArduinoRobot.convertStepsToSeconds(stepsRequired, maxSpeed);
    }

    /**
     *
     * @return distance
     */
    @Override
    public double getMeasurementValue() {
        return distance;
    }

    @Override
    public double getExecutionTime() {
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
