package com.vdobrikov.restconsumer.eventhandler;

import com.vdobrikov.commons.dto.Employee;
import com.vdobrikov.restconsumer.event.NewEmployeeEvent;
import com.vdobrikov.restconsumer.properties.KafkaProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class NewEmployeeEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewEmployeeEventHandler.class);

    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public NewEmployeeEventHandler(KafkaProperties kafkaProperties, KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaProperties = kafkaProperties;
        this.kafkaTemplate = kafkaTemplate;
    }

    @EventListener
    public void handle(NewEmployeeEvent event) {
        Employee employee = event.getValue();
        try {
            kafkaTemplate.send(kafkaProperties.getTopic(), employee);
            LOGGER.debug("Message sent: message={}", employee);
        } catch (Exception e) {
            LOGGER.error("Failed to send message: message={}", employee, e);
        }
    }
}
