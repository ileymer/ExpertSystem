package app;


import model.Fact;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

public class ValidationTests {
    private HashMap<String, Fact> temp;

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
            String tempPath = folder.getName() + "/"+ fileEntry.getName();
            paths.add(tempPath);
            System.out.println(tempPath);
        }
        return paths;
    }

    final File folder = new File("test_cases/valid");

}
