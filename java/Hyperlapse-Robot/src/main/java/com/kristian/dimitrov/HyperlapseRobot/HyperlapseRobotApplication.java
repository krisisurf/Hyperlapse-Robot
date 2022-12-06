package com.kristian.dimitrov.HyperlapseRobot;

import com.kristian.dimitrov.HyperlapseRobot.config.Config;
import com.kristian.dimitrov.HyperlapseRobot.controller.api.System;
import com.kristian.dimitrov.HyperlapseRobot.entity.ArduinoRobot;
import com.kristian.dimitrov.HyperlapseRobot.entity.stepper.StepMotorEntity;
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
            if(System.hasTurnedOff())
                return;

            System system = context.getBean("system", System.class);
            system.turnoff();
        }));

        // Read properties
        Config config = context.getBean("config", Config.class);
        ArduinoService arduinoService = context.getBean("arduinoServiceImpl", ArduinoServiceImpl.class);

        //TODO: Set arduino robot data by requesting it from the arduino
        //String result = arduinoService.requestData(ArduinoRequest.ROBOT_HARDWARE_PROPERTIES);

        final double wheelRadius = 5;
        final StepMotorEntity leftMotor = new StepMotorEntity(64, 16);
        final StepMotorEntity rightMotor = new StepMotorEntity(64, 16);
        final StepMotorEntity cameraPanMotor = new StepMotorEntity(64, 16);
        final StepMotorEntity cameraTiltMotor = new StepMotorEntity(64, 16);

        config.setArduinoRobot(new ArduinoRobot(wheelRadius, leftMotor, rightMotor, cameraPanMotor, cameraTiltMotor));
    }

}
