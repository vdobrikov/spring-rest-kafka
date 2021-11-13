package com.vdobrikov.kafkaconsumer.consumer;

import com.vdobrikov.kafkaconsumer.processor.EmployeeProcessor;
import com.vdobrikov.model.EmployeeDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmployeeConsumerTest {

    @Mock
    private EmployeeProcessor employeeProcessor;

    @InjectMocks
    private EmployeeConsumer employeeConsumer;

    @Test
    void testConsume() {
        EmployeeDto employee = new EmployeeDto();
        employee.setName("Jane");
        employee.setSurname("Doe");
        employee.setWage(100500.5f);
        employee.setEventTime(OffsetDateTime.now());

        employeeConsumer.consume(employee);

        verify(employeeProcessor).process(employee);
    }
}