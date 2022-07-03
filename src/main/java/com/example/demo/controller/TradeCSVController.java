package com.example.demo.controller;

import com.example.demo.service.TradeCSVService;
import com.example.demo.utils.CSVHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TradeCSVController {

    @Autowired
    private TradeCSVService tradeCSVService;

    @PostMapping(value = "/api/v1/enrich")
    public ResponseEntity<Resource> enrichCSV(@RequestParam("file")MultipartFile fileRequest) {
        if (CSVHelper.hasCSVFormat(fileRequest)) {
            String filename = "enriched_trade.csv";
            InputStreamResource fileResponse = new InputStreamResource(tradeCSVService.enrichCSV(fileRequest));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.parseMediaType("application/csv"))
                    .body(fileResponse);
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
    }
}