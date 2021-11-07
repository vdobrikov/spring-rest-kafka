package com.vdobrikov.restconsumer.rest;

import com.vdobrikov.restconsumer.event.NewEmployeeEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.closeTo;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeesController.class)
@AutoConfigureMockMvc
class EmployeesControllerTest {

    @MockBean
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void newEmployeeCorrectValue() throws Exception {
        doNothing().when(applicationEventPublisher).publishEvent(isA(NewEmployeeEvent.class));

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
                "  \"surname\" : \"  \"," +
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
}