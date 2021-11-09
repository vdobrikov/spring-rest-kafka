package com.vdobrikov.kafkaconsumer.service;

import com.vdobrikov.kafkaconsumer.model.Employee;
import com.vdobrikov.kafkaconsumer.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee save(Employee employee) {
        Objects.requireNonNull(employee, "'employee' cannot be null");

        return employeeRepository.save(employee);
    }

    @Override
    public Page<Employee> findAll(int pageNum, int pageSize) {
        Assert.isTrue(pageNum >= 0, "'pageNum' cannot be negative");
        Assert.isTrue(pageSize > 0, "'pageSize' cannot be negative or 0");

        return employeeRepository.findAll(Pageable.ofSize(pageSize).withPage(pageNum));
    }
}
