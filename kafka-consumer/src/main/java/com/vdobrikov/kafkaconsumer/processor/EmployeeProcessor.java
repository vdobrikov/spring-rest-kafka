package com.vdobrikov.kafkaconsumer.processor;

import com.vdobrikov.commons.dto.Employee;

public interface EmployeeProcessor {
    com.vdobrikov.kafkaconsumer.model.Employee process(Employee employeeDto);
}
