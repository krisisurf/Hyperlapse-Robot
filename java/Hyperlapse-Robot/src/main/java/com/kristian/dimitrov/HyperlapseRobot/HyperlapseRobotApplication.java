package com.kristian.dimitrov.HyperlapseRobot;

import com.kristian.dimitrov.HyperlapseRobot.entity.CameraStepMotorEntity;
import com.kristian.dimitrov.HyperlapseRobot.entity.MovementStepMotorEntity;
import com.kristian.dimitrov.HyperlapseRobot.entity.RuleEntity;
import com.kristian.dimitrov.HyperlapseRobot.entity.RulesManagerEntity;
import com.kristian.dimitrov.HyperlapseRobot.entity.builders.RuleEntityBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HyperlapseRobotApplication {

	public static void main(String[] args) {
		//SpringApplication.run(HyperlapseRobotApplication.class, args);

		RulesManagerEntity rulesManagerEntity = new RulesManagerEntity();
		rulesManagerEntity.addRule(
				new RuleEntityBuilder()
						.setLeftMotor(100, 180)
						.setRightMotor(100, 180)
						.setPanMotor(0, 0)
						.setTiltMotor(20, 0)
						.build()
		);
		rulesManagerEntity.addRule(
				new RuleEntityBuilder()
						.setLeftMotor(50, 180)
						.setRightMotor(50, 180)
						.setPanMotor(0, 0)
						.setTiltMotor(40, 0)
						.build()
		);

		System.out.println(rulesManagerEntity.toStringShort());
	}

}
