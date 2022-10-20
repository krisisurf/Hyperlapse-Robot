package com.kristian.dimitrov.HyperlapseRobot.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class System {

    @GetMapping("/turnoff")
    public String turnoff(){
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                java.lang.System.exit(1);
            }
            java.lang.System.exit(1);
        });

        t.start();
        return "Turning off Hyperlapse Robot";
    }
}
