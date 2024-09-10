package com.ezliv.config;

import com.ezliv.domain.entities.Customer;
import com.ezliv.domain.exceptions.FirebaseException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Configuration
@Slf4j
public class FirebaseConfigs {
    @Value("${firebase.json.path}")
    private String firebasePath;

    @Bean
    List<Customer> customers() {
        List<Customer> customers = new ArrayList<>();
        Path dirPath = Paths.get(firebasePath);

        if (Files.exists(dirPath) && Files.isDirectory(dirPath)) {
            try (Stream<Path> stream = Files.list(dirPath)) {
                stream.filter(file -> file.getFileName().toString().endsWith(".json"))
                        .forEach(file -> {
                            String fileName = file.getFileName().toString();
                            String customerName = fileName.substring(0, fileName.lastIndexOf("."));
                            customers.add(new Customer(customerName));
                        });
            } catch (IOException e) {
                throw new FirebaseException("Error while reading files from directory", e);
            }
        }
        log.info("Initialized customers: {}", customers.stream().map(Customer::getName).toList());
        return customers;
    }

    @Bean
    public Map<String, FirebaseApp> firebaseConfig(List<Customer> customers) {
        Map<String, FirebaseApp> firebaseApps = new HashMap<>();
        customers.forEach(customer -> {
            try (FileInputStream serviceAccount = new FileInputStream(firebasePath + "/" + customer.getName() + ".json")) {
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                FirebaseApp.initializeApp(options, customer.getName());
                firebaseApps.put(customer.getName(), FirebaseApp.getInstance(customer.getName()));
                log.info("FirebaseApp initialized for customer: {}", customer.getName());
            } catch (FileNotFoundException e) {
                throw new FirebaseException("File not found", e);
            } catch (IOException e) {
                throw new FirebaseException("Error while reading file", e);
            }
        });
        return firebaseApps;
    }

}
