package com.kristian.dimitrov.HyperlapseRobot;

import com.kristian.dimitrov.HyperlapseRobot.config.Config;
import com.kristian.dimitrov.HyperlapseRobot.controller.api.System;
import com.kristian.dimitrov.HyperlapseRobot.service.ArduinoRequest;
import com.kristian.dimitrov.HyperlapseRobot.service.ArduinoService;
import com.kristian.dimitrov.HyperlapseRobot.service.impl.ArduinoServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class HyperlapseRobotApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(HyperlapseRobotApplication.class, args);

        // Keyboard Interrupt shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System system = context.getBean("system", System.class);
            system.turnoff();
        }));

        // Read properties
        Config config = context.getBean("config", Config.class);
        ArduinoService arduinoService = context.getBean("arduinoServiceImpl", ArduinoServiceImpl.class);

        String result = arduinoService.requestData(ArduinoRequest.ROBOT_HARDWARE_PROPERTIES);
        //TODO: Finish the code here
        //config.setArduinoRobot(new ArduinoRobot());
    }

}
