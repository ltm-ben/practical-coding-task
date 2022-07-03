package com.example.demo.service;

import com.example.demo.domain.Trade;
import com.example.demo.utils.CSVHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.Reader;
import java.util.List;

@Service
@Slf4j
public class TradeCSVServiceImpl implements TradeCSVService {
    private final CSVHelper csvHelper = new CSVHelper();

    @Override
    public ByteArrayInputStream enrichCSV(MultipartFile file) {
        Reader fileReader = CSVHelper.getReaderFromFile(file);
        CSVParser csvParser = csvHelper.parseReaderIntoCSVParser(fileReader);
        List<Trade> enrichedTrades = csvHelper.enrichTradeRecords(csvParser);
        return CSVHelper.convertEnrichedTradeRecordsToCSV(enrichedTrades);
    }
}
