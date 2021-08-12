package app;

public class Utils {
    public static boolean isFact(String fact) {
        return fact.charAt(0) >= 'A' && fact.charAt(0) <= 'Z';
    }

    public static boolean isFact(char fact) {
        return fact >= 'A' && fact <= 'Z';
    }
}
