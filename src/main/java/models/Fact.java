package models;

import java.util.Objects;

public class Fact {
    private String name;
    private boolean value;
    private boolean defined;

    public Fact() {
        value = false;
        defined = false;
    }

    public Fact(String name, boolean value, boolean defined) {
        this.name = name;
        this.value = value;
    }

    public void define(String name, boolean value) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fact fact = (Fact) o;
        return Objects.equals(name, fact.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
