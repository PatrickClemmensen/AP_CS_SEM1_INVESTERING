package util.csv;

import util.AppConstants;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    public static List<String[]> read(String filePath) {
        // TODO: open the file at filePath
        // TODO: skip the header line
        // TODO: split each remaining line by ";" and add to a list
        // TODO: handle IOException
        // Hint: use BufferedReader and FileReader
        // Hint: Danish decimals use comma — remember to replace "," with "." before parsing doubles
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                if (!line.isBlank()) {
                    rows.add(line.split(AppConstants.CSV_DELIMITER));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            e.printStackTrace();
        }

        return rows;
    }
}