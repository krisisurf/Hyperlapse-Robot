package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper.CameraStepMotorEntity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper.MovementStepMotorEntity;

import java.io.Serializable;

public class RuleEntity implements Serializable{

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
