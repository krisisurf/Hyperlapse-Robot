package com.kristian.dimitrov.HyperlapseRobot;
import com.kristian.dimitrov.HyperlapseRobot.controller.api.System;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class HyperlapseRobotApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(HyperlapseRobotApplication.class, args);

        // Keyboard Interrupt shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System system = context.getBean("system", System.class);
            system.turnoff();
        }));
    }

}
