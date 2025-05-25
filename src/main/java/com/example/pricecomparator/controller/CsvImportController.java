package com.example.pricecomparator.controller;

import com.example.pricecomparator.service.CsvImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("import/")
public class CsvImportController {
    private final CsvImportService csvImportService;

    public CsvImportController(CsvImportService csvImportService) {
        this.csvImportService = csvImportService;
    }

    @PostMapping
    public ResponseEntity<String> importCsvFile(@RequestParam("filename") String filename) {
        try {
            csvImportService.importCsv(filename);
            return ResponseEntity.ok("CSV imported successfully: " + filename);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to import CSV: " + e.getMessage());
        }
    }
}
