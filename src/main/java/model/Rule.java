package model;

import app.Utils;

public class Rule {
    public Expression left;
    public Expression right;
    public EquityType equityType;
    public RuleType ruleType;
    public String origin;

    public Rule(String line) {
        String [] splitted = line.contains("<=>") ? line.split("<=>") : line.split("=>");
        equityType = line.contains("<=>") ? EquityType.IF_AND_ONLY_IF : EquityType.IMPLICATION;
        origin = line;
        left = new Expression(splitted[0]);
        right = new Expression(splitted[1]);
        ruleType = RuleType.EQUIATION;
        if (equityType == EquityType.IMPLICATION && Utils.isFact(right.origin)) {
            ruleType = RuleType.LEFT_DEFINING;
        }
        else if (equityType == EquityType.IF_AND_ONLY_IF) {
            if (Utils.isFact(left.origin) && Utils.isFact(right.origin)) {
                ruleType = RuleType.BIDIRECT_DEFINING;
            }
            else if (Utils.isFact(left.origin)) {
                ruleType = RuleType.RIGHT_DEFINING;
            }
            else if (Utils.isFact(right.origin)) {
                ruleType = RuleType.LEFT_DEFINING;
            }
        }
    }

    @Override
    public String toString() {
        return origin;
    }
}
