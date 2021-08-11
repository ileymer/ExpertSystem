package model;

public class Operations {

    public static boolean Not(boolean f)
    {
        return (!f);
    }

    public static boolean And(boolean f, boolean f1)
    {
        f = f && f1;
        return (f);
    }

    public static boolean Or(boolean f, boolean f1)
    {
        f = f || f1;
        return (f);
    }

    public static boolean Xor(boolean f, boolean f1)
    {
        if (f != f1)
            return true;
        return false;
    }

    public static boolean Imp(boolean f, boolean f1)
    {
        if (f == true && f1 == false)
            return false;
        return true;
    }

    public static boolean Eqv(boolean f, boolean f1)
    {
        if (f == f1)
            return true;
        return false;
    }
}
