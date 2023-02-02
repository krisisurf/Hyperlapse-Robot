package com.kristian.dimitrov.HyperlapseRobot;

import com.kristian.dimitrov.HyperlapseRobot.config.Config;
import com.kristian.dimitrov.HyperlapseRobot.controller.api.System;
import com.kristian.dimitrov.HyperlapseRobot.entity.ArduinoRobot;
import com.kristian.dimitrov.HyperlapseRobot.entity.stepper.CameraStepMotorEntity;
import com.kristian.dimitrov.HyperlapseRobot.entity.stepper.MovementStepMotorEntity;
import com.kristian.dimitrov.HyperlapseRobot.utils.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class HyperlapseRobotApplication {

    /**
     * Arguments allow startup with custom configuration.
     *
     * @param args index 0 is the radius of the wheel in cm.
     * @param args index 1 is the axle track width in cm.
     */
    public static void main(String[] args) {
        String errorMessage = "The program requires 2 startup arguments:\narg 0: wheelRadius (double) in centimeters\narg 1: axleTrack (double) in centimeters. Axle Track is the distance between the two movement wheels axles\nEXITED";
        if (args.length != 2) {
            Logger.makeLog(errorMessage, new Throwable());
            return;
        }

        double wheelRadius, axleTrack;
        try {
            wheelRadius = Double.parseDouble(args[0]);
            axleTrack = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            Logger.makeLog(errorMessage, new Throwable());
            return;
        }

        ConfigurableApplicationContext context = SpringApplication.run(HyperlapseRobotApplication.class, args);

        // Keyboard Interrupt shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (System.hasTurnedOff())
                return;

            System system = context.getBean("system", System.class);
            system.turnoff();
        }));


        final MovementStepMotorEntity leftMotor = new MovementStepMotorEntity(5, 64, 16);
        final MovementStepMotorEntity rightMotor = new MovementStepMotorEntity(5, 64, 16);
        final CameraStepMotorEntity cameraPanMotor = new CameraStepMotorEntity(64, 16);
        final CameraStepMotorEntity cameraTiltMotor = new CameraStepMotorEntity(64, 16);

        ArduinoRobot arduinoRobot = new ArduinoRobot();
        arduinoRobot.setHardwareData(wheelRadius, axleTrack, leftMotor, rightMotor, cameraPanMotor, cameraTiltMotor);

        Config config = context.getBean("config", Config.class);
        config.setArduinoRobot(arduinoRobot);
    }

}
