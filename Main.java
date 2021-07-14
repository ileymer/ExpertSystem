package com.company;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<Node> graph =  new ArrayList<>();
        Node f = new AtomNode();
        graph.add(f);
        PolishNotation("A + B | C + E + D + !(A + B | C + E + D) + A + B | C + E + !D + A + B | C + E + D + !(A + B | C + E + D) ");
    }




    public static void PolishNotation(String rule)
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
        for (PolishRec ss : rec)
            System.out.print(ss.rec);
        parsingTree(rec);


    }

    public static String addOperation(char c)
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


    public static int prioritet(char c)
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



    public static PolishRec strPolish(String s)
    {
        PolishRec rec = new PolishRec();

        rec.rec = s;
        return (rec);
    }
//ab*cde!*f*!*+r@r@f=f=f=f=fehr+t=t=!*+=g=
    public static Node parsingTree(ArrayList<PolishRec>  r)
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

    public static int helperTree(ArrayList<PolishRec> r)
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

    public static Node nodeAdd(PolishRec r)
    {
        Node n = new ConnectorNode();

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
