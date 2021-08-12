package app;

import java.util.Locale;

public class Printer {

    public static void printError(String message)
    {
        System.err.println("Error: " + message);
        System.exit(0);
    }
}
