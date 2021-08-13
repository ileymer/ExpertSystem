package solver;

import app.Utils;
import model.*;

import java.io.File;
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
            if (facts.get(key) == Tristate.UNDEF) {
                return false;
            }
        }
        return true;
    }



    public void printFacts(HashMap<String, Tristate> facts) {
        facts.forEach((k, v) -> System.out.println(String.format("%s: %s", k, v)));
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
    public void run() {
        boolean cycle = true;
        HashMap<String, Tristate> temp = new HashMap<>(facts);


        while (cycle) {
            rules.stream().forEach(x -> {solve(x.leftPart);});
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
            i = stack.size() - 1;
            if (s.rec.toCharArray()[0] >= 'A' && s.rec.toCharArray()[0] <= 'Z')
            {
                stack.add(facts.get(s.rec));
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
                else if (s.rec == "=>")
                {
                    stack.set(i - 1, stack.set(i,  Operations.Imp(stack.get(i - 1), stack.get(i))));
                    stack.remove(i);
                }
                else if (s.rec == "<=>")
                {
                    stack.set(i - 1, stack.set(i,  Operations.Eqv(stack.get(i - 1), stack.get(i))));
                    stack.remove(i);
                }
            }
        }
        return stack.get(0);
    }




    public ArrayList<PolishRec> PolishNotation(String rule)
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

    public String addOperation(char c)
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


    public int prioritet(char c)
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



    public PolishRec strPolish(String s)
    {
        PolishRec rec = new PolishRec();

        rec.rec = s;
        return (rec);
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
