package util.csv;

import util.constants.AppConstants;
import util.printing.ConsolePrinter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CSVReader {


    /**
     * Generic CSV reading method
     * @param filePath path to csv file
     * @return String Array containing all the read elements from a csv file.
     */
    public static List<String[]> read(String filePath) {
        // TODO: open the file at filePath
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;

            // TODO: skip the header line
            boolean firstLine = true;
            while((line = br.readLine()) != null){
                if(firstLine){
                    firstLine = false;
                    continue;
                }
            // TODO: split each remaining line by ";" and add to a list
                if(!line.isBlank()){
                    rows.add(line.split(AppConstants.CSV_DELIMITER));
                }

            }
        // TODO: handle IOException
        } catch (IOException e){
            ConsolePrinter.printError("Error reading file: "+ filePath);
            e.printStackTrace();
        }
        // Hint: use BufferedReader and FileReader
        // Hint: Danish decimals use comma — remember to replace "," with "." before parsing doubles
        return rows;
    }
}