package util.benchmark;

import util.AppConstants;
import util.csv.CSVReader;
import util.printing.ConsolePrinter;
import java.io.*;

/**
 * Benchmarks and compares the performance of buffered vs unbuffered CSV file reading.
 * <p>
 *     Runs ach reading strategy a given number of times, calculates the average
 *     CPU time in nanoseconds, and prints a formatted comparison to the console.
 *     Used to demonstrate the performance benefits of buffered I/O.
 * </p>
 */
public class FileReadBenchmark {

    /**
     * Runs the benchmark for the given file and prints the result to the console.
     * <p>
     *     Both buffered and unbuffered strategies are each run {@code runs} times,
     *     and the average execution time is reported for each along with the difference.
     * </p>
     *
     * @param filePath  the path to the CSV file to benchmark
     * @param runs      the number of times each strategy is run to calculate the average
     */
    public static void run(String filePath, int runs) {
        long bufferedTime   = readBuffered(filePath, runs);
        long unbufferedTime = readUnbuffered(filePath, runs);

        ConsolePrinter.printMenuHeader("\n=== FILE READING BENCHMARK: " + filePath + " ===");
        System.out.printf("  Buffered   (avg over %d runs): %,d ns%n", runs, bufferedTime);
        System.out.printf("  Unbuffered (avg over %d runs): %,d ns%n", runs, unbufferedTime);
        System.out.printf("  Difference:                    %,d ns%n", unbufferedTime - bufferedTime);
        ConsolePrinter.printSeparator();
    }


    /**
     * Measures the average time to read a CSV file using buffered I/O.
     * <p>
     *     Delegates to {@link CSVReader#read(String)}, which uses a {@link java.io.BufferedReader}
     *     internally. Reading in large chunks reduces the number of system calls made.
     * </p>
     *
     * @param filePath  the path to the CSV file to read
     * @param runs      the number of times to repeat the read
     * @return          the average execution time in nanoseconds
     */
    private static long readBuffered(String filePath, int runs) {
        long total = 0;

        for (int i = 0; i < runs; i++) {
            long start = System.nanoTime();
            CSVReader.read(filePath); // uses the actual production implementation
            total += System.nanoTime() - start;
        }

        return total / runs;
    }

    /**
     * Measures the average time to read a CSV file using unbuffered I/O.
     * <p>
     *     Reads the file one character at a time using a plain {@link FileReader},
     *     making a separate system call for each character. This is significantly
     *     slower than buffered reading, especially for large files.
     * </p>
     *
     * @param filePath  the path to the CSV file to read
     * @param runs      the number of times to repeat the read
     * @return          the average execution time in nanoseconds
     */
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