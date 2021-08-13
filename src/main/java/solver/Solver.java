package solver;

import app.Utils;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class Solver {
    HashMap<String, Fact> facts;
    LinkedList<String> queries;
    LinkedList<Rule> rules;
    FileContent fileContent;

    public Solver(FileContent fileContent) {
        this.fileContent = fileContent;
        rules = fileContent.rules;
        queries = fileContent.queries;
        facts = fileContent.getFacts();
    }

    public HashMap<String, Fact> getFactsFromLine(String line) {
        HashMap<String, Fact> facts = new HashMap<>();

        for (char c : line.toCharArray()) {
            if (Utils.isFact(c)) {
                facts.put(String.valueOf(c), new Fact(Tristate.UNDEF));
            }
        }
        return facts;
    }

    private boolean isAllFactsDefines(String left) {
        for (String key : getFactsFromLine(left).keySet()) {
            if (facts.get(key).state == Tristate.UNDEF) {
                return false;
            }
        }
        return true;
    }



    public void printFacts(HashMap<String, Fact> facts) {
        facts.forEach((k, v) -> System.out.println(String.format("%s: %s", k, v.state)));
    }

    private boolean isTrueImplication(boolean left, boolean right) {
        return !(left == true && right == false);
    }

    private boolean isTrueIfAndOnlyIf(boolean left, boolean right) {
        return left == right;
    }

    /*
    private void solveRule(Rule rule, HashMap<String, Tristate> temp) {
        Node node = parsingTree(x.leftPart);
        if (x.type == EquityType.IMPLICATION && isAllFactsDefines(x.leftPartString)) {
            System.out.println(node);
            solve(x.leftPart);
            if (!x.onlyLeft) {
                solve(x.rightPart);
            }
        }});
    }
*/
    public void setDefiners() {
        for (String fact : fileContent.initFacts) {
            for (Rule rule : rules) {
                if (rule.rightPartString.equals(fact)) {
                    if (!facts.get(fact).definers.contains(rule.rightPartString)) {
                        facts.get(fact).definers.add(rule.rightPart);
                    }
                }
            }
        }
    }

    private void handleImplication(Rule rule) {
        Tristate result = Operations.Imp(
                solve(rule.leftPart),
                solve(rule.rightPart)
        );

        if (result == Tristate.UNDEF) {

        }
    }

    public void run() {
        boolean cycle = true;
        HashMap<String, Fact> temp = new HashMap<>(facts);
        setDefiners();

        while (cycle) {
            for (Rule rule : rules) {
                handle
                if (rule.equityType == EquityType.IMPLICATION) {
                    handleImplication(rule);
                }
                else if (rule.equityType == EquityType.IF_AND_ONLY_IF) {
                    handleIfAndOnlyIf();
                }
            }
            if (facts.equals(temp)) {
                break;
            }
            temp = new HashMap<>(facts);
        }
        printFacts(facts);
    }

    public Tristate solve(ArrayList<PolishRec> rec)
    {
        ArrayList<Tristate>  stack =  new ArrayList<>();
        int i = 0;

        for (PolishRec s : rec)
        {
            if (Utils.isFact(s.rec.toCharArray()[0]))
            {
                stack.add(facts.get(s.rec).state);
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
