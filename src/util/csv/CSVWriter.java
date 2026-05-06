package util.csv;

import interfaces.CSVSerializable;
import java.util.List;

public class CSVWriter {

    public static void write(String filePath, List<? extends CSVSerializable> items,
                             String header, boolean append) {
        // TODO: open the file for writing (respect the append flag)
        // TODO: if not appending, write the header line first
        // TODO: call toCSVLine() on each item and write it as a line
        // TODO: handle IOException
    }

    public static void append(String filePath, CSVSerializable item) {
        // TODO: open the file in append mode and write item.toCSVLine()
        // TODO: handle IOException
    }
}