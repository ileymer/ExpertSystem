package models;

import sun.awt.image.ImageWatched;

import java.util.LinkedList;

public class ExpertSystem {
    LinkedList<Rule> rules = new LinkedList<>();
    LinkedList<Fact> facts = new LinkedList<>();
    LinkedList<Fact> queries = new LinkedList<>();

    public ExpertSystem(LinkedList<String> lines) {
        addRules(lines);
        addFacts(lines);
        addQueries(lines);
    }


    public void addFacts(LinkedList<String> lines) {
        lines.stream().forEach(line -> {
            if (line.startsWith("=") && line.length() > 1) {
                for (char c : line.substring(1).toCharArray()) {
                    facts.add(new Fact((new Character(c)).toString(), true, true));
                }
            }
        });
    }

    public void addQueries(LinkedList<String> lines) {
        lines.stream().forEach(line -> {
            if (line.charAt(0) == '?') {
                for (char c : line.substring(1).toCharArray()) {
                    queries.add(new Fact((new Character(c)).toString(), false, false));
                }
            }
        });
    }

    public void addRules(LinkedList<String> lines) {
        lines.stream().forEach(line -> {
            if (line.charAt(0) != '?' && line.charAt(0) != '=') {
                rules.add(new Rule(line));
            }
        });
    }

    public void run() {
        ;
    }
}
