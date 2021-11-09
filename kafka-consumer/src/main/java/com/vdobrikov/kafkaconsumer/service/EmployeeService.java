package com.vdobrikov.kafkaconsumer.service;

import com.vdobrikov.kafkaconsumer.model.Employee;
import org.springframework.data.domain.Page;

import java.util.stream.Stream;

public interface EmployeeService {
    Employee save(Employee employee);
    Page<Employee> findAll(int pageNum, int pageSize);
}
