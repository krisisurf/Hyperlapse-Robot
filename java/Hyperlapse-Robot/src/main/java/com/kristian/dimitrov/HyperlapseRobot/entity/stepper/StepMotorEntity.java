package com.kristian.dimitrov.HyperlapseRobot.entity.stepper;

import com.kristian.dimitrov.HyperlapseRobot.exception.IncompatibleStepMotorArguments;
import org.springframework.lang.NonNull;

import java.io.Serializable;

public abstract class StepMotorEntity implements Serializable {

    /**
     * Step motor default steps count for one revolution (360 degrees)
     */
    private static final int DEFAULT_STEPS_PER_REVOLUTION = 64;
    /**
     * Default step motor max speed in steps per second
     */
    private static final int DEFAULT_MAX_SPEED = 16;

    /**
     * Hardware limit of the motor steps count for one revolution (360 degrees)
     */
    protected final int stepsPerRevolution;
    /**
     * Max speed in steps per second
     */
    protected final double maxSpeed;

    /**
     * Initialization of step motor entity.
     *
     * @param stepsPerRevolution Hardware limit of the motor steps count for one revolution (360 degrees)
     * @param maxSpeed           Max speed in steps per second
     */
    public StepMotorEntity(int stepsPerRevolution, double maxSpeed) {
        this.stepsPerRevolution = stepsPerRevolution;
        this.maxSpeed = maxSpeed;
    }

    /**
     * Default constructor
     */
    public StepMotorEntity() {
        this(DEFAULT_STEPS_PER_REVOLUTION, DEFAULT_MAX_SPEED);
    }

    public abstract void setData(double val, double executionTime) throws IncompatibleStepMotorArguments, IncompatibleStepMotorArguments;

    public abstract double getMinimalTimeRequired(double val);

    public abstract double getMeasurementValue();

    public abstract double getExecutionTime();

    public boolean equalsByMeasurementValueAndExecutionTime(@NonNull StepMotorEntity stepMotorEntity) {
        return getMeasurementValue() == stepMotorEntity.getMeasurementValue() && getExecutionTime() == stepMotorEntity.getExecutionTime();
    }

    public static int getMinimalExecutionTimeCelled(StepMotorEntity stepMotorEntity, double val) {
        double minExecTime = stepMotorEntity.getMinimalTimeRequired(Math.abs(val));
        return (int) Math.ceil(minExecTime);
    }
}
