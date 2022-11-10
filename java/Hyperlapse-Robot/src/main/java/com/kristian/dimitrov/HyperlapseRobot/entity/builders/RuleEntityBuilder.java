package com.kristian.dimitrov.HyperlapseRobot.entity.builders;

import com.kristian.dimitrov.HyperlapseRobot.entity.CameraStepMotorEntity;
import com.kristian.dimitrov.HyperlapseRobot.entity.MovementStepMotorEntity;
import com.kristian.dimitrov.HyperlapseRobot.entity.RuleEntity;

public class RuleEntityBuilder {

    private MovementStepMotorEntity leftMotor;
    private MovementStepMotorEntity rightMotor;
    private CameraStepMotorEntity panMotor;
    private CameraStepMotorEntity tiltMotor;

    public RuleEntityBuilder() {
        leftMotor = new MovementStepMotorEntity();
        rightMotor = new MovementStepMotorEntity();
        panMotor = new CameraStepMotorEntity();
        tiltMotor = new CameraStepMotorEntity();
    }

    public RuleEntityBuilder setLeftMotor(float distance, float executionTime) {
        leftMotor.setDistance(distance);
        leftMotor.setExecutionTime(executionTime);
        return this;
    }

    public RuleEntityBuilder setRightMotor(float distance, float executionTime) {
        rightMotor.setDistance(distance);
        rightMotor.setExecutionTime(executionTime);
        return this;
    }

    public RuleEntityBuilder setPanMotor(float degree, float executionTime) {
        panMotor.setDegree(degree);
        panMotor.setExecutionTime(executionTime);
        return this;
    }

    public RuleEntityBuilder setTiltMotor(float degree, float executionTime) {
        tiltMotor.setDegree(degree);
        tiltMotor.setExecutionTime(executionTime);
        return this;
    }

    public RuleEntity build() {
        return new RuleEntity(leftMotor, rightMotor, panMotor, tiltMotor);
    }
}
