package model;

import java.util.HashMap;
import java.util.LinkedList;

public class FileContent {
    public LinkedList<String> allFacts;
    public LinkedList<String> initFacts;
    public LinkedList<Rule> rules;
    public LinkedList<String> queries;

    public FileContent(LinkedList<String> allFacts, LinkedList<String> initFacts,
                       LinkedList<Rule> rules, LinkedList<String> queries) {
        this.allFacts = allFacts;
        this.initFacts = initFacts;
        this.rules = rules;
        this.queries = queries;
    }

    public FileContent() {
        this.allFacts = new LinkedList<>();
        this.initFacts = new LinkedList<>();
        this.rules = new LinkedList<>();
        this.queries = new LinkedList<>();
    }

    public HashMap<String, Fact> getFacts() {
        HashMap<String, Fact> facts = new HashMap<>();

        for (String factName : allFacts) {
            facts.put(factName, new Fact());
            if (initFacts.contains(factName)) {
                facts.get(factName).define(Tristate.TRUE);
            }
        }
        return facts;
    }
}
