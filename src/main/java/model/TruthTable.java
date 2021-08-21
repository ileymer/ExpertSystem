package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class TruthTable {
    LinkedList<Fact> undefined;
    ArrayList<LinkedList<Boolean>> table;

    public TruthTable(LinkedList<Fact> undefined) {
        this.undefined = undefined;
        table = new ArrayList<>();

        undefined.stream().forEach(System.out::print);
        System.out.println();

        for (int i = 0; i < (int)Math.pow(2, undefined.size()); i++) {
            LinkedList<Boolean> row = new LinkedList<>();
            int j = undefined.size();
            String binary = Integer.toBinaryString(i);
            int binaryLen = binary.length();
            while (--j > -1) {
                boolean factValue = binaryLen > 0 && binary.charAt(binaryLen - 1) == '1' ? true : false;
                binaryLen--;
                row.addFirst(new Boolean(factValue));
                System.out.print(String.format("%s ", factValue));
            }
            table.add(row);
            System.out.println();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String format = "%-7s";

        for (int i = 0; i < undefined.size(); i++) {
            builder.append(String.format(format, undefined.get(i).name));
        }
        builder.append('\n');

        for (LinkedList<Boolean> row : table) {
            for (int i = 0; i < row.size(); i++) {
                builder.append(String.format(format, row.get(i).toString()));
            }
            builder.append('\n');
        }
        return builder.toString();
    }

}
