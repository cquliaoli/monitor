package com.alert;

import java.util.HashMap;
import java.util.Map;

public class AlertRuleInterpreterTest {
    public static void main(String[] args) {
        String rule = "key1 > 100 && key2 < 30 || ((key7 = 8 || key8 =2) " +
                "&& key3 < 100 && (key5 = 8 || key6 > 4)) || key4 = 88";

        AlertRuleInterpreter alertRuleInterpreter = new AlertRuleInterpreter(
                rule.replaceAll("\\s",""));
        Map<String, Long> stats = new HashMap<>();
        stats.put("key1", 101L);
        stats.put("key2", 101L);
        stats.put("key3", 121L);
        stats.put("key4", 78L);
        stats.put("key5", 38L);
        stats.put("key6", 88L);

        boolean alert = alertRuleInterpreter.interpret(stats);
        System.out.println(alertRuleInterpreter.toGraphViz());
        System.out.println(alert);
    }
}
