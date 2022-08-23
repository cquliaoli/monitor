package com.alert;

import java.util.Map;

public interface Expression {
    boolean interpret(Map<String, Long> stats);
    int getId();

    public String toGraphViz();
}