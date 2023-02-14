package com.kristian.dimitrov.HyperlapseRobot.entity;

import com.kristian.dimitrov.HyperlapseRobot.entity.stepper.MovementStepMotorEntity;
import com.kristian.dimitrov.HyperlapseRobot.entity.stepper.StepMotorEntity;
import com.kristian.dimitrov.HyperlapseRobot.exception.IncompatibleStepMotorArguments;

import java.io.Serializable;

public class TurnEntity implements Serializable {
    private int turnAngle;
    private int turnRadius;
    private int executionTime;

    private final ArduinoRobot arduinoRobot;

    private RuleEntity ruleEntity;

    public TurnEntity(ArduinoRobot arduinoRobot) {
        this.arduinoRobot = arduinoRobot;
    }

    public void setRuleEntity(RuleEntity ruleEntity) {
        this.ruleEntity = ruleEntity;
    }

    public void setTurnAngle(int turnAngle) {
        this.turnAngle = turnAngle;
        double leftMotorDistance = calculateWheelTravelDistance(turnAngle, turnRadius, arduinoRobot.getAxleTrack(), true);
        double rightMotorDistance = calculateWheelTravelDistance(turnAngle, turnRadius, arduinoRobot.getAxleTrack(), false);

        try {
            int selectedExecutionTime;
            if (Math.abs(leftMotorDistance) > Math.abs(rightMotorDistance)) {
                StepMotorEntity shorterPathMotor = ruleEntity.getRightMotor();
                StepMotorEntity longerPathMotor = ruleEntity.getLeftMotor();
                selectedExecutionTime = Math.max(StepMotorEntity.getMinimalExecutionTimeCelled(longerPathMotor, leftMotorDistance), (int) longerPathMotor.getExecutionTime());

                shorterPathMotor.setData(rightMotorDistance, selectedExecutionTime);
                longerPathMotor.setData(leftMotorDistance, selectedExecutionTime);
            } else {
                StepMotorEntity shorterPathMotor = ruleEntity.getLeftMotor();
                StepMotorEntity longerPathMotor = ruleEntity.getRightMotor();
                selectedExecutionTime = Math.max(StepMotorEntity.getMinimalExecutionTimeCelled(longerPathMotor, leftMotorDistance), (int) longerPathMotor.getExecutionTime());

                shorterPathMotor.setData(leftMotorDistance, selectedExecutionTime);
                longerPathMotor.setData(rightMotorDistance, selectedExecutionTime);
            }
            this.executionTime = selectedExecutionTime;
        } catch (IncompatibleStepMotorArguments e) {
            e.printStackTrace();
        }
    }

    private double calculateWheelTravelDistance(double turnAngle, double turnRadius,
                                                double axleTrack, boolean isLeftSide) {
        turnAngle = (isLeftSide) ? turnAngle : -turnAngle;
        double motorPathRadius = turnRadius + (Math.signum(turnAngle) * axleTrack / 2);

        return Math.toRadians(Math.abs(turnAngle)) * motorPathRadius;
    }

    public void setTurnRadius(int turnRadius) {
        this.turnRadius = turnRadius;
        double leftMotorDistance = calculateWheelTravelDistance(turnAngle, turnRadius, arduinoRobot.getAxleTrack(), true);
        double rightMotorDistance = calculateWheelTravelDistance(turnAngle, turnRadius, arduinoRobot.getAxleTrack(), false);

        try {
            int selectedExecutionTime;
            if (Math.abs(leftMotorDistance) > Math.abs(rightMotorDistance)) {
                StepMotorEntity shorterPathMotor = ruleEntity.getRightMotor();
                StepMotorEntity longerPathMotor = ruleEntity.getLeftMotor();
                selectedExecutionTime = Math.max(StepMotorEntity.getMinimalExecutionTimeCelled(longerPathMotor, leftMotorDistance), (int) longerPathMotor.getExecutionTime());

                shorterPathMotor.setData(rightMotorDistance, selectedExecutionTime);
                longerPathMotor.setData(leftMotorDistance, selectedExecutionTime);
            } else {
                StepMotorEntity shorterPathMotor = ruleEntity.getLeftMotor();
                StepMotorEntity longerPathMotor = ruleEntity.getRightMotor();
                selectedExecutionTime = Math.max(StepMotorEntity.getMinimalExecutionTimeCelled(longerPathMotor, leftMotorDistance), (int) longerPathMotor.getExecutionTime());

                shorterPathMotor.setData(leftMotorDistance, selectedExecutionTime);
                longerPathMotor.setData(rightMotorDistance, selectedExecutionTime);
            }
            this.executionTime = selectedExecutionTime;
        } catch (IncompatibleStepMotorArguments e) {
            e.printStackTrace();
        }
    }

    public void setExecutionTime(int executionTime) throws IncompatibleStepMotorArguments {
        this.executionTime = executionTime;
        ruleEntity.getLeftMotor().setData(ruleEntity.getLeftMotor().getMeasurementValue(), executionTime);
        ruleEntity.getRightMotor().setData(ruleEntity.getRightMotor().getMeasurementValue(), executionTime);
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public MovementStepMotorEntity getLeftMotor() {
        return arduinoRobot.getLeftMotor();
    }

    public MovementStepMotorEntity getRightMotor() {
        return arduinoRobot.getRightMotor();
    }

    public int getTurnAngle() {
        return turnAngle;
    }

    public int getTurnRadius() {
        return turnRadius;
    }
}
