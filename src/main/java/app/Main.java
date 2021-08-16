package app;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import model.Fact;
import model.FileContent;
import org.parboiled.Parboiled;
import solver.Solver;

import java.util.*;

@Parameters(separators = "=")
public class Main {
    @Parameter(names={"--verbose", "-v"})
    static boolean verboseMode;

    @Parameter(names={"--path", "-p"}, required = true)
    static String filePath;

    public static void main(String[] args) {
        JCommander jCommander = new JCommander(new Main());
        try {
            jCommander.parse(args);
        } catch (ParameterException e) {
            Printer.printError("The following options are required: [--path], [--verbose]");
        }


        if (args.length == 0) {
            System.out.println("No input file");
        }
        run(filePath);
    }

    public static void run(String filePath) {
        FileContent fileContent = (new FileParser()).getFileContent(filePath);
        //"D + !A + B | C + E + D + !(A + B | C + E + D) + A + B | C + E + !D + A + B | C + E + D + !(A + B | C + E + D);
        Solver solver = new Solver(fileContent);
        HashMap<String, Fact> queries = solver.getQueries();
        solver.checkSolution();
        queries.keySet().stream().forEach(x-> System.out.println(x));
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
