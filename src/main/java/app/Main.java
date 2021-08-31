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
        } else {
            handleInteractiveMode(fileContent);
        }
    }

    public static void run(FileContent fileContent) {
        Solver solver = new Solver(fileContent);

        Printer.printVerbose(String.format("%s\nStart processing\n%s",
                "===================================",
                "==================================="));
        HashMap<String, Fact> queries = solver.getQueries();

        if (!solver.isAllFactsDefined()) {
            Printer.printVerbose("\nSome facts are still undefined. It means that there is not enough information to define them by backward chaining engine." +
                    "\nSo we need to use forward chaining engine. Lets build a truth table.");
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
                Printer.printInteractive(String.format("Current line content: %s", fileContent.lines.get(input - 1)));
                Printer.printInteractive("Input new line content:");
                String newLine = (new FileParser()).processLine(scanner.nextLine());
                if (newLine.isEmpty()) {
                    Printer.printInteractive("Empty input. The line has not been modified");
                    continue;
                }
                fileContent.lines.set(input - 1, newLine);
                fileContent = (new FileParser()).getFileContent(fileContent.lines);
                Printer.printInteractive("Line has been modified successfully");
            }
            else if (input == 3) {
                Printer.printInteractive("Add input line:");
                String newLine = (new FileParser()).processLine(scanner.nextLine());
                if (newLine.isEmpty()) {
                    Printer.printInteractive("Empty input. The line has not been added");
                    continue;
                }
                fileContent.lines.addFirst(newLine);
                fileContent = (new FileParser()).getFileContent(fileContent.lines);
                Printer.printInteractive("Line has been added successfully");
            }
            else if (input == 4) {
                Printer.printInteractive("Choose the line to delete:");
                try {
                    input = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    Printer.printInteractiveError("invalid input");
                    continue;
                }
                if (input < 2 || input > fileContent.lines.size()) {
                    Printer.printInteractiveError("invalid row number");
                    continue;
                }
                if (fileContent.lines.size() == 1) {
                    Printer.printInteractiveError("you can't delete the last row");
                    continue;
                }
                fileContent.lines.remove(input - 1);
                fileContent = (new FileParser()).getFileContent(fileContent.lines);
                Printer.printInteractive("Line has been deleted successfully");
            }
            else if (input == 5) {
                Printer.printInteractive("Input lines:");
                for (int i = 0; i < fileContent.lines.size(); i++) {
                    Printer.printInteractive(String.format("%d. %s", i + 1, fileContent.lines.get(i)));
                }
            }
            else if (input == 6) {
                Printer.printInteractive("Exiting the program...");
                System.exit(0);
            }
            else {
                Printer.printInteractive("No menu option for this value");
            }
        }
    }
}
