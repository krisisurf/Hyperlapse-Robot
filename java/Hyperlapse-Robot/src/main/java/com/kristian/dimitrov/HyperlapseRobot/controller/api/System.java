package com.kristian.dimitrov.HyperlapseRobot.controller.api;

import com.kristian.dimitrov.HyperlapseRobot.config.Config;
import com.kristian.dimitrov.HyperlapseRobot.entity.ArduinoRobot;
import com.kristian.dimitrov.HyperlapseRobot.entity.RulesManagerEntity;
import com.kristian.dimitrov.HyperlapseRobot.service.ArduinoService;
import com.kristian.dimitrov.HyperlapseRobot.utils.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class System {

    private final Config config;
    private final ArduinoService arduinoService;
    private static boolean hasTurnedOff = false;

    public System(Config config, ArduinoService arduinoService) {
        this.config = config;
        this.arduinoService = arduinoService;
    }

    /**
     * HTTP Get which test the connection.
     *
     * @return OK message
     */
    @GetMapping("/testConnection")
    public String testConnection() {
        return "OK";
    }

    /**
     * HTTP Get which returns the created ArduinoRobot instance
     *
     * @return ArduinoRobot instance
     */
    @GetMapping("/getArduinoRobot")
    public ArduinoRobot getArduinoRobot() {
        return config.getArduinoRobot();
    }

    /**
     * HTTP Get which returns the wheelRadius in centimeters
     *
     * @return wheelRadius in centimeters, (Config::ArduinoRobot::wheelRadius)
     * @see ArduinoRobot
     */
    @GetMapping("/getWheelRadius")
    public double getWheelRadius() {
        return config.getArduinoRobot().getWheelRadius();
    }

    /**
     * HTTP Get which returns the axleTrack in centimeters
     *
     * @return axleTrack in centimeters, (Config::ArduinoRobot::wheelRadius)
     * @see ArduinoRobot
     */
    @GetMapping("/getAxleTrack")
    public double getAxleTrack() {
        return config.getArduinoRobot().getAxleTrack();
    }

    /**
     * Exits the java application after 1 second, when this method is called.
     *
     * @return turn off message
     */
    @GetMapping("/turnoff")
    public String turnoff() {
        if (System.hasTurnedOff)
            return null;

        java.lang.System.out.println("\n\n\n");
        Logger.makeLog("TURNING OFF...", new Throwable());

        Thread t = new Thread(() -> {
            try {
                RulesManagerEntity emptyRules = config.getArduinoRobot().getRulesManagerEntity();
                emptyRules.clearRules();
                arduinoService.sendRules(emptyRules); // Sends empty rules to the Arduino to stop it
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                java.lang.System.exit(1);
            }

            System.hasTurnedOff = true;
            java.lang.System.exit(1);
        });

        t.start();
        return "Turning off Hyperlapse Robot";
    }

    public static boolean hasTurnedOff() {
        return hasTurnedOff;
    }
}
