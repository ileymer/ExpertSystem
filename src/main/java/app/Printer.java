package app;

import model.Fact;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.GREEN_TEXT;

public class Printer {
    private static int tabulationTimes = 0;

    public static void printError(String message)
    {
        System.err.println("Error: " + message);
        System.exit(0);
    }

    public static void printFacts(HashMap<String, Fact> facts) {
        facts.forEach((k, v) -> System.out.println(String.format("%s: %s", k, v.state)));
    }

    private static void decreaseTabTimes(int n) {
        tabulationTimes -= n;
        if (tabulationTimes < 0) {
            tabulationTimes = 0;
        }
    }

    public static void printFactsError(HashMap<String, Fact> facts) {
        facts.forEach((k, v) -> System.err.println(String.format("%s: %s", k, v.state)));
    }

    public static void printVerbose(String s ) {
        doPrintVerbose(s, 0);
    }

    public static void printVerbose(String s, int tabTimes) {
        doPrintVerbose(s, tabTimes);
    }

    private static void doPrintVerbose(String s, int tabTimes) {
        if (Main.verboseMode) {
            tabulationTimes += tabTimes;
            int temp = tabulationTimes;
            while (temp-- > 0) {
                System.out.print("\t");
            }
            System.out.println(colorize(s, GREEN_TEXT()));
        }
    }

    public static void printVerboseBordered(String s ) {
        if (Main.verboseMode) {
            String board = String.join("", Collections.nCopies(s.length(), s));
            System.out.println(board);
            printVerbose(s);
            System.out.println(board);
        }
    }
}
