package model;

public class Operations {

    public static Tristate Not(Tristate f)
    {
        if (f != Tristate.UNDEF)
            return f == Tristate.FALSE ? Tristate.TRUE : Tristate.FALSE;
        return f;
    }

    public static Tristate And(Tristate f1, Tristate f2)
    {
        if (f1 == Tristate.UNDEF || f2 == Tristate.UNDEF) {
            return Tristate.UNDEF;
        }
        else if (f1 == Tristate.TRUE && f2 == Tristate.TRUE) {
            return Tristate.TRUE;
        }
        return Tristate.FALSE;
    }

    public static Tristate Or(Tristate f1, Tristate f2)
    {
        if (f1 == Tristate.TRUE || f2 == Tristate.TRUE) {
            return Tristate.TRUE;
        }
        if (f1 == Tristate.UNDEF || f2 == Tristate.UNDEF) {
            return Tristate.UNDEF;
        }
        return Tristate.FALSE;
    }

    public static Tristate Xor(Tristate f, Tristate f1)
    {
        if (f == Tristate.UNDEF || f1 == Tristate.UNDEF) {
            return Tristate.UNDEF;
        }
        if (f == f1) {
            return Tristate.FALSE;
        }
        return Tristate.TRUE;
    }

    public static Tristate Imp(Tristate f, Tristate f1) {
        if (f == Tristate.TRUE) {
            if (f1 == Tristate.FALSE) {
                return Tristate.FALSE;
            } else if (f1 == Tristate.UNDEF) {
                return Tristate.FALSE;
            }
        } else if (f == Tristate.UNDEF) {
            if (f1 == Tristate.UNDEF || f1 == Tristate.FALSE) {
                return Tristate.UNDEF;
            }
        }
        return Tristate.TRUE;
    }

    public static Tristate Eqv(Tristate f, Tristate f1)
    {
        if (f == Tristate.UNDEF || f1 == Tristate.UNDEF) {
            return Tristate.UNDEF;
        }
        if (f == f1) {
            return Tristate.TRUE;
        }
        return Tristate.FALSE;
    }
}
