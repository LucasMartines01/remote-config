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

    @Value("${customer}")
    private String customer;


    @Bean
    public FirebaseApp firebaseConfig() {

        try (FileInputStream serviceAccount = new FileInputStream(firebasePath + customer + ".json")) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            log.info("FirebaseApp initialized for customer: {}", customer);
            return FirebaseApp.initializeApp(options, customer);
        } catch (FileNotFoundException e) {
            throw new FirebaseException("File not found", e);
        } catch (IOException e) {
            throw new FirebaseException("Error while reading file", e);
        }
    }
}
