package com.kristian.dimitrov.HyperlapseRobot.entity;

import java.util.ArrayList;
import java.util.List;

public class RulesManagerEntity {

    private List<RuleEntity> rules;

    public RulesManagerEntity() {
        this.rules = new ArrayList<RuleEntity>();
    }

    public void addRule(RuleEntity ruleEntity){
        rules.add(ruleEntity);
    }

    public void removeRule(RuleEntity ruleEntity){
        rules.remove(ruleEntity);
    }

    public void clearRules(){
        rules.clear();
    }

    @Override
    public String toString() {
        return "{" +
                "rulesCount: " + rules.size() +
                ", rules: " + rules +
                '}';
    }

    /**
        Shortened variant of toString()
     */
    public String toStringShort() {
        return "{" +
                "rc:" + rules.size() +
                ",r:" + rules +
                '}';
    }
}
