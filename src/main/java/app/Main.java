package app;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import model.Fact;
import model.FileContent;
import model.Rule;

import java.util.*;

@Parameters(separators = "=")
public class Main {
    @Parameter(names={"--verbose", "-v"})
    static boolean verboseMode;

    @Parameter(names={"--path", "-p"}, required = true)
    static String filePath;

    @Parameter(names={"--interactive", "-i"})
    static boolean interactiveMode;

    public static void main(String[] args) {
        JCommander jCommander = new JCommander(new Main());
        try {
            jCommander.parse(args);
        } catch (ParameterException e) {
            Printer.printError("The following options are required: --path, [--verbose], [--interactive]");
        }

        if (args.length == 0) {
            System.out.println("No input file");
        }
        FileContent fileContent = (new FileParser()).getFileContent(filePath);

        Printer.printInteractive("Welcome to interactive mode");

        if (!interactiveMode) {
            run(fileContent);
            System.exit(0);
        }
        handleInteractiveMode(fileContent);
    }

    public static void handleInteractiveMode(FileContent fileContent){

        Scanner scanner = new Scanner(System.in);
        while (true) {
            int input;
            Printer.printInteractiveMenu();
            try {
                input = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                Printer.printInteractiveError("invalid input");
                continue;
            }
            if (input == 1) {
                Printer.printInteractive("Solver is running...");
                run(fileContent);
            }
            else if (input == 2) {
                Printer.printInteractive("Choose the line to modify:");
                try {
                    input = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    Printer.printInteractiveError("invalid input");
                    continue;
                }
                if (input < 1 || input > fileContent.lines.size()) {
                    Printer.printInteractiveError("invalid row number");
                    continue;
                }
                Printer.printInteractive("Input new line content:");
                String newLine = scanner.nextLine();
                if (newLine.isEmpty()) {
                    Printer.printInteractive("Empty input. The line isn't modified");
                }
                fileContent.lines.set(input - 1, newLine);
                fileContent = (new FileParser()).getFileContent(fileContent.lines);
                Printer.printInteractive("Line modified successfully");
            }
            else if (input == 3) {
                Printer.printInteractive("Input data:");
                for (int i = 0; i < fileContent.lines.size(); i++) {
                    Printer.printInteractive(String.format("%d. %s", i + 1, fileContent.lines.get(i)));
                }
            }
            else if (input == 4) {
                Printer.printInteractive("Exit the program");
            }
            else {
                Printer.printInteractive("No menu option for this value");
            }
        }

    }

    public static void run(FileContent fileContent) {
        Solver solver = new Solver(fileContent);

        Printer.printVerbose(String.format("%s\nStart processing\n%s",
                "===================================",
                "==================================="));
        HashMap<String, Fact> queries = solver.getQueries();

        if (!solver.isAllFactsDefined()) {
            Printer.printVerbose("\nSome facts are still undefined. It means that there is not enough information to define them for backward chaining engine. So we need to use forward chaining engine. Lets build a truth table.");
            solver.solveTruthTable();
            if (Main.verboseMode && solver.solutions.table.size() > 1) {
                Printer.printVerbose("\nThere are several solutions from the truth table:\n", -100);
                Printer.printVerbose(solver.solutions.toString());
                Printer.printVerbose("By default, the program chooses the last solution");
            }
        }
        solver.checkSolution();
        queries.keySet().stream().forEach(x-> System.out.println(String.format("%s: %s", x, queries.get(x).state)));
    }
}
