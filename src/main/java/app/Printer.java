package app;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi;

import java.util.Locale;

public class Printer {
    static ColoredPrinter enemyPrinter;
    static ColoredPrinter emptyPrinter;
    static ColoredPrinter playerPrinter;
    static ColoredPrinter goalPrinter;
    static ColoredPrinter wallPrinter;

    static {
        enemyPrinter = Printer.initPrinter("black");
        emptyPrinter = Printer.initPrinter("red");
        playerPrinter = Printer.initPrinter("green");
        goalPrinter = Printer.initPrinter("yellow");
        wallPrinter = Printer.initPrinter("magenta");
    }

    public static void printError(String message)
    {
        System.err.println("Error: " + message);
        System.exit(0);
    }

    public static Ansi.BColor determineColor(String color) {
        color = color.toLowerCase(Locale.ROOT);

        switch (color) {
            case "black":
                return Ansi.BColor.BLACK;
            case "green":
                return Ansi.BColor.GREEN;
            case "red":
                return Ansi.BColor.RED;
            case "yellow":
                return Ansi.BColor.YELLOW;
            case "blue":
                return Ansi.BColor.BLUE;
            case "magenta":
                return Ansi.BColor.MAGENTA;
            case "cyan":
                return Ansi.BColor.CYAN;
            case "white":
                return Ansi.BColor.WHITE;
            default:
                System.err.println("Error: invalid color '" + color + "'");
                System.exit(-1);
        }
        throw new IllegalStateException();
    }

    private static ColoredPrinter initPrinter(String color) {
        Ansi.BColor bColor = determineColor(color);
        if (Ansi.BColor.BLACK.equals(bColor)) {
            return new ColoredPrinter.Builder(1, false)
                    .foreground(Ansi.FColor.WHITE).background(bColor).build();
        }
        return new ColoredPrinter.Builder(1, false)
                .foreground(Ansi.FColor.BLACK).background(bColor).build();
    }

}
