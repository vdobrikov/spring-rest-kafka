package com.vdobrikov.restconsumer.event;

import com.vdobrikov.commons.dto.Employee;

public class NewEmployeeEvent extends Event<Employee> {
    public NewEmployeeEvent(Employee value) {
        super(value);
    }
}
