package app;


import model.Fact;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

public class ValidationTests {
    private HashMap<String, Fact> temp;
    private String invalidFolder = "C:\\Users\\dmitr\\Desktop\\REPOs\\ExpertSystem\\test_cases\\invalid\\";
    private String validFolder = "C:\\Users\\dmitr\\Desktop\\REPOs\\ExpertSystem\\test_cases\\valid\\";
    private String correctionFolder = "C:\\Users\\dmitr\\Desktop\\REPOs\\ExpertSystem\\test_cases\\correction\\";

    private String keysToStringRow(HashMap<String, Fact> facts) {
        String s = "";

        for (String f : facts.keySet()) {
            s += f;
        }
        return s;
    }

    public LinkedList<String> listFilesForFolder(final File folder) {
        LinkedList<String> paths = new LinkedList<>();
        for (final File fileEntry : folder.listFiles()) {
            String tempPath = fileEntry.getAbsolutePath();
            paths.add(tempPath);
            //System.out.println(tempPath);
        }
        return paths;
    }



    @Test
    public void InvalidTest() {
        LinkedList<String> files = listFilesForFolder(new File(invalidFolder));
        int a =1;
        FileParser fileParser = new FileParser();
        for (String path : files) {
            LinkedList<String>tempLines = fileParser.getLines(path).orElse(new LinkedList<>());
            System.out.println(path);
            Assertions.assertFalse(Validator.isValid(tempLines));
        }
    }

    @Test
    public void ValidTest() {
        LinkedList<String> files = listFilesForFolder(new File(validFolder));
        int a =1;
        FileParser fileParser = new FileParser();
        for (String path : files) {
            LinkedList<String>tempLines = fileParser.getLines(path).orElse(new LinkedList<>());
            System.out.println(path);
            Assertions.assertTrue(Validator.isValid(tempLines));
        }
    }



}
