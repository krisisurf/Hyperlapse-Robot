package com.kristian.dimitrov.hyperlapse_robot_mobile_controller.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RulesManagerEntity implements Serializable {

    private final ArduinoRobot arduinoRobot;
    private final List<RuleEntity> rules;

    public RulesManagerEntity(ArduinoRobot arduinoRobot) {
        this.arduinoRobot = arduinoRobot;
        this.rules = new ArrayList<>();
    }

    public void addRule(RuleEntity ruleEntity) {
        rules.add(ruleEntity);
    }

    public void removeRule(RuleEntity ruleEntity) {
        rules.remove(ruleEntity);
    }

    public RuleEntity get(int index) throws IndexOutOfBoundsException {
        return rules.get(index);
    }

    public int size() {
        return rules.size();
    }

    public void clearRules() {
        rules.clear();
    }

    public List<RuleEntity> getRules() {
        return rules;
    }

    @Override
    public String toString() {
        return "{" +
                "rulesCount: " + rules.size() +
                ", wheelRadius:" + arduinoRobot.getWheelRadius() +
                ", rules: " + rules +
                '}';
    }
}
