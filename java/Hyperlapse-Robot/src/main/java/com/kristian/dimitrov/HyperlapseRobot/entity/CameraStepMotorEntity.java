package com.kristian.dimitrov.HyperlapseRobot.entity;

public class CameraStepMotorEntity {

    private float degree;
    private float executionTime;

    public CameraStepMotorEntity() {
    }

    public CameraStepMotorEntity(float degree, float executionTime) {
        this.degree = degree;
        this.executionTime = executionTime;
    }

    public float getDegree() {
        return degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
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
                "degree: " + degree +
                ", executionTime: " + executionTime +
                '}';
    }

    /**
     Shortened variant of toString()
     */
    public String toStringShort() {
        return "{" +
                "d:" + degree +
                ",t:" + executionTime +
                '}';
    }
}
