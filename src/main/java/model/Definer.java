package model;

import java.util.ArrayList;

public class Definer {
    public boolean visited;
    public ArrayList<PolishRec> rec;
    public String origin;

    public Definer(ArrayList<PolishRec> rec, String origin) {
        this.rec = rec;
        visited = false;
        this.origin = origin;
    }
}
