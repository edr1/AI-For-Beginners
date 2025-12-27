package com.example.nifi;

import com.example.nifi.config.NifiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(NifiProperties.class)
public class NifiMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NifiMicroserviceApplication.class, args);
    }
}
