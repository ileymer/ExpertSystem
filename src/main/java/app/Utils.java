package app;

import model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class Utils {
    public static boolean isFact(String fact) {
        return fact.charAt(0) >= 'A' && fact.charAt(0) <= 'Z' && fact.length() == 1;
    }

    public static boolean isFact(char fact) {
        return fact >= 'A' && fact <= 'Z';
    }

    public static ArrayList<PolishRec> PolishNotation(String rule)
    {
        ArrayList<PolishRec>  rec =  new ArrayList<>();
        ArrayList<String>  stack =  new ArrayList<>();

        int i = 0;
        int f = 0;
        int sizeS = 0;
        int size = rule.length();
        char []s = rule.toCharArray();

        while (i < size)
        {
            if (s[i] != ' ')
            {
                sizeS = stack.size() - 1;
                if (s[i] >= 'A' && s[i] <= 'Z')
                    rec.add(strPolish(Character.toString(s[i])));
                else if (s[i] == '(')
                    stack.add("(");
                else if (s[i] == ')')
                {
                    while (stack.get(sizeS) != "(") {
                        rec.add(strPolish(stack.get(sizeS)));
                        stack.remove(sizeS--);
                    }
                    stack.remove(sizeS);
                }
                else if (sizeS < 0)
                    stack.add(addOperation(s[i]));
                else
                {
                    if (prioritet(s[i]) > prioritet(stack.get(sizeS).toCharArray()[0]))
                    {
                        stack.add(addOperation(s[i]));
                    }
                    else
                    {
                        while (sizeS > -1 && prioritet(s[i]) <= prioritet(stack.get(sizeS).toCharArray()[0]))
                        {
                            rec.add(strPolish(stack.get(sizeS)));
                            stack.remove(sizeS--);
                        }
                        stack.add(addOperation(s[i]));
                    }
                }
                if (s[i] == '<')
                    i=+3;
                else if (s[i] == '=')
                    i=+2;
                else
                    i++;
            }
            else
                i++;
        }
        sizeS = stack.size() - 1;
        while (sizeS > -1)
        {
            rec.add(strPolish(stack.get(sizeS)));
            stack.remove(sizeS--);
        }
        return rec;
    }

    private static String addOperation(char c)
    {
        if (c == '!')
            return ("!");
        else if (c == '+')
            return ("+");
        else if (c == '|')
            return ("|");
        else if (c == '^')
            return ("^");
        else if (c == '=')
            return ("=>");
        else if (c == '<')
            return ("<=>");
        return ("");
    }


    private static int prioritet(char c)
    {
        if (c == '!')
            return (4);
        else if (c == '+')
            return (3);
        else if (c == '|')
            return (2);
        else if (c == '^')
            return (2);
        else if (c == '=')
            return (1);
        else if (c == '<')
            return (1);
        else if (c == '(')
            return (1);
        return (0);
    }

    private static PolishRec strPolish(String s)
    {
        PolishRec rec = new PolishRec();

        rec.rec = s;
        return (rec);
    }

    private void copyFacts(HashMap<String, Fact> facts) {

    }
    public static boolean hasUndefinedFacts(Rule rule, HashMap<String, Fact> facts) {
        return facts.values().stream()
                .filter(x -> rule.origin.contains(x.name) && x.state == Tristate.UNDEF).count() > 0;
    }

    public static void setFacts(HashMap<String, Fact>facts, LinkedList<Tristate>toSet, LinkedList<Fact>undefined) {

        for (int i = 0; i < undefined.size(); i++) {
            if (facts.containsKey(undefined.get(i).name)) {
                facts.get(undefined.get(i).name).state = toSet.get(i);
            Printer.printVerboseNoNewline(String.format("%s: %s\t", undefined.get(i).name, facts.get(undefined.get(i).name).state));
            }
        }
        Printer.printVerbose("\n");
    }
}
