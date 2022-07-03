package com.example.demo.utils;

import com.example.demo.domain.Trade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class CSVHelper {
    private static final String TYPE = "text/csv";
    private static final String DATE = "date";
    private static final String PRODUCT_ID = "product_id";
    private static final String PRODUCT_NAME = "product_name";
    private static final String CURRENCY = "currency";
    private static final String PRICE = "price";
    private static final String[] PRODUCT_HEADERS = {PRODUCT_ID, PRODUCT_NAME};
    private static final String[] TRADE_HEADERS = {DATE, PRODUCT_ID, CURRENCY, PRICE};
    private static final String DATA_FILE = "./src/main/resources/product.csv";
    private final DateValidator dateValidator;
    private MapWrapper productIdNameMap;

    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public CSVHelper() {
        initializeStaticData();
        dateValidator = new DateValidatorUsingDateFormat("yyyyMMdd");
    }

    private void initializeStaticData() {
        try {
            Reader in = new FileReader(DATA_FILE);
            CSVParser productRecords = CSVFormat.Builder.create()
                    .setHeader(PRODUCT_HEADERS)
                    .build()
                    .parse(in);
            Map<String, String> result = new HashMap<>();
            for (CSVRecord record : productRecords) {
                result.put(record.get(PRODUCT_ID), record.get(PRODUCT_NAME));
            }
            this.productIdNameMap = new MapWrapperImpl(result);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize data from " + DATA_FILE + ": " + e.getMessage());
        }
    }

    public static Reader getReaderFromFile(MultipartFile fileRequest) {
        try {
            return new BufferedReader(new InputStreamReader(fileRequest.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Failed to get Reader from file: " + e.getMessage());
        }
    }

    public CSVParser parseReaderIntoCSVParser(Reader reader) {
        try {
            return CSVFormat.Builder.create()
                    .setHeader(CSVHelper.TRADE_HEADERS)
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(reader);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse input to trade records: " + e.getMessage());
        }
    }

    public List<Trade> enrichTradeRecords(CSVParser csvParser) {
        List<Trade> result = new ArrayList<>();
        for (CSVRecord tradeRecord : csvParser) {
            try {
                Trade enrichedTrade = new Trade();
                enrichedTrade.setDate(validateDate(tradeRecord.get(DATE)));
                enrichedTrade.setProductName(mapProductIdToProductName(tradeRecord.get(PRODUCT_ID)));
                enrichedTrade.setCurrency(tradeRecord.get(CURRENCY));
                enrichedTrade.setPrice(tradeRecord.get(PRICE));
                result.add(enrichedTrade);
            } catch (RuntimeException e) {
                log.error("Discarding trade record: {}", e.getMessage());
            }
        }
        return result;
    }

    private String validateDate(String dateToBeValidated) {
        if (dateValidator.isValid(dateToBeValidated)) {
            return dateToBeValidated;
        } else {
            throw new RuntimeException("Invalid date: " + dateToBeValidated);
        }
    }

    private String mapProductIdToProductName(String productId) {
        String missingProductName = "Missing Product Name";
        try {
            return productIdNameMap.getValueUsingKey(productId);
        } catch (RuntimeException e) {
            log.error("Could not find a mapping for product ID: " + productId);
            return missingProductName;
        }
    }

    public static ByteArrayInputStream convertEnrichedTradeRecordsToCSV(List<Trade> trades) {
        final CSVFormat format = CSVFormat
                .DEFAULT
                .withQuoteMode(QuoteMode.MINIMAL)
                .withHeader(TRADE_HEADERS);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            for (Trade trade : trades) {
                List<String> data = Arrays.asList(
                        trade.getDate(),
                        trade.getProductName(),
                        trade.getCurrency(),
                        trade.getPrice()
                );
                csvPrinter.printRecord(data);
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to convert trade records to CSV file: " + e.getMessage());
        }
    }
}