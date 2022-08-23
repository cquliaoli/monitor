package com.alert;

import java.util.Map;

public class AlertRuleInterpreter {

    int pos;
    String b;
    int id;
    private final Expression expression;

    public AlertRuleInterpreter(String b) {
        this.b = b;
        this.pos = 0;
        this.expression = parseOr();
    }

    /**
     * 转换为 graphviz 格式
     * @return graphviz
     */
    public String toGraphViz() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph AlertRuleInterpreter {").append(System.lineSeparator());
        sb.append(String.format(expression.toGraphViz()));
        sb.append("}");
        return sb.toString();
    }

    boolean interpret(Map<String, Long> stats) {
        return expression.interpret(stats);
    }

    /**
     * 语法规则如下：<p>
     * or ::= and || or <br>
     * and ::= simple && and <br>
     * simple ::= a > b <br>
     * |(or) <br>
     * <p>
     * 采用递归下降算法解析（Recursive Descent）
     * @return expression
     */
    Expression parseOr() {
        Expression e = parseAnd();
        if (match('|') && match('|')) {
            e = makeOr(e, parseOr());
        }
        return e;
    }

    Expression parseAnd() {
        Expression e = parseSimple();
        if (match('&') && match('&')) {
            e = makeAnd(e, parseAnd());
        }

        return e;
    }

    Expression parseSimple() {
        if (peek() == '(') {
            next();
            Expression e = parseOr();
            next();
            return e;
        } else {
            String key = parseKey();
            if (peek() == '>') {
                next();
                return makeGreater(key, parseValue());
            } else if (peek() == '<') {
                next();
                return makeLess(key, parseValue());
            } else {
                next();
                return makeEqual(key, parseValue());
            }
        }
    }

    String parseKey() {
        StringBuilder key = new StringBuilder();
        while (more() && (Character.isDigit(peek()) || Character.isAlphabetic(peek()))) {
            key.append(next());
        }
        return key.toString();
    }

    Long parseValue() {
        StringBuilder value = new StringBuilder();
        while (more() && Character.isDigit(peek())) {
            value.append(next());
        }
        return Long.parseLong(value.toString());
    }
    boolean more() {
        return pos < b.length();
    }

    char peek() {
        if (!more()) {
            throw new IllegalArgumentException("pos exceed limit");
        }

        return b.charAt(pos);
    }

    char next() {
        if (!more()) {
            throw new IllegalArgumentException("pos exceed limit");
        }

        return b.charAt(pos++);
    }

    private boolean match(char c) {
        if (pos >= b.length())
            return false;
        if (b.charAt(pos) == c) {
            pos++;
            return true;
        }
        return false;
    }

    Expression makeOr(Expression e1, Expression e2) {
        return new OrExpression(e1, e2, id++);
    }

    Expression makeAnd(Expression e1, Expression e2) {
        return new AndExpression(e1, e2, id++);
    }

    Expression makeGreater(String key, Long v) {
        return new GreaterExpression(key, v, id++);
    }

    Expression makeLess(String key, Long v) {
        return new LessExpression(key, v, id++);
    }

    Expression makeEqual(String key, Long v) {
        return new EqualExpression(key, v, id++);
    }

}
