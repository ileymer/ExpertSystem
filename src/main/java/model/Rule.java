package model;

import solver.Solver;

import java.util.ArrayList;

public class Rule {
    public ArrayList <PolishRec> leftPart;
    public ArrayList <PolishRec> rightPart;
    public String leftPartString;
    public String rightPartString;
    public boolean onlyLeft;
    public EquityType type;

    public Rule(String line) {
        if (line.contains("<=>") || line.contains("=>")) {
            String [] splitted = line.contains("<=>") ? line.split("<=>") : line.split("=>");
            type = line.contains("<=>") ? EquityType.IF_AND_ONLY_IF : EquityType.IMPLICATION;
            onlyLeft = false;
            leftPartString = splitted[0];
            rightPartString = splitted[1];
            leftPart = Solver.PolishNotation(splitted[0]);
            rightPart = Solver.PolishNotation(splitted[1]);
        }
        else {
            onlyLeft = true;
            leftPart = Solver.PolishNotation(line);
            leftPartString = line;
        }
    }
}
