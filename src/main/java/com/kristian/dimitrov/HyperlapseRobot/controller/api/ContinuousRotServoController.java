package com.kristian.dimitrov.HyperlapseRobot.controller.api;

import com.kristian.dimitrov.HyperlapseRobot.service.ContinuousRotServoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        return returnMessage("STOPPED");
    }

    @GetMapping("/clockwise")
    public String clockwise(){
        continuousRotServoService.rotateClockwise();
        return returnMessage("CLOCKWISE ROTATION is active");
    }


    @GetMapping("/counter-clockwise")
    public String counterclockwise(){
        continuousRotServoService.rotateCounterClockwise();
        return returnMessage("COUNTER-CLOCKWISE ROTATION is active");
    }

    private String returnMessage(String message){
        return "Continuous Rotation Servo: " + message;
    }
}
