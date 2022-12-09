package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity.stepper;

import com.kristian.dimitrov.HyperlapseRobot.entity.ArduinoRobot;
import com.kristian.dimitrov.HyperlapseRobot.exception.IncompatibleStepMotorArguments;

public class CameraStepMotorEntity extends StepMotorEntity {

    private float degree;
    private float executionTime;

    public CameraStepMotorEntity() {
    }

    public CameraStepMotorEntity(float degree, float executionTime) throws IncompatibleStepMotorArguments {
        setData(degree, executionTime);
    }

    /**
     * Sets the rotation data for a rule.
     *
     * @param degree        How many degrees to move (newDegrees = currentDegree + degree)
     * @param executionTime Time to complete the rotation in seconds.
     * @throws IncompatibleStepMotorArguments
     * @see com.kristian.dimitrov.HyperlapseRobot.entity.RuleEntity
     */
    public void setData(float degree, float executionTime) throws IncompatibleStepMotorArguments {
        int stepsRequired = ArduinoRobot.convertDegreesToSteps(degree, stepsPerRevolution);
        double minimalTimeRequired = ArduinoRobot.convertStepsToSeconds(stepsRequired, maxSpeed);
        if (minimalTimeRequired > executionTime)
            throw new IncompatibleStepMotorArguments("The given 'executionTime=" + executionTime + "' is too short for reaching the target 'degree=" + degree + "'. Minimal time for this rotation is: " + minimalTimeRequired + " seconds");

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
