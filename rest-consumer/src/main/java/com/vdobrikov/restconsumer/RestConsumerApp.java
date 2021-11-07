package com.vdobrikov.restconsumer;

import com.vdobrikov.restconsumer.properties.KafkaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(KafkaProperties.class)
@SpringBootApplication(scanBasePackages = "com.vdobrikov.restconsumer")
public class RestConsumerApp {
    public static void main(String[] args) {
        SpringApplication.run(RestConsumerApp.class, args);
    }

}
