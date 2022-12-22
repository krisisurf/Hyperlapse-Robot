package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper;

import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.ArduinoRobot;
import com.kristian.dimitrov.hyperlapse_robot_mobile_controller.exception.IncompatibleStepMotorArguments;

import java.io.Serializable;

public class CameraStepMotorEntity extends StepMotorEntity implements Serializable {

    private double degree;
    private double executionTime;

    public CameraStepMotorEntity(int stepsPerRevolution, double maxSpeed) {
        super(stepsPerRevolution, maxSpeed);
    }

    /**
     * Clone constructor
     *
     * @param cameraStepMotorEntity
     */
    public CameraStepMotorEntity(CameraStepMotorEntity cameraStepMotorEntity) {
        super(cameraStepMotorEntity.stepsPerRevolution, cameraStepMotorEntity.maxSpeed);
    }

    /**
     * Sets the rotation data for a rule.
     *
     * @param degree        How many degrees to move (newDegrees = currentDegree + degree)
     * @param executionTime Time to complete the rotation in seconds.
     * @throws IncompatibleStepMotorArguments
     */
    public void setData(double degree, double executionTime) throws IncompatibleStepMotorArguments {
        double minimalTimeRequired = getMinimalTimeRequired(degree);
        if (minimalTimeRequired > executionTime)
            throw new IncompatibleStepMotorArguments("The given 'executionTime=" + executionTime + "' is too short for reaching the target 'degree=" + degree + "'. Minimal time for this rotation is: " + minimalTimeRequired + " seconds");

        this.degree = degree;
        this.executionTime = executionTime;
    }

    @Override
    public double getMinimalTimeRequired(double degree) {
        int stepsRequired = ArduinoRobot.convertDegreesToSteps(degree, stepsPerRevolution);
        return ArduinoRobot.convertStepsToSeconds(stepsRequired, maxSpeed);
    }

    public double getExecutionTime() {
        return executionTime;
    }

    public double getDegree() {
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
