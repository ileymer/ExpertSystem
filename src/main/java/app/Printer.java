package app;

import model.Fact;
import model.Rule;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

public class Printer {
    private static int indent = 0;

    public static void printError(String message)
    {
        System.err.println("Error: " + message);
        System.exit(0);
    }

    public static void printFacts(HashMap<String, Fact> facts) {
        facts.forEach((k, v) -> System.out.println(String.format("%s: %s", k, v.state)));
    }

    public static void setIndent(int n) {
        indent = n;
    }

    public static void printError(HashMap<String, Fact> facts) {
        facts.forEach((k, v) -> System.err.println(String.format("%s: %s", k, v.state)));
    }

    public static void printError(LinkedList<Rule> rules) {
        rules.forEach(System.err::println);
    }

    public static void printVerbose(String s ) {
        doPrintVerbose(s, 0);
    }



    public static void printVerbose(String s, int tabTimes) {
        doPrintVerbose(s, tabTimes);
    }

    public static void printInteractive(String s) {
        if (Main.interactiveMode) {
            System.out.println(colorize(s, YELLOW_TEXT()));
        }
    }

    public static void printInteractiveError(String s) {
        if (Main.interactiveMode) {
            System.out.println(colorize(String.format("Error: %s", s), RED_TEXT()));
        }
    }

    public static void printInteractiveMenu() {
        printInteractive("\nPlease choose the action:\n" +
                "\t1. Run solver\n" +
                "\t2. Modify input line\n" +
                "\t3. Add input line\n" +
                "\t4. Delete input line\n" +
                "\t5. Print input lines\n" +
                "\t6. Exit\n");
    }

    public static void  printVerboseNoNewline(String s) {
        System.out.print(colorize(s, GREEN_TEXT()));
    }

    private static void doPrintVerbose(String s, int tabTimes) {
        if (Main.verboseMode) {
            indent += tabTimes;
            int temp = indent;
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
