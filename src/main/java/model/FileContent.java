package model;

import app.Utils;

import java.util.HashMap;
import java.util.LinkedList;

public class FileContent {
    public LinkedList<String> allFacts;
    public LinkedList<String> initFacts;
    public LinkedList<Rule> rules;
    public LinkedList<String> queries;
    public LinkedList<String> lines;

    public FileContent(LinkedList<String> allFacts, LinkedList<String> initFacts,
                       LinkedList<Rule> rules, LinkedList<String> queries,
                       LinkedList<String> lines) {
        this.allFacts = allFacts;
        this.initFacts = initFacts;
        this.rules = rules;
        this.queries = queries;
        this.lines = lines;
    }

    public FileContent() {
        this.allFacts = new LinkedList<>();
        this.initFacts = new LinkedList<>();
        this.rules = new LinkedList<>();
        this.queries = new LinkedList<>();
    }


    public HashMap<String, Fact> getFacts() {
        HashMap<String, Fact> facts = createFacts();
        setInitDefiners(facts);
        return facts;
    }


    private HashMap<String, Fact> createFacts() {
        HashMap<String, Fact> facts = new HashMap<>();

        for (String factName : allFacts) {
            facts.put(factName, new Fact(factName));
            if (initFacts.contains(factName)) {
                facts.get(factName).define(Tristate.TRUE);
            }
        }
        return facts;
    }


    private void setInitDefiners(HashMap<String, Fact> facts) {
        for (Rule rule : rules) {
            if (!rule.ruleType.isDefining())
                continue;
            if (Utils.isFact(rule.right.origin) || Utils.isFact(rule.left.origin)) {
                if (rule.ruleType == RuleType.BIDIRECT_DEFINING) {
                    facts.get(rule.right.origin).definers.add(new Expression(rule.left.origin));
                    facts.get(rule.left.origin).definers.add(new Expression(rule.right.origin));
                } else if (rule.ruleType == RuleType.LEFT_DEFINING) {
                    facts.get(rule.right.origin).definers.add(new Expression(rule.left.origin));
                } else if (rule.ruleType == RuleType.RIGHT_DEFINING) {
                    facts.get(rule.left.origin).definers.add(new Expression(rule.right.origin));
                }
            }
        }
    }
}
