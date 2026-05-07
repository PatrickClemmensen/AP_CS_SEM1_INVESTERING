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

}
