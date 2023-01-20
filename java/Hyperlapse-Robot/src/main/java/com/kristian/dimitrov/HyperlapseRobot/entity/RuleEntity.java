package com.kristian.dimitrov.HyperlapseRobot.entity;

import com.kristian.dimitrov.HyperlapseRobot.entity.stepper.CameraStepMotorEntity;
import com.kristian.dimitrov.HyperlapseRobot.entity.stepper.MovementStepMotorEntity;

import java.io.Serializable;

public class RuleEntity implements Serializable, Cloneable {

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

    public String superToString() {
        return super.toString();
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

    @Override
    public RuleEntity clone() {
        try {
            RuleEntity clone = (RuleEntity) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
