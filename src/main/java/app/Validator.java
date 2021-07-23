package app;

import java.util.LinkedList;

public class Validator {

    private static String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String validChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ<=>+|()!";

    private static boolean isValidStructure(LinkedList<String> lines) {
        if (lines == null || lines.size() < 3) {
            System.out.println("input file must contain at least 3 rows");
            return false;
            //System.exit(1);
        }
        if (lines.stream().filter(x -> x.charAt(0) == '0').count() > 1) {
            System.out.println("must be only one facts row");
            return false;
        }
        if (lines.stream().filter(x -> x.charAt(0) == '?').count() > 1) {
            System.out.println("must be only one queries row");
            return false;
        }
        if (lines.get(lines.size() - 1).charAt(0) != '?') {
            System.out.println("last line must be queries row and starts with ?");
            return false;
        }
        if (lines.get(lines.size() - 2).charAt(0) != '=') {
            System.out.println("last line must be facts row and starts with =");
            return false;
        }
        return true;
    }



    private static boolean isValidSyntax(LinkedList<String> lines) {

        LinkedList<String> temp = new LinkedList<>(lines);
        String queriesRow = temp.removeLast();
        String factsRow = temp.removeLast();

        for (char c : queriesRow.toCharArray()) {
            if (!(alphabet + "?").contains(Character.toString(c))) {
                System.out.println(String.format(
                        "invalid char %c at line %d", c, temp.size()));
                return false;
            }
        }

        for (char c : factsRow.toCharArray()) {
            if (!(alphabet + "=").contains(Character.toString(c))) {
                System.out.println(String.format(
                        "invalid char %c at line %d", c, temp.size() - 1));
                return false;
            }
        }

        int counter = 0;
        for (String line : temp) {
            ++counter;
            for (char c : line.toCharArray()) {
                if (!(validChars).contains(Character.toString(c))) {
                    System.out.println(String.format(
                            "invalid char %c at line %d", c, counter));
                    return false;
                }
                if (isValidLineSyntax(line, counter)){
                    return true;
                }
            }
        }
        return true;
    }

    private static boolean isValidLineSyntax(String line, int counter) {

        return true;
    }

    public static void validate(LinkedList<String> lines) {
        if (!isValidStructure(lines))
            System.exit(1);
        if (!isValidSyntax(lines))
            System.exit(1);
    }
}
