package com.vdobrikov.kafkaconsumer;

import com.vdobrikov.kafkaconsumer.properties.KafkaProperties;
import com.vdobrikov.kafkaconsumer.properties.ProcessingProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.listener.LoggingErrorHandler;

@EnableKafka
@EnableConfigurationProperties({KafkaProperties.class, ProcessingProperties.class})
@SpringBootApplication(scanBasePackages = "com.vdobrikov.kafkaconsumer")
public class KafkaConsumerApp {
    public static void main(String[] args) {
        SpringApplication.run(KafkaConsumerApp.class, args);
    }

    @Bean
    public LoggingErrorHandler errorHandler() {
        return new LoggingErrorHandler();
    }
}
