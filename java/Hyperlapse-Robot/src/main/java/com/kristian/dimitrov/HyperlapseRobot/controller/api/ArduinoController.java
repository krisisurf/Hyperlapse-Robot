package com.kristian.dimitrov.HyperlapseRobot.controller.api;

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

    @GetMapping("/sendString")
    public String sendChar(@RequestParam(defaultValue = "This string comes from Raspberry Pi") String str){
        arduinoService.sendJsonString(str);
        return "String sent to Arduino: " + str;
    }
}
