package model;

import app.Utils;

import java.security.cert.TrustAnchor;
import java.util.ArrayList;

public class Fact {
    public String name;
    public boolean defined;
    public ArrayList<Expression> definers;
    public Tristate state;


    public Fact() {
        state = Tristate.UNDEF;
        defined = false;
        definers = new ArrayList<>();;
    }

    public Fact(String name) {
        this.state = Tristate.UNDEF;
        definers = new ArrayList<>();
        this.name = name;
    }

    public void define(Tristate state) {
        this.state = state;
        defined = true;
        ;
    }



    @Override
    public String toString() {
        String stringValue;

        if (!defined)
            stringValue = "undefined";
        else
            stringValue = state.toString();

        return String.format(
                "%s - %s",
                name,
                stringValue);
    }
}
