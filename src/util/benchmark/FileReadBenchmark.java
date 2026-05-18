package util.benchmark;

import util.AppConstants;
import util.csv.CSVReader;
import util.printing.ConsolePrinter;
import java.io.*;

public class FileReadBenchmark {

    public static void run(String filePath, int runs) {
        long bufferedTime   = readBuffered(filePath, runs);
        long unbufferedTime = readUnbuffered(filePath, runs);

        ConsolePrinter.printMenuHeader("\n=== FILE READING BENCHMARK: " + filePath + " ===");
        System.out.printf("  Buffered   (avg over %d runs): %,d ns%n", runs, bufferedTime);
        System.out.printf("  Unbuffered (avg over %d runs): %,d ns%n", runs, unbufferedTime);
        System.out.printf("  Difference:                    %,d ns%n", unbufferedTime - bufferedTime);
        ConsolePrinter.printSeparator();
    }

    private static long readBuffered(String filePath, int runs) {
        long total = 0;

        for (int i = 0; i < runs; i++) {
            long start = System.nanoTime();
            CSVReader.read(filePath); // uses the actual production implementation
            total += System.nanoTime() - start;
        }

        return total / runs;
    }

    private static long readUnbuffered(String filePath, int runs) {
        long total = 0;

        for (int i = 0; i < runs; i++) {
            long start = System.nanoTime();
            try (FileReader fr = new FileReader(filePath)) {
                StringBuilder sb = new StringBuilder();
                int c;
                while ((c = fr.read()) != -1) {
                    if ((char) c == '\n') {
                        String line = sb.toString().trim();
                        if (!line.isBlank()) line.split(AppConstants.CSV_DELIMITER);
                        sb.setLength(0);
                    } else {
                        sb.append((char) c);
                    }
                }
            } catch (IOException e) {
                ConsolePrinter.printError("Benchmark error (unbuffered): " + e.getMessage());
            }
            total += System.nanoTime() - start;
        }

        return total / runs;
    }
}