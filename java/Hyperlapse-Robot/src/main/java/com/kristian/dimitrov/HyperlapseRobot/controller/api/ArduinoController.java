package com.kristian.dimitrov.HyperlapseRobot.controller.api;

import com.kristian.dimitrov.HyperlapseRobot.config.Config;
import com.kristian.dimitrov.HyperlapseRobot.entity.ArduinoRobot;
import com.kristian.dimitrov.HyperlapseRobot.entity.RulesManagerEntity;
import com.kristian.dimitrov.HyperlapseRobot.entity.builders.RuleEntityBuilder;
import com.kristian.dimitrov.HyperlapseRobot.exception.IncompatibleStepMotorArguments;
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
        Logger.makeLog("Received rules via http: " + rulesManagerEntity.getShortenedJson(), new Throwable());
        arduinoService.sendRules(rulesManagerEntity);
        return "Rules sent to Arduino: " + rulesManagerEntity;
    }

    @GetMapping("/getRobotHardwareProperties")
    public ArduinoRobot getRobotHardwareProperties() {
        return config.getArduinoRobot();
    }
}
