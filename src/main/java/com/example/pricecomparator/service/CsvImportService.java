package com.example.pricecomparator.service;

import com.example.pricecomparator.model.Discount;
import com.example.pricecomparator.model.Product;
import com.example.pricecomparator.model.StoreOffer;
import com.example.pricecomparator.repository.DiscountRepository;
import com.example.pricecomparator.repository.ProductRepository;
import com.example.pricecomparator.repository.StoreOfferRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CsvImportService {
    @Value("${app.csv-directory}")
    private String csvDirectory;

    private final ProductRepository productRepo;
    private final StoreOfferRepository offerRepo;
    private final DiscountRepository discountRepo;

    public CsvImportService(ProductRepository productRepo, StoreOfferRepository offerRepo, DiscountRepository discountRepo) {
        this.productRepo = productRepo;
        this.offerRepo = offerRepo;
        this.discountRepo = discountRepo;
    }

    @PostConstruct
    public void importCsvsAtStartup() {
        File dir = new File(csvDirectory);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".csv"));
        if (files == null) return;

        for (File file : files) {
            try {
                if (file.getName().contains("discount")) {
                    importDiscountCsv(file);
                } else {
                    importProductAndOfferCsv(file);
                }
            } catch (Exception e) {
                System.err.println("Failed to import: " + file.getName() + ": " + e.getMessage());
            }
        }
    }

    private void importProductAndOfferCsv(File file) throws IOException {
        String store = extractStoreFromFilename(file.getName());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(";");
                if (tokens.length < 8) continue;

                String productId = tokens[0];
                Product product = productRepo.findById(productId).orElseGet(() -> {
                    Product p = new Product();
                    p.setId(productId);
                    p.setName(tokens[1]);
                    p.setCategory(tokens[2]);
                    p.setBrand(tokens[3]);
                    p.setPackageQuantity(Double.parseDouble(tokens[4]));
                    p.setPackageUnit(tokens[5]);
                    return productRepo.save(p);
                });

                StoreOffer offer = new StoreOffer();
                offer.setProduct(product);
                offer.setStore(store);
                offer.setPrice(Double.parseDouble(tokens[6]));
                offer.setCurrency(tokens[7]);
                offer.setDate(extractDateFromFilename(file.getName()));
                offerRepo.save(offer);
            }
        }
    }

    private void importDiscountCsv(File file) throws IOException {
        String store = extractStoreFromFilename(file.getName());
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(";");
                if (tokens.length < 9) continue;

                String productId = tokens[0];
                Product product = productRepo.findById(productId).orElseGet(() -> {
                    Product p = new Product();
                    p.setId(productId);
                    p.setName(tokens[1]);
                    p.setCategory(tokens[2]);
                    p.setBrand(tokens[3]);
                    p.setPackageQuantity(Double.parseDouble(tokens[4]));
                    p.setPackageUnit(tokens[5]);
                    return productRepo.save(p);
                });

                Discount discount = new Discount();
                discount.setProduct(product);
                discount.setStore(store);
                discount.setFromDate(LocalDate.parse(tokens[6]));
                discount.setToDate(LocalDate.parse(tokens[7]));
                discount.setPercentageOfDiscount(Integer.parseInt(tokens[8]));
                discountRepo.save(discount);
            }
        }
    }

    private String extractStoreFromFilename(String filename) {
        return filename.split("_")[0];
    }

    private LocalDate extractDateFromFilename(String filename) {
        Pattern pattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2})");
        Matcher matcher = pattern.matcher(filename);
        if (matcher.find()) {
            try {
                return LocalDate.parse(matcher.group(1), DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                System.err.println("Invalid date format in filename: " + filename);
            }
        }
        return LocalDate.now();
    }
}
