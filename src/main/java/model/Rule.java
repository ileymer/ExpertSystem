package model;

import app.Utils;
import solver.Solver;

import java.util.ArrayList;

public class Rule {
    public ArrayList <PolishRec> leftPart;
    public ArrayList <PolishRec> rightPart;
    public String leftPartString;
    public String rightPartString;
    public boolean onlyLeft;
    public EquityType equityType;
    public RuleType ruleType;
    public String origin;

    public Rule(String line) {
        String [] splitted = line.contains("<=>") ? line.split("<=>") : line.split("=>");
        equityType = line.contains("<=>") ? EquityType.IF_AND_ONLY_IF : EquityType.IMPLICATION;
        onlyLeft = false;
        origin = line;
        leftPartString = splitted[0];
        rightPartString = splitted[1];
        leftPart = Utils.PolishNotation(splitted[0]);
        rightPart = Utils.PolishNotation(splitted[1]);
        ruleType = RuleType.EQUIATION;
        if (equityType == EquityType.IMPLICATION && Utils.isFact(rightPartString)) {
            ruleType = RuleType.LEFT_DEFINING;
        }
        else if (equityType == EquityType.IF_AND_ONLY_IF) {
            if (Utils.isFact(leftPartString) && Utils.isFact(rightPartString)) {
                ruleType = RuleType.BIDIRECT_DEFINING;
            }
            else if (Utils.isFact(leftPartString)) {
                ruleType = RuleType.RIGHT_DEFINING;
            }
            else if (Utils.isFact(rightPartString)) {
                ruleType = RuleType.LEFT_DEFINING;
            }
        }
    }
}
