package com.vdobrikov.kafkaconsumer.repository;

import com.vdobrikov.kafkaconsumer.model.Employee;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface EmployeeRepository extends PagingAndSortingRepository<Employee, UUID> {
}
