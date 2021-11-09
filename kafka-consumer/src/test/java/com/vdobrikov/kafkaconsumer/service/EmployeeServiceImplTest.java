package com.vdobrikov.kafkaconsumer.service;

import com.vdobrikov.kafkaconsumer.model.Employee;
import com.vdobrikov.kafkaconsumer.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Captor
    private ArgumentCaptor<Pageable> pageableCaptor;

    @Test
    void testSave() {
        Employee employee = new Employee("Jane", "Doe", 100500f, ZonedDateTime.now());

        employeeService.save(employee);

        verify(employeeRepository).save(employee);
    }

    @Test
    void testSaveNullValue() {
        assertThatThrownBy(() -> employeeService.save(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("'employee' cannot be null");
    }

    @Test
    void testFindAll() {
        Employee employee = new Employee("Jane", "Doe", 100500f, ZonedDateTime.now());

        when(employeeRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(employee)));

        Page<Employee> page = employeeService.findAll(1, 42);

        assertThat(page)
                .isNotNull()
                .containsExactly(employee);

        verify(employeeRepository).findAll(pageableCaptor.capture());
        assertThat(pageableCaptor.getValue())
                .isNotNull()
                .hasFieldOrPropertyWithValue("pageNumber", 1)
                .hasFieldOrPropertyWithValue("size", 42);
    }

    @Test
    void testFindAllNegativePageNum() {
        assertThatThrownBy(() -> employeeService.findAll(-1, 42))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("'pageNum' cannot be negative");
    }

    @Test
    void testFindAllNegativePageSize() {
        assertThatThrownBy(() -> employeeService.findAll(1, -42))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("'pageSize' cannot be negative or 0");
    }

    @Test
    void testFindAllZeroPageSize() {
        assertThatThrownBy(() -> employeeService.findAll(1, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("'pageSize' cannot be negative or 0");
    }
}