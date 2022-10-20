package com.kristian.dimitrov.HyperlapseRobot.config;

import com.kristian.dimitrov.HyperlapseRobot.hardware.ContinuousRotationServoComponent;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.kristian.dimitrov.HyperlapseRobot")
public class Config {

    private final Context context;

    private ContinuousRotationServoComponent continuousRotServo;

    public Config() {
        context = Pi4J.newAutoContext();
        continuousRotServo = new ContinuousRotationServoComponent(context, 5, "Servo MG996R", "Custom modified servo, model MG996R for 360 degrees continuous rotation.");
    }

    public Context getPi4j() {
        return context;
    }

    public ContinuousRotationServoComponent getContinuousRotServo() {
        return continuousRotServo;
    }
}