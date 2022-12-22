package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper.CameraStepMotorEntity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper.MovementStepMotorEntity;

import java.io.Serializable;

public class RuleEntity implements Serializable {

    private final MovementStepMotorEntity leftMotor;
    private final MovementStepMotorEntity rightMotor;
    private final CameraStepMotorEntity panMotor;
    private final CameraStepMotorEntity tiltMotor;

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
    public double getExecutionTime() {
        double maxExecutionTimeMovement = Math.max(leftMotor.getExecutionTime(), rightMotor.getExecutionTime());
        double maxExecutionTimeCamera = Math.max(panMotor.getExecutionTime(), tiltMotor.getExecutionTime());

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
