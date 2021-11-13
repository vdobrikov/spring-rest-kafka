package com.vdobrikov.restconsumer.rest;

import com.vdobrikov.restconsumer.event.NewEmployeeEvent;
import com.vdobrikov.restconsumer.model.EmployeeDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeesController.class)
class EmployeesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void newEmployeeCorrectValue() throws Exception {
        String employee = "{" +
                "  \"name\" : \"Alice\"," +
                "  \"surname\" : \"Thompson\"," +
                "  \"wage\" : 5500.75," +
                "  \"eventTime\" : \"2012-04-23T18:25:43.511Z\"" +
                "}";
        mockMvc.perform(post(UriPaths.EMPLOYEES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(employee))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Alice"))
            .andExpect(jsonPath("$.surname").value("Thompson"))
            .andExpect(jsonPath("$.wage").value(closeTo(5500.75, 0.001)))
            .andExpect(jsonPath("$.eventTime").value("2012-04-23T18:25:43.511Z"));
    }

    @Test
    void newEmployeeNullName() throws Exception {
        String employee = "{" +
                "  \"surname\" : \"Thompson\"," +
                "  \"wage\" : 5500.75," +
                "  \"eventTime\" : \"2012-04-23T18:25:43.511Z\"" +
                "}";
        mockMvc.perform(post(UriPaths.EMPLOYEES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(employee))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    void newEmployeeSurnameIsBlank() throws Exception {
        String employee = "{" +
                "  \"name\" : \"Alice\"," +
                "  \"surname\" : \"\"," +
                "  \"wage\" : 5500.75," +
                "  \"eventTime\" : \"2012-04-23T18:25:43.511Z\"" +
                "}";
        mockMvc.perform(post(UriPaths.EMPLOYEES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employee))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void newEmployeeWageIsNegative() throws Exception {
        String employee = "{" +
                "  \"name\" : \"Alice\"," +
                "  \"surname\" : \"Thompson\"," +
                "  \"wage\" : -5500.75," +
                "  \"eventTime\" : \"2012-04-23T18:25:43.511Z\"" +
                "}";
        mockMvc.perform(post(UriPaths.EMPLOYEES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employee))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void newEmployeeNullEventTime() throws Exception {
        String employee = "{" +
                "  \"name\" : \"Alice\"," +
                "  \"surname\" : \"Thompson\"," +
                "  \"wage\" : 5500.75," +
                "}";
        mockMvc.perform(post(UriPaths.EMPLOYEES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employee))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void newEmployeeIncorrectEventTime() throws Exception {
        String employee = "{" +
                "  \"name\" : \"Alice\"," +
                "  \"surname\" : \"Thompson\"," +
                "  \"wage\" : 5500.75," +
                "  \"eventTime\" : \"incorrect\"" +
                "}";
        mockMvc.perform(post(UriPaths.EMPLOYEES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employee))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    /**
     * For testing that controller publishes event we need separated test with explicit mock usage of ApplicationEventPublisher
     * since it's the Spring limitation that we cannot mock it for regular MockMvcTest
     */
    @Test
    void newEmployeePublishesEvent() {
        EmployeeDto employee = new EmployeeDto();
        employee.setName("Jane");
        employee.setSurname("Doe");
        employee.setWage(100.5f);
        employee.setEventTime(OffsetDateTime.now());

        ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
        EmployeesController employeesController = new EmployeesController(applicationEventPublisher);

        ResponseEntity<EmployeeDto> employeeResponse = employeesController.newEmployee(employee);
        assertThat(employeeResponse)
                .isNotNull()
                .extracting(ResponseEntity::getBody)
                .isEqualTo(employee);

        ArgumentCaptor<NewEmployeeEvent> newEmployeeEventCaptor = ArgumentCaptor.forClass(NewEmployeeEvent.class);
        verify(applicationEventPublisher).publishEvent(newEmployeeEventCaptor.capture());

        assertThat(newEmployeeEventCaptor)
                .extracting(ArgumentCaptor::getValue)
                .isInstanceOf(NewEmployeeEvent.class)
                .extracting(NewEmployeeEvent::getValue)
                .isEqualTo(employee);
    }

}