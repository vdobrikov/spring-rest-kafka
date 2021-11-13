package com.vdobrikov.kafkaconsumer.service;

import com.vdobrikov.kafkaconsumer.model.Employee;
import org.springframework.data.domain.Page;

public interface EmployeeService {
    Employee save(Employee employee);
    Page<Employee> findAll(int pageNum, int pageSize);
}
