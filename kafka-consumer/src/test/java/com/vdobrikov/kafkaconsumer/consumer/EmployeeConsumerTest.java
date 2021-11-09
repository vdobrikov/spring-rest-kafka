package com.vdobrikov.kafkaconsumer.consumer;

import com.vdobrikov.commons.dto.Employee;
import com.vdobrikov.kafkaconsumer.processor.EmployeeProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmployeeConsumerTest {

    @Mock
    private EmployeeProcessor employeeProcessor;

    @InjectMocks
    private EmployeeConsumer employeeConsumer;

    @Test
    void testConsume() {
        Employee employee = new Employee("Jane", "Doe", 100500.50f, ZonedDateTime.now());

        employeeConsumer.consume(employee);

        verify(employeeProcessor).process(employee);
    }
}