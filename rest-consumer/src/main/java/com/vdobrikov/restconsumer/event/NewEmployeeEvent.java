package com.vdobrikov.restconsumer.event;

import com.vdobrikov.model.EmployeeDto;

public class NewEmployeeEvent extends Event<EmployeeDto> {
    public NewEmployeeEvent(EmployeeDto value) {
        super(value);
    }
}
