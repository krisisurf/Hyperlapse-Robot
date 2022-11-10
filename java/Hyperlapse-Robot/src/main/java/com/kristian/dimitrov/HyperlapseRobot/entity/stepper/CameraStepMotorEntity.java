package com.kristian.dimitrov.HyperlapseRobot.entity.stepper;

import com.kristian.dimitrov.HyperlapseRobot.config.ArduinoRobot;
import com.kristian.dimitrov.HyperlapseRobot.exception.IncompatibleStepMotorArguments;

public class CameraStepMotorEntity extends StepMotorEntity{

    private float degree;
    private float executionTime;

    public CameraStepMotorEntity() {
    }

    public CameraStepMotorEntity(float degree, float executionTime) throws IncompatibleStepMotorArguments {
        setData(degree, executionTime);
    }
    public void setData(float degree, float executionTime) throws IncompatibleStepMotorArguments{
        // TODO: Add a check to see if rotation is possible for the given executionTime. If not, than throw IncompatibleStepMotorArguments exception
        //        Edit the code below:
        //        int stepsRequired = 0;
        //        double minimalTimeRequired = 0;
        //        if (minimalTimeRequired > executionTime)
        //            throw new IncompatibleStepMotorArguments("The given 'executionTime=" + executionTime + "' is too short for reaching the target 'degree=" + degree + "'. Minimal time for this rotation is: " + minimalTimeRequired + " seconds");

        this.degree = degree;
        this.executionTime = executionTime;
    }

    public float getExecutionTime() {
        return executionTime;
    }

    public float getDegree() {
        return degree;
    }


    @Override
    public String toString() {
        return "{" +
                "degree: " + degree +
                ", executionTime: " + executionTime +
                '}';
    }
}
