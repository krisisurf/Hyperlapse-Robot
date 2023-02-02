package com.kristian.dimitrov.HyperlapseRobot.entity.stepper;

import com.kristian.dimitrov.HyperlapseRobot.entity.ArduinoRobot;
import com.kristian.dimitrov.HyperlapseRobot.exception.IncompatibleStepMotorArguments;

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
     */
    @Override
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

    @Override
    public double getExecutionTime() {
        return executionTime;
    }

    /**
     * @return the target degree of this motor
     */
    @Override
    public double getMeasurementValue() {
        return degree;
    }

    /**
     * Does the same as getMeasurementValue().
     * The reason why this method exist is that the name of the method must follow the name convention of java spring.
     * Otherwise, there is a problem with receiving entity via http and parsing the values.
     * @return the target degree of this motor
     */
    public double getDegree(){
        return getMeasurementValue();
    }

    @Override
    public String toString() {
        return "{" + "degree: " + degree + ", executionTime: " + executionTime + '}';
    }
}
