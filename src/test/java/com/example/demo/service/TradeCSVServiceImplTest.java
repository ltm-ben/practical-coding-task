package com.example.demo.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TradeCSVServiceImplTest {

    @Test
    void method() throws IOException {

        Map<String, String> PRODUCT_ID_NAME_MAP = new HashMap<>() {
            {
                put("1", "Treasury Bills Domestic");
                put("2", "Corporate Bonds Domestic");
                put("3", "REPO Domestic");
                put("4", "Interest rate swaps International");
                put("5", "OTC Index Option");
                put("6", "Currency Options");
                put("7", "Reverse Repos International");
                put("8", "REPO International");
                put("9", "766A_CORP BD");
                put("10", "766B_CORP BD");
            }
        };
        String[] HEADERS = {"product_id", "product_name"};
        Reader in = new FileReader("./src/main/resources/product.csv");
        CSVParser records = CSVFormat.Builder.create()
                .setHeader(HEADERS)
                .setSkipHeaderRecord(true)
                .build()
                .parse(in);
        for (CSVRecord record : records) {
            String productId = record.get("product_id");
            String productName = record.get("product_name");
            assertEquals(PRODUCT_ID_NAME_MAP.get(productId), productName);
        }
    }
}