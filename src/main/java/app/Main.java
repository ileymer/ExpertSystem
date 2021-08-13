package app;


import model.FileContent;
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
        String filePath = args[0];
        FileContent fileContent = (new FileParser()).getFileContent(filePath);
        //"D + !A + B | C + E + D + !(A + B | C + E + D) + A + B | C + E + !D + A + B | C + E + D + !(A + B | C + E + D);
        (new Solver(fileContent)).run();
    }



    public static LinkedList<String> testLines() {
        LinkedList<String> lines = new LinkedList<>();

        lines.add("!D => C");


        return lines;
    }

    public static void runParserExample() {
        String input = "A+B+C=>B+C";
        RuleParser parser = new RuleParser(Parboiled.createParser(RuleParser.class));
        String parsed =  parser.getRule(input);
        System.out.println(parsed);
        System.exit(0);
    }
}
