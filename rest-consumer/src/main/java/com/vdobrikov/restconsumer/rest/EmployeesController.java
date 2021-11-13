package com.vdobrikov.restconsumer.rest;

import com.vdobrikov.restconsumer.api.EmployeesApi;
import com.vdobrikov.restconsumer.event.NewEmployeeEvent;
import com.vdobrikov.restconsumer.model.EmployeeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeesController implements EmployeesApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeesController.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public EmployeesController(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public ResponseEntity<EmployeeDto> newEmployee(EmployeeDto employee) {
        LOGGER.info("Got new employee: employee={}", employee);
        applicationEventPublisher.publishEvent(new NewEmployeeEvent(employee));
        return ResponseEntity.ok(employee);
    }
}
