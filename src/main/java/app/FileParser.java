package app;

import model.FileContent;
import model.Rule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileParser {
    public FileContent getFileContent(LinkedList<String> lines) {
        Validator.validate(lines);
        LinkedList<Rule> rules = getRules(lines).orElse(new LinkedList<>());
        LinkedList<String> allFacts = getAllFacts(lines);
        LinkedList<String> initFacts = getInitFacts(lines);
        LinkedList<String> queries = getQueries(lines);
        return new FileContent(allFacts, initFacts, rules, queries, lines);
    }

    public FileContent getFileContent(String filePath) {
        LinkedList<String> lines = getLines(filePath).orElse(new LinkedList<>());
        Validator.validate(lines);
        LinkedList<Rule> rules = getRules(lines).orElse(new LinkedList<>());
        LinkedList<String> allFacts = getAllFacts(lines);
        LinkedList<String> initFacts = getInitFacts(lines);
        LinkedList<String> queries = getQueries(lines);
        return new FileContent(allFacts, initFacts, rules, queries, lines);
    }

    private Optional<LinkedList<Rule>> getRules(LinkedList<String> lines) {
        LinkedList<Rule> rules = new LinkedList<>();
        lines.stream().forEach(line -> {
            if (Utils.isFact(line.charAt(0))) {
                rules.add(new Rule(line));
            }
         });
        return Optional.of(rules);
    }

    public String processLine(String line) {
        return line.split("#")[0]
                .trim()
                .replace(" ", "")
                .replace("\t", "");
    }

    private Optional<LinkedList<String>> getLines(String filePath) {
        LinkedList<String> lines = null;


        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            lines = stream
                    .map(this::processLine)
                    .filter(line -> !line.equals(""))
                    .collect(Collectors.toCollection(LinkedList::new));

        } catch (IOException e) {
            System.out.println("Can't read file");
            System.exit(-1);
        }
        return Optional.of(lines);
    }

    private LinkedList<String> getQueries(LinkedList<String> lines) {
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

    private LinkedList<String> getInitFacts(LinkedList<String> lines) {
        LinkedList<String> initFacts = new LinkedList<>();

        for (String line : lines) {
            if (line.startsWith("=")) {
                String [] splitted = line.split("=");
                if (splitted.length > 1) {
                    line = splitted[1];
                    for (char c : line.toCharArray()) {
                        initFacts.add(String.valueOf(c));
                    }
                }
                break;
            }
        }
        return initFacts;
    }

    private LinkedList<String> getAllFacts(LinkedList<String> lines) {
        HashSet<String> facts = new HashSet<>();

        lines.stream().forEach(line -> {
            for (char c: line.toCharArray()) {
                if (Utils.isFact(c)) {
                    facts.add(String.valueOf(c));
                }
            }
        });
        LinkedList<String> list = new LinkedList<>();
        for (String s : facts) {
            list.add(s);
        }
        return list;
    }
}
