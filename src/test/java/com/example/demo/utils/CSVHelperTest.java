package com.example.demo.utils;

import com.example.demo.domain.Trade;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVHelperTest {

    private final CSVHelper csvHelper = new CSVHelper();

    @Test
    void givenInvalidDate_whenValidated_thenDiscardRow() throws IOException {
        Reader reader = new FileReader("./src/main/resources/trade_invalid_dates.csv");
        CSVParser csvParser = csvHelper.parseReaderIntoCSVParser(reader);
        List<Trade> enrichedTrades = csvHelper.enrichTradeRecords(csvParser);
        assertEquals(0, enrichedTrades.size());
    }

    @Test
    void givenInvalidProductId_whenValidated_thenReplaceWithMissingProductName() throws IOException {
        Reader reader = new FileReader("./src/main/resources/trade_invalid_product_id.csv");
        CSVParser csvParser = csvHelper.parseReaderIntoCSVParser(reader);
        List<Trade> enrichedTrades = csvHelper.enrichTradeRecords(csvParser);
        assertEquals("Missing Product Name", enrichedTrades.get(0).getProductName());
    }

    @Test
    void givenValidData_whenValidated_thenEnrichRow() throws IOException {
        Reader reader = new FileReader("./src/main/resources/trade_valid.csv");
        CSVParser csvParser = csvHelper.parseReaderIntoCSVParser(reader);
        List<Trade> enrichedTrades = csvHelper.enrichTradeRecords(csvParser);
        assertEquals("Treasury Bills Domestic", enrichedTrades.get(0).getProductName());
        assertEquals("REPO Domestic", enrichedTrades.get(1).getProductName());
    }
}