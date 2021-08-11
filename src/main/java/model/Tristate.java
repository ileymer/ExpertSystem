package model;

public enum Tristate {
    TRUE,
    FALSE,
    UNDEF;


    public boolean toBoolean() {
        if (this == Tristate.TRUE) {
            return true;
        }
        return false;
    }
}


