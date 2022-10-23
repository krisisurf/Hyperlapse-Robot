package com.kristian.dimitrov.HyperlapseRobot.controller.api;

import com.kristian.dimitrov.HyperlapseRobot.service.ContinuousRotServoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/continuousRotationServo")
public class ContinuousRotServoController {

    public ContinuousRotServoService continuousRotServoService;

    public ContinuousRotServoController(ContinuousRotServoService continuousRotServoService) {
        this.continuousRotServoService = continuousRotServoService;
    }

    @GetMapping("/stop")
    public String stop(){
        continuousRotServoService.stop();
        return createMessage("STOPPED");
    }

    @GetMapping("/clockwise")
    public String clockwise(@RequestParam(defaultValue = "50") int frequency, @RequestParam(defaultValue = "2") float dutyCycle){
        continuousRotServoService.rotateClockwise(frequency, dutyCycle);
        return createMessage("CLOCKWISE ROTATION is active");
    }


    @GetMapping("/counter-clockwise")
    public String counterclockwise(){
        continuousRotServoService.rotateCounterClockwise(50);
        return createMessage("COUNTER-CLOCKWISE ROTATION is active");
    }

    private String createMessage(String message){
        return "Continuous Rotation Servo: " + message;
    }
}
