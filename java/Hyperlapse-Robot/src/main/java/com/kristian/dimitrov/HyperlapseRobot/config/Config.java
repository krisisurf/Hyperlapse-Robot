package com.kristian.dimitrov.HyperlapseRobot.config;

import com.kristian.dimitrov.HyperlapseRobot.entity.ArduinoRobot;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.kristian.dimitrov.HyperlapseRobot")
public class Config {

    private final ArduinoRobot robotProperties;

    public Config() {
        robotProperties = new ArduinoRobot(5.5f, 64, 16, 64, 16);
    }
}