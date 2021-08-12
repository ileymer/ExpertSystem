package model;

import java.util.ArrayList;

public class Fact {
    private String name;
    private boolean value;
    private boolean defined;

    private Tristate state;


    public Fact() {
        value = false;
        defined = false;
        ArrayList<ArrayList<PolishRec>> definers;
    }

    public Fact(Tristate state) {
        this.state = state;
    }

    public void define(boolean value) {
        this.value = value;
        defined = true;
    }

    @Override
    public String toString() {
        String stringValue;

        if (!defined)
            stringValue = "undefined";
        else
            stringValue = (new Boolean(value)).toString();

        return String.format(
                "%s - %s",
                name,
                stringValue);
    }
}
