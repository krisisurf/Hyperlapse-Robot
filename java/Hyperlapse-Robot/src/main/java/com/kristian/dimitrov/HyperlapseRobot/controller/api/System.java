package com.kristian.dimitrov.HyperlapseRobot.controller.api;

import com.kristian.dimitrov.HyperlapseRobot.entity.RulesManagerEntity;
import com.kristian.dimitrov.HyperlapseRobot.service.ArduinoService;
import com.kristian.dimitrov.HyperlapseRobot.utils.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class System {

    private ArduinoService arduinoService;

    public System(ArduinoService arduinoService) {
        this.arduinoService = arduinoService;
    }

    /**
     * HTTP Get which test the connection.
     * @return OK message
     */
    @GetMapping("/testConnection")
    public String testConnection() {
        return "OK";
    }

    /**
     * Exits the java application after 1 second, when this method is called.
     *
     * @return turn off message
     */
    @GetMapping("/turnoff")
    public String turnoff() {
        Logger.makeLog("TURNING OFF...", new Throwable());

        Thread t = new Thread(() -> {
            try {
                arduinoService.sendRules(new RulesManagerEntity()); // Sends empty rules to the Arduino to stop it
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
