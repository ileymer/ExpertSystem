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
    public EquityType type;

    public Rule(String line) {
        if (line.contains("<=>") || line.contains("=>")) {
            String [] splitted = line.contains("<=>") ? line.split("<=>") : line.split("=>");
            type = line.contains("<=>") ? EquityType.IF_AND_ONLY_IF : EquityType.IMPLICATION;
            onlyLeft = false;
            leftPartString = splitted[0];
            rightPartString = splitted[1];
            leftPart = Utils.PolishNotation(splitted[0]);
            rightPart = Utils.PolishNotation(splitted[1]);
        }
        else {
            onlyLeft = true;
            leftPart = Utils.PolishNotation(line);
            leftPartString = line;
        }
    }
}
