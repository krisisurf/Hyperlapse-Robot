package com.kristian.dimitrov.HyperlapseRobot.entity;

public class MovementStepMotorEntity {
    private float distance;
    private float executionTime;

    public MovementStepMotorEntity() {
    }

    public MovementStepMotorEntity(float distance, float executionTime) {
        this.distance = distance;
        this.executionTime = executionTime;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(float executionTime) {
        this.executionTime = executionTime;
    }

    @Override
    public String toString() {
        return "{" +
                "distance: " + distance +
                ", executionTime: " + executionTime +
                '}';
    }

    /**
     Shortened variant of toString()
     */
    public String toStringShort(){
        return "{" +
            "d:" + distance +
            ",t:" + executionTime +
            '}';
    }
}
