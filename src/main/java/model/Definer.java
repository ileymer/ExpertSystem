package model;

import java.util.ArrayList;

public class Definer {
    public boolean visited;
    public ArrayList<PolishRec> rec;

    public Definer(ArrayList<PolishRec> rec) {
        this.rec = rec;
        visited = false;
    }
}
