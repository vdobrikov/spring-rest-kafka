package com.vdobrikov.restconsumer.eventhandler;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.vdobrikov.commons.dto.Employee;
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

import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewEmployeeEventHandlerTest {
    private static final String TEST_TOPIC = "test-topic";
    private static final Employee TEST_EMPLOYEE = new Employee("Jane", "Doe", 100500.50f, ZonedDateTime.now());

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
        newEmployeeEventHandler.handle(new NewEmployeeEvent(TEST_EMPLOYEE));

        verify(kafkaTemplate).send(eq(TEST_TOPIC), eq(TEST_EMPLOYEE));
    }

    @Test
    void testHandleError() {
        Logger root = (Logger) LoggerFactory.getLogger(NewEmployeeEventHandler.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        root.addAppender(listAppender);

        doThrow(new RuntimeException()).when(kafkaTemplate).send(TEST_TOPIC, TEST_EMPLOYEE);
        newEmployeeEventHandler.handle(new NewEmployeeEvent(TEST_EMPLOYEE));

        List<ILoggingEvent> logsList = listAppender.list;
        Assertions.assertThat(logsList)
                .extracting(ILoggingEvent::getMessage, ILoggingEvent::getLevel)
                .containsExactly(Tuple.tuple("Failed to send message: message={}", Level.ERROR));
    }
}