package util.csv;

import interfaces.CSVSerializable;
import util.printing.ConsolePrinter;
import java.io.*;
import java.util.List;

/**
 * Utility class for writing {@link interfaces.CSVSerializable} objects to CSV files.
 * <p>
 *     Supports both full overwrites (with a header row) and appending individual records
 *     to an existing file. Uses a {@link BufferedWriter} for efficient I/O.
 *     This class cannot be instantiated — all methods are static.
 * </p>
 */
public class CSVWriter {


    /**
     * Generic CSV writing method, turns a list into a csv file.
     * @param filePath CSV file path to write to
     * @param items List of CSVSerializable items to write to CSV
     * @param header String of headers for the CSV file
     * @param append Boolean that determines if items should be appended or overwrite the file.
     */
    public static void write(String filePath, List<? extends CSVSerializable> items,
                             String header, boolean append) {
        if(append){
            for (CSVSerializable item : items){
                CSVWriter.append(filePath,item);
            }
        }else{
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))){
                writer.write(header);
                writer.newLine();
                for (CSVSerializable item : items){
                    writer.write(item.toCSVLine());
                    writer.newLine();
                }
            }catch (IOException e){
                ConsolePrinter.printError("Error writing to file: " + e.getMessage());
            }
        }

    }

    /**
     * Appends a list to a CSV file
     * @param filePath CSV file path to write to
     * @param item List of CSVSerializable items to write to CSV
     */
    public static void append(String filePath, CSVSerializable item) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.newLine();
            writer.write(item.toCSVLine());
        } catch (IOException e) {
            ConsolePrinter.printError("Error appending to file: " + e.getMessage());
        }
    }
}