package com.vdobrikov.kafkaconsumer.consumer;

import com.vdobrikov.commons.dto.Employee;
import com.vdobrikov.kafkaconsumer.processor.EmployeeProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EmployeeConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeConsumer.class);

    private final EmployeeProcessor employeeProcessor;

    public EmployeeConsumer(EmployeeProcessor employeeProcessor) {
        this.employeeProcessor = employeeProcessor;
    }

    @KafkaListener(topics = "${kafka.topic}")
    public void consume(Employee employee) {
        LOGGER.info("Got new employee: employee={}", employee);

        employeeProcessor.process(employee);
    }
}
