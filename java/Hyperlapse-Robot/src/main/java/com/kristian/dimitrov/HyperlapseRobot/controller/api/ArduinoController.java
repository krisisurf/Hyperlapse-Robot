package com.kristian.dimitrov.HyperlapseRobot.controller.api;

import com.kristian.dimitrov.HyperlapseRobot.entity.RulesManagerEntity;
import com.kristian.dimitrov.HyperlapseRobot.entity.builders.RuleEntityBuilder;
import com.kristian.dimitrov.HyperlapseRobot.exception.IncompatibleStepMotorArguments;
import com.kristian.dimitrov.HyperlapseRobot.service.ArduinoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/arduino")
public class ArduinoController {

    private ArduinoService arduinoService;

    public ArduinoController(ArduinoService arduinoService) {
        this.arduinoService = arduinoService;
    }

    @GetMapping("/sendPredefinedRules")
    public String sendString() {

        RulesManagerEntity rulesManagerEntity = new RulesManagerEntity();
        try {
            rulesManagerEntity.addRule(
                    new RuleEntityBuilder()
                            .setLeftMotor(100, 30)
                            .setRightMotor(100, 30)
                            .setPanMotor(360 * 5, 30)
                            .setTiltMotor(360 * 5, 30)
                            .build()
            );
            rulesManagerEntity.addRule(
                    new RuleEntityBuilder()
                            .setLeftMotor(10, 30)
                            .setRightMotor(10, 30)
                            .setPanMotor(360, 30)
                            .setTiltMotor(360, 30)
                            .build()
            );
            arduinoService.sendRules(rulesManagerEntity);
            return "Rules sent to Arduino: " + rulesManagerEntity;
        } catch (IncompatibleStepMotorArguments e) {
            e.printStackTrace();
            return "ERROR, could not send rules to arduino: " + e.getMessage();
        }
    }
}
