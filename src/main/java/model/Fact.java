package model;

import java.security.cert.TrustAnchor;
import java.util.ArrayList;

public class Fact {
    private String name;
    public boolean defined;
    public ArrayList<ArrayList<PolishRec>> definers;
    public Tristate state;


    public Fact() {
        state = Tristate.UNDEF;
        defined = false;
        ArrayList<ArrayList<PolishRec>> definers;
    }

    public Fact(Tristate state) {
        this.state = state;
    }

    public void define(Tristate state) {
        this.state = state;
        defined = true;
    }

    public Tristate getState() {
        if (state == Tristate.UNDEF) {
            for (ArrayList<PolishRec> definer : definers) {
                if (!definer.isVisited) {
                    Tristate temp = solve(definer);
                    if (temp != Tristate.UNDEF) {

                    }
                    definer.isVisited = true;
                }

            }
        }
        return state;
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
