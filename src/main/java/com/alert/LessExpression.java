package com.alert;

import java.util.Map;

public class LessExpression implements Expression{
    private String key;
    private long value;

    private int id;
    public LessExpression(String key, long value, int id) {
        this.key = key;
        this.value = value;
        this.id = id;
    }

    @Override
    public String toGraphViz() {
        return String.format("   %s [label=\"%s\"];%s",id, this, System.lineSeparator());
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean interpret(Map<String,Long> stats) {
        if (!stats.containsKey(key)) {
            return false;
        }
        long statValue = stats.get(key);
        return statValue < value;
    }

    @Override
    public String toString() {
        return key + "<" + value;
    }
}
