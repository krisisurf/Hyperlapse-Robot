package com.kristian.dimitrov.HyperlapseRobot.entity;

import com.kristian.dimitrov.HyperlapseRobot.entity.stepper.CameraStepMotorEntity;
import com.kristian.dimitrov.HyperlapseRobot.entity.stepper.MovementStepMotorEntity;

public class RuleEntity {

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
