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
    public FileContent getFileContent(String filePath) {
        LinkedList<String> lines = getLines(filePath).orElse(new LinkedList<>());
        LinkedList<Rule> rules = getRules(lines).orElse(new LinkedList<>());
        LinkedList<String> allFacts = getAllFacts(lines);
        LinkedList<String> initFacts = getInitFacts(lines);
        LinkedList<String> queries = getQueries(lines);
        return new FileContent(allFacts, initFacts, rules, queries);
    }
    private Optional<LinkedList<Rule>> getRules(LinkedList<String> lines) {
        LinkedList<Rule> rules = new LinkedList<>();
        lines.stream().forEach(line -> rules.add(new Rule(line)));
        return Optional.of(rules);
    }

    private Optional<LinkedList<String>> getLines(String filePath) {
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
            lines.add(s);
        }
        return list;
    }
}
