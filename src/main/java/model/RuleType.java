package model;

public enum RuleType {
    EQUIATION,
    LEFT_DEFINING,
    RIGHT_DEFINING,
    BIDIRECT_DEFINING;

    public boolean isDefining() {
        return this != EQUIATION;
    }
}


