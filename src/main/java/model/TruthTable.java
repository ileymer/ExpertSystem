package model;

import sun.awt.image.ImageWatched;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class TruthTable {
    public LinkedList<Fact> undefined;
    public ArrayList<LinkedList<Tristate>> table;

    public TruthTable(LinkedList<Fact> undefined) {
        this.undefined = undefined;
        table = new ArrayList<>();

        for (int i = 0; i < (int)Math.pow(2, undefined.size()); i++) {
            LinkedList<Tristate> row = new LinkedList<>();
            int j = undefined.size();
            String binary = Integer.toBinaryString(i);
            int binaryLen = binary.length();
            while (--j > -1) {
                Tristate factValue = binaryLen > 0 && binary.charAt(binaryLen - 1) == '1' ? Tristate.TRUE : Tristate.FALSE;
                binaryLen--;
                row.addFirst(factValue);
            }
            table.add(row);
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

        for (LinkedList<Tristate> row : table) {
            for (int i = 0; i < row.size(); i++) {
                builder.append(String.format(format, row.get(i).toString()));
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    public void addRow(LinkedList<Tristate>row) {
        table.add(row);
    }

    public void dropAllRows() {
        while (table.size() > 0) {
            table.remove(0);
        }
    }
}
