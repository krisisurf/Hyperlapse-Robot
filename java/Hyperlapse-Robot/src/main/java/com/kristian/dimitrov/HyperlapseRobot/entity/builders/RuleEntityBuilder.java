package com.kristian.dimitrov.HyperlapseRobot.entity.builders;

import com.kristian.dimitrov.HyperlapseRobot.entity.ArduinoRobot;
import com.kristian.dimitrov.HyperlapseRobot.entity.stepper.CameraStepMotorEntity;
import com.kristian.dimitrov.HyperlapseRobot.entity.stepper.MovementStepMotorEntity;
import com.kristian.dimitrov.HyperlapseRobot.entity.RuleEntity;
import com.kristian.dimitrov.HyperlapseRobot.exception.IncompatibleStepMotorArguments;

public class RuleEntityBuilder {

    private final ArduinoRobot arduinoRobot;

    private MovementStepMotorEntity leftMotor;
    private MovementStepMotorEntity rightMotor;
    private CameraStepMotorEntity panMotor;
    private CameraStepMotorEntity tiltMotor;

    public RuleEntityBuilder(ArduinoRobot arduinoRobot) {
        this.arduinoRobot = arduinoRobot;

        leftMotor = new MovementStepMotorEntity(arduinoRobot.wheelRadius);
        rightMotor = new MovementStepMotorEntity(arduinoRobot.wheelRadius);
        panMotor = new CameraStepMotorEntity();
        tiltMotor = new CameraStepMotorEntity();
    }

    public RuleEntityBuilder setLeftMotor(float distance, float executionTime) throws IncompatibleStepMotorArguments {
        leftMotor.setData(distance, executionTime);
        return this;
    }

    public RuleEntityBuilder setRightMotor(float distance, float executionTime) throws IncompatibleStepMotorArguments {
        rightMotor.setData(distance, executionTime);
        return this;
    }

    public RuleEntityBuilder setPanMotor(float degree, float executionTime) throws IncompatibleStepMotorArguments {
        panMotor.setData(degree, executionTime);
        return this;
    }

    public RuleEntityBuilder setTiltMotor(float degree, float executionTime) throws IncompatibleStepMotorArguments {
        tiltMotor.setData(degree, executionTime);
        return this;
    }

    public RuleEntity build() {
        return new RuleEntity(leftMotor, rightMotor, panMotor, tiltMotor);
    }
}