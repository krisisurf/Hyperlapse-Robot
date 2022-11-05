package com.kristian.dimitrov.HyperlapseRobot.config;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.kristian.dimitrov.HyperlapseRobot")
public class Config {

    private final Context context;

    public Config() {
        context = Pi4J.newAutoContext();
    }

    public Context getPi4j() {
        return context;
    }
}