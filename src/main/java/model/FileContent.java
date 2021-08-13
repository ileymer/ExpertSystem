package model;

import app.Utils;

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
            if (Utils.isFact(rule.rightPartString) || Utils.isFact(rule.leftPartString)) {
                if (rule.ruleType == RuleType.BIDIRECT_DEFINING) {
                    facts.get(rule.rightPartString).definers.add(new Definer(rule.leftPart));
                    facts.get(rule.leftPartString).definers.add(new Definer(rule.rightPart));
                } else if (rule.ruleType == RuleType.LEFT_DEFINING) {
                    facts.get(rule.rightPartString).definers.add(new Definer(rule.leftPart));
                } else if (rule.ruleType == RuleType.RIGHT_DEFINING) {
                    facts.get(rule.leftPartString).definers.add(new Definer(rule.rightPart));
                }
            }
        }
    }
}
