package com.kristian.dimitrov.HyperlapseRobot.entity;

import com.kristian.dimitrov.HyperlapseRobot.entity.stepper.CameraStepMotorEntity;
import com.kristian.dimitrov.HyperlapseRobot.entity.stepper.MovementStepMotorEntity;

import java.io.Serializable;

public class RuleEntity implements Serializable {

    private MovementStepMotorEntity leftMotor;
    private MovementStepMotorEntity rightMotor;
    private CameraStepMotorEntity panMotor;
    private CameraStepMotorEntity tiltMotor;

    public RuleEntity(MovementStepMotorEntity leftMotor, MovementStepMotorEntity rightMotor, CameraStepMotorEntity panMotor, CameraStepMotorEntity tiltMotor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.panMotor = panMotor;
        this.tiltMotor = tiltMotor;
    }

    /**
     * Finds how much time will the rule take.
     *
     * @return max execution time in seconds
     */
    public float getExecutionTime() {
        float maxExecutionTimeMovement = Math.max(leftMotor.getExecutionTime(), rightMotor.getExecutionTime());
        float maxExecutionTimeCamera = Math.max(panMotor.getExecutionTime(), tiltMotor.getExecutionTime());

        return Math.max(maxExecutionTimeMovement, maxExecutionTimeCamera);
    }

    @Override
    public String toString() {
        return "{" +
                "leftMotor: " + leftMotor +
                ", rightMotor: " + rightMotor +
                ", panMotor: " + panMotor +
                ", tiltMotor: " + tiltMotor +
                '}';
    }
}

