package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper;

import java.io.Serializable;

public class StepMotorEntity implements Serializable {

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
    protected final float maxSpeed;

    /**
     * Initialization of step motor entity.
     *
     * @param stepsPerRevolution Hardware limit of the motor steps count for one revolution (360 degrees)
     * @param maxSpeed           Max speed in steps per second
     */
    public StepMotorEntity(int stepsPerRevolution, float maxSpeed) {
        this.stepsPerRevolution = stepsPerRevolution;
        this.maxSpeed = maxSpeed;
    }

    /**
     * Default constructor
     */
    public StepMotorEntity() {
        this(DEFAULT_STEPS_PER_REVOLUTION, DEFAULT_MAX_SPEED);
    }
}
