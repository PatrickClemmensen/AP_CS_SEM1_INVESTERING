package util.csv;

import util.AppConstants;
import util.printing.ConsolePrinter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Utility class for reading semicolon-delimited CSV files.
 * <p>
 *     Uses a {@link BufferedReader} for efficient file access.
 *     The first line of every file is treated as a header and skipped automatically.
 *     Blank lines are also ignored.
 *     This class cannot be instantiated — all methods are static.
 * </p>
 */
public class CSVReader {


    /**
     * Generic CSV reading method
     * @param filePath path to csv file
     * @return String Array containing all the read elements from a csv file.
     */
    public static List<String[]> read(String filePath) {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;

            boolean firstLine = true;
            while((line = br.readLine()) != null){
                if(firstLine){
                    firstLine = false;
                    continue;
                }
                if(!line.isBlank()){
                    rows.add(line.split(AppConstants.CSV_DELIMITER));
                }

            }
        } catch (IOException e){
            ConsolePrinter.printError("Error reading file: "+ filePath);
            e.printStackTrace();
        }
        return rows;
    }
}