package com.kristian.dimitrov.HyperlapseRobot.entity;

import org.apache.tomcat.util.buf.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RulesManagerEntity {

    private List<RuleEntity> rules;

    public RulesManagerEntity() {
        this.rules = new ArrayList<RuleEntity>();
    }

    public void addRule(RuleEntity ruleEntity) {
        rules.add(ruleEntity);
    }

    public void removeRule(RuleEntity ruleEntity) {
        rules.remove(ruleEntity);
    }

    public void clearRules() {
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
     * Abbreviates field names from RulesManagerEntity::toString() and removes space gaps to save memory.
     * <pre>
     * +----------------+-------------+
     * | Original       | Shortened   |
     * +----------------+-------------+
     * | rulesCount     | rc          |
     * | rules          | r           |
     * | leftMotor      | lm          |
     * | rightMotor     | rm          |
     * | panMotor       | pm          |
     * | tiltMotor      | tm          |
     * | distance       | ds          |
     * | degree         | dg          |
     * | executionTime  | t           |
     * +----------------+-------------+
     *  </pre>
     *
     * @return Shortened string of the RulesManagerEntity::toString()
     */
    public String getShortenedJson() {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("rulesCount", "rc");
        tokens.put("rules", "r");
        tokens.put("leftMotor", "lm");
        tokens.put("rightMotor", "rm");
        tokens.put("panMotor", "pm");
        tokens.put("tiltMotor", "tm");
        tokens.put("distance", "ds");
        tokens.put("executionTime", "t");
        tokens.put("degree", "dg");
        tokens.put(" ", "");

        String template = toString();

        // Create pattern of the format
        String patternString = "(" + StringUtils.join(tokens.keySet(), '|') + ")";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(template);

        StringBuilder sb = new StringBuilder();
        while (matcher.find())
            matcher.appendReplacement(sb, tokens.get(matcher.group(1)));
        matcher.appendTail(sb);

        return sb.toString();
    }
}
