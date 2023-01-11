package com.kristian.dimitrov.HyperlapseRobot;

import com.kristian.dimitrov.HyperlapseRobot.config.Config;
import com.kristian.dimitrov.HyperlapseRobot.controller.api.System;
import com.kristian.dimitrov.HyperlapseRobot.entity.ArduinoRobot;
import com.kristian.dimitrov.HyperlapseRobot.entity.stepper.StepMotorEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class HyperlapseRobotApplication {

    /**
     * Arguments allow startup with custom configuration.
     *
     * @param args index 0 is the radius of the wheel in cm. By default, the wheel radius is 5cm.
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(HyperlapseRobotApplication.class, args);

        // Keyboard Interrupt shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (System.hasTurnedOff())
                return;

            System system = context.getBean("system", System.class);
            system.turnoff();
        }));

        Config config = context.getBean("config", Config.class);
        //TODO: Set arduino robot data by requesting it from the arduino
//        ArduinoService arduinoService = context.getBean("arduinoServiceImpl", ArduinoServiceImpl.class);
//        String result = arduinoService.requestData(ArduinoRequest.ROBOT_HARDWARE_PROPERTIES);


        // if arguments are not presented, the wheelRadius is set to 5 cm.
        final double wheelRadius = args.length == 0 ? 5 : Double.parseDouble(args[0]);

        final StepMotorEntity leftMotor = new StepMotorEntity(64, 16);
        final StepMotorEntity rightMotor = new StepMotorEntity(64, 16);
        final StepMotorEntity cameraPanMotor = new StepMotorEntity(64, 16);
        final StepMotorEntity cameraTiltMotor = new StepMotorEntity(64, 16);

        ArduinoRobot arduinoRobot = new ArduinoRobot();
        arduinoRobot.setHardwareData(wheelRadius, leftMotor, rightMotor, cameraPanMotor, cameraTiltMotor);
        config.setArduinoRobot(arduinoRobot);
    }

}
