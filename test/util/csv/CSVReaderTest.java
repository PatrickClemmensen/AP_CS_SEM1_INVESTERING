package util.csv;

import org.junit.jupiter.api.*;
import util.csv.CSVReader;
import interfaces.CSVSerializable;

import java.io.*;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CSVReaderTest {

    private static final String TEST_FILE = "test/resources/users_test.csv";

    @Test
    void testReadReturnsCorrectNumberOfRows() throws IOException {
        List<String[]> rows = CSVReader.read(TEST_FILE);

        assertEquals(2, rows.size());
    }

    @Test
    void testReadSkipsHeader() throws IOException {
        List<String[]> rows = CSVReader.read(TEST_FILE);
        assertEquals("1", rows.get(0)[0]);
        assertNotEquals("user_id", rows.get(0)[0]);
    }

    @Test
    void testReadHeaderOnlyPasses() throws IOException {
        String TEST_FILE = "test/resources/onlyHeader_test.csv";
        List<String[]> rows = CSVReader.read(TEST_FILE);
        assertEquals(0, rows.size());
    }

}
