package com.vdobrikov.kafkaconsumer.processor;


import com.vdobrikov.model.EmployeeDto;

public interface EmployeeProcessor {
    com.vdobrikov.kafkaconsumer.model.Employee process(EmployeeDto employeeDto);
}
