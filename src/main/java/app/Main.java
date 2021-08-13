package app;


import org.parboiled.Parboiled;
import model.Rule;
import solver.Solver;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String [] args) {
        if (args.length == 0) {
            System.out.println("No input file");
        }
        //runParserExample();
        LinkedList<Rule> rules;
        LinkedList<String> lines = getLines(args[0]).orElse(new LinkedList<>());
        //"D + !A + B | C + E + D + !(A + B | C + E + D) + A + B | C + E + !D + A + B | C + E + D + !(A + B | C + E + D);

        rules = getRules(lines).orElse(new LinkedList<>());
        LinkedList<String> allFacts = getAllFacts(lines);
        LinkedList<String> initFacts = getInitFacts(lines);
        LinkedList<String> queries = getQueries(lines);
        Solver solver = new Solver(rules, allFacts, initFacts, queries);
        Solver.run(rules);
    }

    public static LinkedList<String> getQueries(LinkedList<String> lines) {
        LinkedList<String> queries = new LinkedList<>();

        for (String line : lines) {
            if (line.contains("?")) {
                line = line.split("\\?")[1];
                for (char c : line.toCharArray()) {
                    queries.add(String.valueOf(c));
                }
                break;
            }
        }
        return queries;
    }

    public static LinkedList<String> getInitFacts(LinkedList<String> lines) {
        LinkedList<String> initFacts = new LinkedList<>();

        for (String line : lines) {
            if (line.contains("=")) {
                line = line.split("=")[1];
                for (char c : line.toCharArray()) {
                    initFacts.add(String.valueOf(c));
                }
                break;
            }
        }
        return initFacts;
    }

    public static LinkedList<String> getAllFacts(LinkedList<String> lines) {
        HashSet<String> facts = new HashSet<>();

        lines.stream().forEach(line -> {
            for (char c: line.toCharArray()) {
                if (Utils.isFact(c)) {
                    facts.add(String.valueOf(c));
                }
            }
        });
        return (LinkedList<String>)facts.stream().collect(Collectors.toList());
    }

    public static LinkedList<String> testLines() {
        LinkedList<String> lines = new LinkedList<>();

        lines.add("!D => C");


        return lines;
    }

    public static Optional<LinkedList<String>> getLines(String filePath) {
        LinkedList<String> lines = null;


        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            lines = stream
                    .map(line -> line.split("#")[0]
                            .trim()
                            .replace(" ", "")
                            .replace("\t", ""))
                    .filter(line -> !line.equals(""))
                    .collect(Collectors.toCollection(LinkedList::new));
            Validator.validate(lines);
        } catch (IOException e) {
            System.out.println("Can't read file");
            System.exit(-1);
        }
        return Optional.of(lines);
    }

    public static Optional<LinkedList<Rule>> getRules(LinkedList<String> lines) {
        LinkedList<Rule> rules = new LinkedList<>();
        lines.stream().forEach(line -> rules.add(new Rule(line)));
        return Optional.of(rules);
    }

    public static void runParserExample() {
        String input = "A+B+C=>B+C";
        RuleParser parser = new RuleParser(Parboiled.createParser(RuleParser.class));
        String parsed =  parser.getRule(input);
        System.out.println(parsed);
        System.exit(0);
    }
}
