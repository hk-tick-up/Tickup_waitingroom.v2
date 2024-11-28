package com.example.waitingroom.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class YamlPropertyChecker implements CommandLineRunner {
    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Override
    public void run(String...args) throws Exception {
        System.out.println("Datasource URL:"+datasourceUrl);
    }
}
