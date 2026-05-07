package util.csv;

import interfaces.CSVSerializable;
import util.printing.ConsolePrinter;
import java.io.*;
import java.util.List;

public class CSVWriter {

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

        // TODO: open the file for writing (respect the append flag)
        // TODO: if not appending, write the header line first
        // TODO: call toCSVLine() on each item and write it as a line
        // TODO: handle IOException
    }

    private static void append(String filePath, CSVSerializable item){
        // TODO: open the file in append mode and write item.toCSVLine()
        // TODO: handle IOException
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))){
            writer.write(item.toCSVLine());
            writer.newLine();
        }catch(IOException e){
            ConsolePrinter.printError("Error writing to file" + e.getMessage());
        }
    }

}