package com.vdobrikov.kafkaconsumer.processor.subprocessor;

import com.vdobrikov.kafkaconsumer.model.Employee;
import com.vdobrikov.kafkaconsumer.properties.ProcessingProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class WageEmployeeSubProcessor implements EmployeeSubProcessor {
    private static final float PERCENT_100 = 100f;
    private final ProcessingProperties processingProperties;

    public WageEmployeeSubProcessor(ProcessingProperties processingProperties) {
        this.processingProperties = processingProperties;
    }

    /**
     * Processing employee by updating raw wage with taxed one
     * @param employee employee to process
     * @return employee with taxed wage
     */
    @Override
    public Employee process(Employee employee) {
        Objects.requireNonNull(employee, "'employee' cannot be null");
        employee.setWage(employee.getWage() + employee.getWage() * processingProperties.getTaxPercent() / PERCENT_100);
        return employee;
    }
}
