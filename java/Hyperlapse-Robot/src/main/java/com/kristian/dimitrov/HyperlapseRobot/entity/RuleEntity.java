package com.kristian.dimitrov.HyperlapseRobot.entity;

public class RuleEntity {

    private MovementStepMotorEntity leftMotor;
    private MovementStepMotorEntity rightMotor;
    private CameraStepMotorEntity panMotor;
    private CameraStepMotorEntity tiltMotor;

    public RuleEntity(MovementStepMotorEntity leftMotor, MovementStepMotorEntity rightMotor, CameraStepMotorEntity panMotor, CameraStepMotorEntity tiltMotor) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.panMotor = panMotor;
        this.tiltMotor = tiltMotor;
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

    /**
     Shortened variant of toString()
     */
    public String toStringShort() {
        return "{" +
                "lm: " + leftMotor +
                ", rm: " + rightMotor +
                ", pm: " + panMotor +
                ", tm: " + tiltMotor +
                '}';
    }
}
