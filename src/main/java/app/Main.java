package app;

import org.parboiled.Parboiled;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParseTreeUtils;
import org.parboiled.support.ParsingResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String [] args) {
        if (args.length == 0) {
            System.out.println("No input file");
        }
        Optional<LinkedList<String>> lines = Optional.of(getLines(args[0]));
    }

    public static LinkedList<String> getLines(String filePath) {
        LinkedList<String> lines = null;

        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.map(line -> line.split("#")[0].trim());
            lines = stream.collect(Collectors.toCollection(LinkedList::new));
            Validator.validate(lines);
        } catch (IOException e) {
            System.out.println("Can't read file");
        }
        return lines;
    }

    public static void runParserExample() {
        String input = "1+2-3";
        CalculatorParser parser = Parboiled.createParser(CalculatorParser.class);
        ParsingResult<?> result = new ReportingParseRunner(parser.Expression()).run(input);
        String parseTreePrintOut = ParseTreeUtils.printNodeTree(result);
        System.out.println(parseTreePrintOut);
        System.exit(0);
    }

}
