package app;

import app.Printer;
import app.Utils;
import model.*;
import sun.awt.image.ImageWatched;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static app.Printer.printFacts;


public class Solver {
    public HashMap<String, Fact> facts;
    public LinkedList<String> queries;
    public LinkedList<Rule> rules;
    public FileContent fileContent;
    public HashMap <String, Expression> expressions;
    public TruthTable solutions;

    public Solver(FileContent fileContent) {
        this.fileContent = fileContent;
        rules = fileContent.rules;
        queries = fileContent.queries;
        facts = fileContent.getFacts();
    }

    public HashMap<String, Fact> getFactsFromLine(String line) {
        HashMap<String, Fact> getFacts = new HashMap<>();

        for (char c : line.toCharArray()) {
            if (Utils.isFact(c)) {
                getFacts.put(String.valueOf(c), facts.get(String.valueOf(c)));
            }
        }
        return getFacts;
    }

    public boolean isAllFactsDefined() {
        return facts.values().stream().filter(x -> x.defined == false).count() == 0;
    }

    private boolean isTrueImplication(Tristate left, Tristate right) {
        return !(left == Tristate.TRUE && right == Tristate.FALSE);
    }

    private boolean isTrueIfAndOnlyIf(Tristate left, Tristate right) {
        return left == right;
    }


    public Tristate getState(String strFact) {
        Fact fact = facts.get(strFact);
        //Printer.printVerbose(String.format("Variable \"%s\" = %s", strFact, fact.state.toString()));
        if (fact.state == Tristate.UNDEF) {
            Printer.printVerbose(String.format("Lets check definers of \"%s\"", strFact));
            for (Expression definer : fact.definers) {
                if (!definer.visited) {
                    Printer.printVerbose(String.format("Definer \"%s\" is not visited, visiting the definer", definer.origin), 1);
                    Tristate temp = solve(definer.rec);
                    if (temp != Tristate.UNDEF) {
                        fact.define(temp);
                        Printer.printVerbose(String.format(
                                "Variable %s has been set to %s",
                                fact.name, fact.state));
                    }
                    definer.visited = true;
                }
            }
            if (fact.definers.size() == 0) {
                Printer.printVerbose("There are no definers");
            }
        }
        return fact.state;
    }

    public void resetVisited() {
        for (Fact fact : facts.values()) {
            for (Expression expression : fact.definers) {
                expression.visited = false;
            }
        }
    }

    public HashMap<String, Fact> getQueries() {
        HashMap<String, Fact> temp = new HashMap<>(facts);
        HashMap<String, Fact> queriesDict = new HashMap<>();

        Printer.printVerbose("\nWe need to find these variables: ");
        queries.stream().forEach(x -> Printer.printVerbose(x));
        Printer.printVerbose("");

        while (true) {
            resetVisited();
            for (String query : queries) {
                Printer.printVerbose(String.format("Getting state of \"%s\" variable:", query));
                getState(query);
            }
            if (facts.equals(temp)) {
                break ;
            }
            temp = new HashMap<>(facts);
        }
        facts.values().stream()
                .filter(x -> queries.contains(x.name))
                .forEach(x -> queriesDict.put(x.name, x));
        return queriesDict;
    }

    public LinkedList<Fact> getUndefinedFacts() {
        return facts.values().stream().filter(x -> x.defined == false)
                .collect(Collectors.toCollection(LinkedList<Fact>::new));
    }

    public void solveTruthTable() {
        LinkedList<Fact> undefined = getUndefinedFacts();
        TruthTable table = new TruthTable(undefined);
        solutions = new TruthTable(undefined);
        solutions.dropAllRows();
        HashMap<String, Fact> temp = new HashMap<>(facts);

        for (LinkedList<Tristate> row : table.table) {
            Utils.setFacts(temp, row, undefined);
            if (isValidSolution()) {
                solutions.addRow(row);
            }
        }
    }

    public void checkSolution() {
        if (!isValidSolution()) {
            Printer.printFactsError(facts);
            Printer.printError("logic error: there is a contradiction in facts");
        }
    }

    public boolean isValidSolution() {
        for (Rule rule : rules) {
            if (rule.equityType == EquityType.IMPLICATION &&
                    isTrueImplication(solve(rule.left.rec), solve(rule.right.rec)))
                continue;
            if (rule.equityType == EquityType.IF_AND_ONLY_IF &&
                    isTrueIfAndOnlyIf(solve(rule.left.rec), solve(rule.right.rec)))
                continue;
            return false;
        }
        return true;
    }

    private Tristate solve(ArrayList<PolishRec> rec)
    {
        ArrayList<Tristate>  stack =  new ArrayList<>();
        int i = 0;

        for (PolishRec s : rec)
        {
            i = stack.size() - 1;
            if (Utils.isFact(s.rec.toCharArray()[0]))
            {
                stack.add(getState(s.rec));
            }
            else
            {
                if (s.rec == "!")
                    stack.set(i,  Operations.Not(stack.get(i)));
                else if (s.rec == "+")
                {
                    stack.set(i - 1, stack.set(i,  Operations.And(stack.get(i - 1), stack.get(i))));
                    stack.remove(i);
                }
                else if (s.rec == "|")
                {
                    stack.set(i - 1, stack.set(i,  Operations.Or(stack.get(i - 1), stack.get(i))));
                    stack.remove(i);
                }
                else if (s.rec == "^")
                {
                    stack.set(i - 1, stack.set(i,  Operations.Xor(stack.get(i - 1), stack.get(i))));
                    stack.remove(i);
                }
            }
        }
        return stack.get(0);
    }
}
