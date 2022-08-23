package com.alert;

import java.util.Map;

public class OrExpression implements Expression {
    Expression e1;
    Expression e2;
    int id;

    OrExpression(Expression e1, Expression e2, int id) {
        this.e1 = e1;
        this.e2 = e2;
        this.id = id;
    }

    @Override
    public String toGraphViz() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("   %s[label=\"||\"];%s", id, System.lineSeparator()));
        sb.append(String.format("   %s->%s;%s", id, e1.getId(), System.lineSeparator()));
        sb.append(String.format("   %s->%s;%s", id, e2.getId(), System.lineSeparator()));
        sb.append(e1.toGraphViz());
        sb.append(e2.toGraphViz());
        return sb.toString();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean interpret(Map<String, Long> stats) {
        return e1.interpret(stats) || e2.interpret(stats);
    }

    @Override
    public String toString() {
        return e1.toString() + "||" + e2.toString();
    }
}
