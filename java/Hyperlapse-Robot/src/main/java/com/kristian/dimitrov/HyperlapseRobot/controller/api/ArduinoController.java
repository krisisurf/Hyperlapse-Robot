package com.kristian.dimitrov.HyperlapseRobot.controller.api;

import com.kristian.dimitrov.HyperlapseRobot.config.Config;
import com.kristian.dimitrov.HyperlapseRobot.entity.ArduinoRobot;
import com.kristian.dimitrov.HyperlapseRobot.entity.RulesManagerEntity;
import com.kristian.dimitrov.HyperlapseRobot.entity.stepper.CameraStepMotorEntity;
import com.kristian.dimitrov.HyperlapseRobot.service.ArduinoService;
import com.kristian.dimitrov.HyperlapseRobot.utils.Logger;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/arduino")
public class ArduinoController {

    private final ArduinoService arduinoService;
    private final Config config;

    public ArduinoController(ArduinoService arduinoService, Config config) {
        this.arduinoService = arduinoService;
        this.config = config;
    }

    @PostMapping("/runRules")
    public String runRules(@RequestBody RulesManagerEntity rulesManagerEntity) {
        Logger.makeLog("Received rules via http: " + rulesManagerEntity.toString(), new Throwable());

        Logger.makeLog("\n\nOVERRIDEN Tilt motor degree: " + rulesManagerEntity.getRules().get(0).getTiltMotor().getMeasurementValue() + "\n\n", new Throwable());
        Logger.makeLog("\n\nOVERRIDEN Tilt motor executionTime: " + rulesManagerEntity.getRules().get(0).getTiltMotor().getExecutionTime() + "\n\n", new Throwable());

        arduinoService.sendRules(rulesManagerEntity);
        return "Rules sent to Arduino: " + rulesManagerEntity;
    }

    @GetMapping("/getRobotHardwareProperties")
    public ArduinoRobot getRobotHardwareProperties() {
        return config.getArduinoRobot();
    }
}
