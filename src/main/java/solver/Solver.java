package solver;

import app.Printer;
import app.Utils;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Stream;

import static app.Printer.printFacts;


public class Solver {
    public HashMap<String, Fact> facts;
    public LinkedList<String> queries;
    public LinkedList<Rule> rules;
    public FileContent fileContent;
    public HashMap <String, Expression> expressions;

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

    private boolean isAllFactsDefined() {
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
        Printer.printVerbose(String.format("Variable \"%s\" = %s", strFact, fact.state.toString()));
        if (fact.state == Tristate.UNDEF) {
            Printer.printVerbose(String.format("Lets check definers", strFact));
            for (Definer definer : fact.definers) {
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
        }
        return fact.state;
    }

    public void resetVisited() {
        for (Fact fact : facts.values()) {
            for (Definer definer : fact.definers) {
                definer.visited = false;
            }
        }
    }

    public HashMap<String, Fact> getQueries() {
        HashMap<String, Fact> temp = new HashMap<>(facts);

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
                if (isAllFactsDefined()) {
                    break;
                }
                for (Fact fact : facts.values()) {
                    if (!fact.defined && !queries.contains(fact.name)) {
                        fact.define(Tristate.FALSE);
                        break;
                    }
                }

                for (Fact fact : facts.values()) {
                    if (!fact.defined) {
                        fact.define(Tristate.FALSE);
                        break;
                    }
                }
            }
            temp = new HashMap<>(facts);
        }
        return facts;
    }

    public HashMap<String, Fact> returnQueriesFromFacts() {
        for (Fact fact : facts.values()) {
            if (!queries.contains(fact.name)) {
                facts.remove(fact);
            }
        }
        return facts;
    }

    public void checkSolution() {
        for (Rule rule : rules) {
            if (rule.equityType == EquityType.IMPLICATION &&
                    isTrueImplication(solve(rule.left.rec), solve(rule.right.rec)))
                continue;
            if (rule.equityType == EquityType.IF_AND_ONLY_IF &&
                    isTrueIfAndOnlyIf(solve(rule.left.rec), solve(rule.right.rec)))
                continue;
            Printer.printFactsError(getFactsFromLine(rule.origin));
            Printer.printError("logic error: there is a contradiction in facts. Start from this line: " + rule.origin);

        }
    }

    private boolean isUniqueUndefinedQuery(Expression expression) {
        return
    }

    private LinkedList<Fact> getUndefinedQueriesFromExpression(Expression expression) {
        LinkedList<Fact> queriesFromExpression = new LinkedList<>();
        for (String symbol : expression.origin.split("")) {
            if (queries.contains(symbol)) {
                Fact temp = facts.get(symbol);
                if (temp.state == Tristate.UNDEF) {
                    queriesFromExpression.add(temp);
                }

            }
        }
        return queriesFromExpression;
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


    //ab*cde!*f*!*+r@r@f=f=f=f=fehr+t=t=!*+=g=
    public Node parsingTree(ArrayList<PolishRec>  r)
    {
        int size;
        int p; 
        int sizeS = 0;
        //ArrayList <Integer> stack = new ArrayList<>();
        Node tree = new Node();
        ArrayList <Node> stack = new ArrayList<>();

        size = r.size() - 1;
        r.get(size).visited = true;
        stack.add(nodeAdd(r.get(size - 1)));
        stack.add(nodeAdd(r.get(size - 2)));
        tree.type = 2;
        tree.children[0] = stack.get(0);
        tree.children[1] = stack.get(1);
        while (stack.size() != 0)
        {
            if (stack.get(0).type == 2)
            {
                p = helperTree(r);
                stack.get(0).children[0] = nodeAdd(r.get(p));
                p--;
                stack.get(0).children[1] = nodeAdd(r.get(p));
                stack.add(stack.get(0).children[0]);
                stack.add(stack.get(0).children[1]);
            }
            else if (stack.get(0).type == 1)
            {
                p = helperTree(r);
                stack.get(0).children[0] = nodeAdd(r.get(p));
                stack.add(stack.get(0).children[0]);
            }
            stack.remove(0);

        }
        return (tree);
    }

    public int helperTree(ArrayList<PolishRec> r)
    {
        int i = r.size() - 1;

        while (i >= 0)
        {
            if (r.get(i).visited == false)
                return (i);
            i--;
        }
        return (i);
    }


    public Node nodeAdd(PolishRec r)
    {
        Node n = new Node();

        r.visited = true;
        n.name = r.rec;
        if (r.rec == "!")
            n.type = 1;
        else if (r.rec.toCharArray()[0] >= 'A' && r.rec.toCharArray()[0] <= 'Z')
            n.type = 0;
        else
            n.type = 2;
        return (n);
    }
}
