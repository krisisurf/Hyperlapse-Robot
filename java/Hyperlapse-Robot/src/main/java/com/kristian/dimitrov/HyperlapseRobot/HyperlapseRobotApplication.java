package com.kristian.dimitrov.HyperlapseRobot;

import com.kristian.dimitrov.HyperlapseRobot.entity.RulesManagerEntity;
import com.kristian.dimitrov.HyperlapseRobot.entity.builders.RuleEntityBuilder;
import com.kristian.dimitrov.HyperlapseRobot.exception.IncompatibleStepMotorArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HyperlapseRobotApplication {

    public static void main(String[] args) {
        SpringApplication.run(HyperlapseRobotApplication.class, args);
    }

}
