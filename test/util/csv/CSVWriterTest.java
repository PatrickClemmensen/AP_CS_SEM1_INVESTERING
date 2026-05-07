package util.csv;

import org.junit.jupiter.api.*;
import util.csv.CSVWriter;
import interfaces.CSVSerializable;

import java.io.*;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CSVWriterTest {

    private static final String TEST_FILE = "test/resources/test_output.csv";
    private static final String HEADER = "id;name;value";

    // simple helper class to test with
    static class TestItem implements CSVSerializable {
        private final String line;
        public TestItem(String line) { this.line = line; }

        @Override
        public String toCSVLine() { return line; }
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(Path.of(TEST_FILE));
    }

    @Test
    void testWriteOverwritesFileWithHeader() throws IOException {
        List<TestItem> items = List.of(new TestItem("1;Alice;100"), new TestItem("2;Bob;200"));

        CSVWriter.write(TEST_FILE, items, HEADER, false);

        List<String> lines = Files.readAllLines(Path.of(TEST_FILE));
        assertEquals(HEADER, lines.get(0));
        assertEquals("1;Alice;100", lines.get(1));
        assertEquals("2;Bob;200", lines.get(2));
    }

    @Test
    void testWriteAppendsWithoutHeader() throws IOException {
        // write initial file
        CSVWriter.write(TEST_FILE, List.of(new TestItem("1;Alice;100")), HEADER, false);

        // append a new item
        CSVWriter.write(TEST_FILE, List.of(new TestItem("2;Bob;200")), null, true);

        List<String> lines = Files.readAllLines(Path.of(TEST_FILE));
        assertEquals(3, lines.size());
        assertEquals(HEADER, lines.get(0));
        assertEquals("1;Alice;100", lines.get(1));
        assertEquals("2;Bob;200", lines.get(2));
    }

    @Test
    void testWriteOverwritesExistingContent() throws IOException {
        //Write initial file
        CSVWriter.write(TEST_FILE, List.of(new TestItem("1;Alice;100")), HEADER, false);

        // Overwrite with different content
        CSVWriter.write(TEST_FILE, List.of(new TestItem("2;Bob;200")), HEADER, false);

        List<String> lines = Files.readAllLines(Path.of(TEST_FILE));
        assertEquals(2, lines.size());
        assertEquals("2;Bob;200", lines.get(1));
    }

    @Test
    void testWriteWithEmptyList() throws IOException {
        CSVWriter.write(TEST_FILE, List.of(), HEADER,false);

        List<String> lines = Files.readAllLines(Path.of(TEST_FILE));
        assertEquals(1, lines.size());
        assertEquals(HEADER,lines.get(0));
    }
}