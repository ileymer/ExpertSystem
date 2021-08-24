package app;

import org.parboiled.Parboiled;

import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class Validator {

    private static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ<=>+|()!^";
    private static int maxLineLength = 1000;

    private static boolean isValidStructure(LinkedList<String> lines) {
        if (lines == null || lines.size() < 1) {
            System.err.println("input file must contain at least 1 row");
            return false;
        }
        if (lines.stream().filter(x -> x.length() > maxLineLength).count() > 0) {
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).length() > maxLineLength) {
                    System.err.println(String.format("Line %d is too long, length must not exceed %d", i + 1, maxLineLength));
                    return false;
                }
            }
        }
        long factRowCount = lines.stream().filter(x -> x.charAt(0) == '=').count();
        if (factRowCount > 1) {
            System.err.println("must be only one facts row");
            return false;
        }
        if (lines.stream().filter(x -> x.charAt(0) == '?').count() != 1) {
            System.err.println("must be one and only one queries row");
            return false;
        }
        if (lines.get(lines.size() - 1).charAt(0) != '?') {
            System.err.println("last line must be queries row and starts with ?");
            return false;
        }
        if (lines.stream().filter(x -> Arrays.stream(x.split("")).filter(c -> alphabet.contains(c)).count() > 0).count() == 0) {
            System.err.println("must be at least 1 fact in file");
            return false;
        }
        return true;
    }



    private static boolean isValidSyntax(LinkedList<String> lines) {

        LinkedList<String> temp = new LinkedList<>(lines);
        String queriesRow = temp.removeLast();
        String factsRow = temp.stream().filter(x -> x.charAt(0) == '=').findFirst().orElse("");
        temp.remove(factsRow);

        for (char c : queriesRow.toCharArray()) {
            if (!(alphabet + "?").contains(Character.toString(c))) {
                System.err.println(String.format(
                        "invalid char %c at line %d", c, temp.size()));
                return false;
            }
        }

        for (char c : factsRow.toCharArray()) {
            if (!(alphabet + "=").contains(Character.toString(c))) {
                System.err.println(String.format(
                        "invalid char %c at line %d", c, temp.size() - 1));
                return false;
            }
        }

        int counter = 0;
        for (String line : temp) {
            ++counter;
            for (char c : line.toCharArray()) {
                if (!(validChars).contains(Character.toString(c))) {
                    System.err.println(String.format(
                            "invalid char %c at line %d", c, counter));
                    return false;
                }
            }
            if (!isValidLineSyntax(line, counter)){
                return false;
            }
        }
        return true && isValidLineSyntax(queriesRow, lines.size()) && isValidLineSyntax(factsRow, lines.size() - 1);
    }

    private static boolean isValidLineSyntax(String line, int counter) {
        if (line.startsWith("?") || line.startsWith("=")) {
            if (line.length() > 1) {
                String tempLine = line.substring(1);
                for (String c : tempLine.split("")) {
                    if (!alphabet.contains(c)) {
                        System.err.println(String.format("parsing error: invalid format at line %d", counter));
                        return false;
                    }
                }
            }
            return true;
        }
        RuleParser ruleParser = new RuleParser(Parboiled.createParser(RuleParser.class));
        String parsed = ruleParser.parse(line);

        if (!parsed.equals(line)) {
            if (parsed.isEmpty() && !line.isEmpty()) {
                System.err.println(String.format("parsing error: invalid format at line %d", counter));
                return false;
            }

            int i = -1;
            while (++i < Math.min(parsed.length(), line.length())) {
                if (parsed.charAt(i) != line.charAt(i)) {
                    break ;
                }
            }
            System.err.println(String.format(
                    "invalid character at position %d: %c",
                    i, line.charAt(i)));
            return false;
        }
        return true;
    }

    public static boolean isValid(LinkedList<String> lines) {
        return isValidStructure(lines) && isValidSyntax(lines);
    }

    public static void validate(LinkedList<String> lines) {
        if (!isValidStructure(lines))
            System.exit(1);
        if (!isValidSyntax(lines))
            System.exit(1);
    }
}
