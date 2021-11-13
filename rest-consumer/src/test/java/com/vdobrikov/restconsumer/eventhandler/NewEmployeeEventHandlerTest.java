package com.vdobrikov.restconsumer.eventhandler;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.vdobrikov.model.EmployeeDto;
import com.vdobrikov.restconsumer.event.NewEmployeeEvent;
import com.vdobrikov.restconsumer.properties.KafkaProperties;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewEmployeeEventHandlerTest {
    private static final String TEST_TOPIC = "test-topic";

    @Mock
    private KafkaProperties kafkaProperties;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private NewEmployeeEventHandler newEmployeeEventHandler;

    @BeforeEach
    void setUp() {
        when(kafkaProperties.getTopic()).thenReturn(TEST_TOPIC);
    }

    @Test
    void testHandleEvent() {
        EmployeeDto employee = createTestEmployee();

        newEmployeeEventHandler.handle(new NewEmployeeEvent(employee));

        verify(kafkaTemplate).send(eq(TEST_TOPIC), eq(employee));
    }

    @Test
    void testHandleError() {
        Logger root = (Logger) LoggerFactory.getLogger(NewEmployeeEventHandler.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        root.addAppender(listAppender);

        EmployeeDto employee = createTestEmployee();

        doThrow(new RuntimeException()).when(kafkaTemplate).send(TEST_TOPIC, employee);
        newEmployeeEventHandler.handle(new NewEmployeeEvent(employee));

        List<ILoggingEvent> logsList = listAppender.list;
        Assertions.assertThat(logsList)
                .extracting(ILoggingEvent::getMessage, ILoggingEvent::getLevel)
                .containsExactly(Tuple.tuple("Failed to send message: message={}", Level.ERROR));
    }

    private EmployeeDto createTestEmployee() {
        EmployeeDto employee = new EmployeeDto();
        employee.setName("Jane");
        employee.setSurname("Doe");
        employee.setWage(100500.5f);
        employee.setEventTime(OffsetDateTime.now());
        return employee;
    }
}