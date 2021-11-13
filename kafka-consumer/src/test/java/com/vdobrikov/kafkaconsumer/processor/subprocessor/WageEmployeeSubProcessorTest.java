package com.vdobrikov.kafkaconsumer.processor.subprocessor;

import com.vdobrikov.kafkaconsumer.model.Employee;
import com.vdobrikov.kafkaconsumer.properties.ProcessingProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WageEmployeeSubProcessorTest {

    @Mock
    private ProcessingProperties processingProperties;

    @InjectMocks
    private WageEmployeeSubProcessor subProcessor;

    @Test
    void testProcess() {
        when(processingProperties.getTaxPercent()).thenReturn(10f);
        OffsetDateTime now = OffsetDateTime.now();
        Employee employee = new Employee("Jane", "Doe", 100f, now);

        subProcessor.process(employee);

        assertThat(employee).isNotNull()
                .hasFieldOrPropertyWithValue("name", "Jane")
                .hasFieldOrPropertyWithValue("surname", "Doe")
                .hasFieldOrPropertyWithValue("wage", 110.0f)
                .hasFieldOrPropertyWithValue("eventTime", now);
    }

    @Test
    void testProcessNullValue() {
        assertThatThrownBy(() -> subProcessor.process(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("'employee' cannot be null");
    }
}