package model;

import java.util.LinkedList;

public class FileContent {
    public LinkedList<String> allFacts;
    public LinkedList<String> initFacts;
    public LinkedList<String> rules;
    public LinkedList<String> queries;

    public FileContent() {
        this.allFacts = new LinkedList<>();
        this.initFacts = new LinkedList<>();
        this.rules = new LinkedList<>();
        this.queries = new LinkedList<>();
    }
}
