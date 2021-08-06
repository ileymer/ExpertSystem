package app;

import java.util.Arrays;

public class Node {
    public int type;
    public String name;
    public Node[] children = new Node[2];
    public boolean visited = false;
    public boolean state = false;

    public Node() {

    }

    @Override
    public String toString() {
        return "Node{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", children=" + Arrays.toString(children) +
                ", visited=" + visited +
                ", state=" + state +
                '}';
    }
}
