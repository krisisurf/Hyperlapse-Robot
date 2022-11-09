package com.kristian.dimitrov.HyperlapseRobot.entity;

public class MovementStepMotorEntity {
    private float distance;
    private float executionTime;

    public MovementStepMotorEntity(float distance, float executionTime) {
        this.distance = distance;
        this.executionTime = executionTime;
    }
}
