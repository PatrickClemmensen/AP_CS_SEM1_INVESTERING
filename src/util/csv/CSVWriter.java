package util.csv;

import interfaces.CSVSerializeable;
import java.util.List;

public class CSVWriter {

    public static void write(String filePath, List<? extends CSVSerializeable> items,
                             String header, boolean append) {
        // TODO: open the file for writing (respect the append flag)
        // TODO: if not appending, write the header line first
        // TODO: call toCSVLine() on each item and write it as a line
        // TODO: handle IOException
    }

    public static void append(String filePath, CSVSerializeable item) {
        // TODO: open the file in append mode and write item.toCSVLine()
        // TODO: handle IOException
    }
}