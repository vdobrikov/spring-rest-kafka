package com.vdobrikov.kafkaconsumer.processor;

import com.vdobrikov.kafkaconsumer.model.Employee;
import com.vdobrikov.kafkaconsumer.processor.subprocessor.EmployeeSubProcessor;
import com.vdobrikov.kafkaconsumer.service.EmployeeService;
import com.vdobrikov.model.EmployeeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class EmployeeProcessorImpl implements EmployeeProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeProcessorImpl.class);

    private final EmployeeService employeeService;
    private final List<EmployeeSubProcessor> subProcessors;

    public EmployeeProcessorImpl(EmployeeService employeeService, Collection<EmployeeSubProcessor> subProcessors) {
        this.employeeService = employeeService;
        this.subProcessors = new ArrayList<>(subProcessors);
        Collections.sort(this.subProcessors);
    }

    @Override
    public Employee process(EmployeeDto employeeDto) {
        Objects.requireNonNull(employeeDto, "'employeeDto' cannot be null");

        Employee employee = new Employee(
                employeeDto.getName(),
                employeeDto.getSurname(),
                employeeDto.getWage(),
                employeeDto.getEventTime()
        );

        for (EmployeeSubProcessor subProcessor : subProcessors) {
            employee = subProcessor.process(employee);
        }
        employee = employeeService.save(employee);
        LOGGER.info("Saved processed employee: employee={}", employee);
        return employee;
    }
}
