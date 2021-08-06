package app;

import com.sun.xml.internal.ws.api.ha.StickyFeature;

public class Fact {
    private String name;
    private boolean value;
    private boolean defined;

    private Tristate state;


    public Fact() {
        value = false;
        defined = false;
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
