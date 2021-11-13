package com.vdobrikov.kafkaconsumer.processor;

import com.vdobrikov.kafkaconsumer.model.Employee;
import com.vdobrikov.kafkaconsumer.processor.subprocessor.EmployeeSubProcessor;
import com.vdobrikov.kafkaconsumer.service.EmployeeService;
import com.vdobrikov.model.EmployeeDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmployeeProcessorImplTest {

    @Mock
    private EmployeeService employeeService;

    private EmployeeProcessorImpl employeeProcessor;

    @Captor
    private ArgumentCaptor<com.vdobrikov.kafkaconsumer.model.Employee> employeeCaptor;

    @Test
    void testProcessNoSubprocessors() {
        employeeProcessor = new EmployeeProcessorImpl(employeeService, Collections.emptyList());
        EmployeeDto employeeDto = createEmployee();

        employeeProcessor.process(employeeDto);

        verify(employeeService).save(employeeCaptor.capture());
        Employee savedEmployee = employeeCaptor.getValue();
        assertThat(savedEmployee).isNotNull()
                .hasFieldOrPropertyWithValue("name", employeeDto.getName())
                .hasFieldOrPropertyWithValue("surname", employeeDto.getSurname())
                .hasFieldOrPropertyWithValue("wage", 100f)
                .hasFieldOrPropertyWithValue("eventTime", employeeDto.getEventTime());
    }

    @Test
    void testProcessWithSubprocessor() {
        EmployeeDto employeeDto = createEmployee();
        EmployeeSubProcessor subProcessor = createSubprocessor(0, e -> {
            e.setName(e.getName() + "_subprocessed");
            return e;
        });
        employeeProcessor = new EmployeeProcessorImpl(employeeService, Collections.singleton(subProcessor));

        employeeProcessor.process(employeeDto);

        verify(employeeService).save(employeeCaptor.capture());
        Employee savedEmployee = employeeCaptor.getValue();
        assertThat(savedEmployee).isNotNull()
                .hasFieldOrPropertyWithValue("name", employeeDto.getName() + "_subprocessed")
                .hasFieldOrPropertyWithValue("surname", employeeDto.getSurname())
                .hasFieldOrPropertyWithValue("wage", 100f)
                .hasFieldOrPropertyWithValue("eventTime", employeeDto.getEventTime());
    }

    @Test
    void testProcessWithOrderedSubprocessors() {
        EmployeeDto employeeDto = createEmployee();
        EmployeeSubProcessor firstSubProcessor = createSubprocessor(1, e -> {
            e.setName(e.getName() + "_1");
            return e;
        });
        EmployeeSubProcessor secondSubProcessor = createSubprocessor(2, e -> {
            e.setName(e.getName() + "_2");
            return e;
        });
        EmployeeSubProcessor thirdSubProcessor = createSubprocessor(3, e -> {
            e.setName(e.getName() + "_3");
            return e;
        });
        Collection<EmployeeSubProcessor> subProcessors = new ArrayList<>();
        // Add in unordered way
        subProcessors.add(secondSubProcessor);
        subProcessors.add(firstSubProcessor);
        subProcessors.add(thirdSubProcessor);
        employeeProcessor = new EmployeeProcessorImpl(employeeService, subProcessors);

        employeeProcessor.process(employeeDto);

        verify(employeeService).save(employeeCaptor.capture());
        com.vdobrikov.kafkaconsumer.model.Employee savedEmployee = employeeCaptor.getValue();
        assertThat(savedEmployee.getName())
                .endsWith("_1_2_3");
    }

    @Test
    void testProcessWithSameOrderedSubprocessors() {
        EmployeeDto employeeDto = createEmployee();
        EmployeeSubProcessor firstSubProcessor = createSubprocessor(1, e -> {
            e.setName(e.getName() + "_1");
            return e;
        });
        EmployeeSubProcessor secondSubProcessor = createSubprocessor(1, e -> {
            e.setName(e.getName() + "_2");
            return e;
        });
        Collection<EmployeeSubProcessor> subProcessors = new ArrayList<>();
        subProcessors.add(secondSubProcessor);
        subProcessors.add(firstSubProcessor);
        employeeProcessor = new EmployeeProcessorImpl(employeeService, subProcessors);

        employeeProcessor.process(employeeDto);

        verify(employeeService).save(employeeCaptor.capture());
        com.vdobrikov.kafkaconsumer.model.Employee savedEmployee = employeeCaptor.getValue();
        assertThat(savedEmployee.getName())
                .contains("_1")
                .contains("_2");
    }

    private EmployeeSubProcessor createSubprocessor(int order, Function<Employee, Employee> function) {
        return new EmployeeSubProcessor() {
            @Override
            public Employee process(Employee employee) {
                return function.apply(employee);
            }

            @Override
            public int getOrder() {
                return order;
            }
        };
    }

    @Test
    void testProcessNullValue() {
        employeeProcessor = new EmployeeProcessorImpl(employeeService, Collections.emptyList());

        assertThatThrownBy(() -> employeeProcessor.process(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("'employeeDto' cannot be null");
    }

    private EmployeeDto createEmployee() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setName("Jane");
        employeeDto.setSurname("Doe");
        employeeDto.setWage(100f);
        employeeDto.setEventTime(OffsetDateTime.now());
        return employeeDto;
    }
}