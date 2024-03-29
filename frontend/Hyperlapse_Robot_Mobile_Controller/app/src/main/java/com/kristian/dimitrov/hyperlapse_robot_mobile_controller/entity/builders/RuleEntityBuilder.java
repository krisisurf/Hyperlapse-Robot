package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.builders;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.ArduinoRobot;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.RuleEntity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.TurnEntity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper.CameraStepMotorEntity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper.MovementStepMotorEntity;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.exception.IncompatibleStepMotorArguments;

import java.io.Serializable;

public class RuleEntityBuilder implements Serializable {

    private final ArduinoRobot arduinoRobot;

    private MovementStepMotorEntity leftMotor;
    private MovementStepMotorEntity rightMotor;
    private CameraStepMotorEntity panMotor;
    private CameraStepMotorEntity tiltMotor;

    public RuleEntityBuilder(ArduinoRobot arduinoRobot) {
        this.arduinoRobot = arduinoRobot;

        leftMotor = new MovementStepMotorEntity(arduinoRobot.getLeftMotor());
        rightMotor = new MovementStepMotorEntity(arduinoRobot.getRightMotor());
        panMotor = new CameraStepMotorEntity(arduinoRobot.getCameraPanMotor());
        tiltMotor = new CameraStepMotorEntity(arduinoRobot.getCameraTiltMotor());
    }

    public RuleEntityBuilder setLeftMotor(double distance, double executionTime) throws IncompatibleStepMotorArguments {
        leftMotor.setData(distance, executionTime);
        return this;
    }

    public RuleEntityBuilder setRightMotor(double distance, double executionTime) throws IncompatibleStepMotorArguments {
        rightMotor.setData(distance, executionTime);
        return this;
    }

    public RuleEntityBuilder setPanMotor(double degree, double executionTime) throws IncompatibleStepMotorArguments {
        panMotor.setData(degree, executionTime);
        return this;
    }

    public RuleEntityBuilder setTiltMotor(double degree, double executionTime) throws IncompatibleStepMotorArguments {
        tiltMotor.setData(degree, executionTime);
        return this;
    }

    public RuleEntity build() {
        return new RuleEntity(new TurnEntity(arduinoRobot), panMotor, tiltMotor);
    }

    public MovementStepMotorEntity getLeftMotor() {
        return leftMotor;
    }

    public MovementStepMotorEntity getRightMotor() {
        return rightMotor;
    }

    public CameraStepMotorEntity getPanMotor() {
        return panMotor;
    }

    public CameraStepMotorEntity getTiltMotor() {
        return tiltMotor;
    }
}
