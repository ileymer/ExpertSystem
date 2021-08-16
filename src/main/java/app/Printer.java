package app;

import model.Fact;

import java.util.HashMap;
import java.util.Locale;

public class Printer {

    public static void printError(String message)
    {
        System.err.println("Error: " + message);
        System.exit(0);
    }

    public static void printFacts(HashMap<String, Fact> facts) {
        facts.forEach((k, v) -> System.out.println(String.format("%s: %s", k, v.state)));
    }

    public static void printFactsError(HashMap<String, Fact> facts) {
        facts.forEach((k, v) -> System.err.println(String.format("%s: %s", k, v.state)));
    }

    public static void printVerbose(String s ) {
        if (Main.verboseMode) {
            System.out.println(s);
        }
    }
}
