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
        Solver solver = new Solver(fileContent);

        Printer.printVerbose(String.format("%s\nStart processing\n%s",
                "===================================",
                "==================================="));
        HashMap<String, Fact> queries = solver.getQueries();
        solver.checkSolution();
        queries.keySet().stream().forEach(x-> System.out.println(String.format("%s: %s", x, queries.get(x).state)));
    }
}
