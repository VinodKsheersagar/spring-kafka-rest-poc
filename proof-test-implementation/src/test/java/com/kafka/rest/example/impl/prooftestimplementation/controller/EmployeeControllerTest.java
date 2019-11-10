package com.kafka.rest.example.impl.prooftestimplementation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.when;

import com.kafka.rest.example.impl.prooftestimplementation.TestUtils;
import com.kafka.rest.example.impl.prooftestimplementation.model.Employee;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmployeeControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;



    @Before
    public void setUp() {

    }

    @LocalServerPort
    int randomServerPort;

    @MockBean
    EmployeeController employeeController;



    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetAllEmployees() throws Exception  {
        final String baseUrl = "http://localhost:"+randomServerPort+"/api/v1/employees/";
        URI uri = new URI(baseUrl);

        // given
        Employee employee1 = new Employee("Lokesh", "Gupta", "howtodoinjava@gmail.com");
        Employee employee2 = new Employee("Alex", "Gussin", "example@gmail.com");

        when(employeeController.getAllEmployees()).thenReturn(Arrays.asList(employee1, employee2));
        List<Employee> result = employeeController.getAllEmployees();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getFirstName())
                .isEqualTo(employee1.getFirstName());
        assertThat(result.get(1).getFirstName())
                .isEqualTo(employee2.getFirstName());
    }

    @Test
    void getEmployeeById() {
    }

    @Test
    public void testCreateEmployee() throws Exception {
        final String baseUrl = "http://localhost:"+randomServerPort+"/api/v1/addEmployee/";
        URI uri = new URI(baseUrl);
        //prepare data
        Employee employee = new Employee("Vinod","Kumar","ksheersagar.vinod@gmail.com");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Employee> request = new HttpEntity<>(employee, headers);
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        //Verify request succeed
        Assert.assertEquals(200, result.getStatusCodeValue());

    }
}