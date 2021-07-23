package models;

public class Rule {
    public String leftSide;
    public String rightSide;
    public String leftRpn;
    public String rightRpn;
    public String op;
    public String string;

    public Rule(String s) {
        string = s;
        op = s.contains("<=>") ? "<=>" : "=>";
        String[] splitted = s.split(op);
        leftSide = splitted[0];
        rightSide = splitted[1];
        leftRpn = ;
        rightRpn = ;
    }
}
