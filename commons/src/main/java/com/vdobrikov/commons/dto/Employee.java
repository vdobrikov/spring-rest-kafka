package com.vdobrikov.commons.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

public final class Employee {
    @Schema(description = "First name", example = "Jane", required = true)
    @NotBlank(message = "'name' is mandatory")
    @Size(max = 255, message = "'name' max length is 255")
    private String name;

    @Schema(description = "Last name", example = "Doe", required = true)
    @NotBlank(message = "'surname' is mandatory")
    @Size(max = 255, message = "'surname' max length is 255")
    private String surname;

    @Schema(description = "Wage", example = "5500.75", required = true)
    @Min(value = 1, message = "Min value is 1")
    private float wage;

    @Schema(description = "Event time in ISO 8601 format", example = "2012-04-23T18:25:43.511Z", required = true)
    @NotNull(message = "'eventTime' is mandatory")
    private ZonedDateTime eventTime;

    public Employee() {
    }

    public Employee(String name, String surname, float wage, ZonedDateTime eventTime) {
        this.name = name;
        this.surname = surname;
        this.wage = wage;
        this.eventTime = eventTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public float getWage() {
        return wage;
    }

    public void setWage(float wage) {
        this.wage = wage;
    }

    public ZonedDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(ZonedDateTime eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", wage=" + wage +
                ", eventTime=" + eventTime +
                '}';
    }
}
