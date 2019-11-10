package com.kafka.rest.example.impl.prooftestimplementation.controller;

import com.kafka.rest.example.impl.prooftestimplementation.exception.ResourceNotFoundException;
import com.kafka.rest.example.impl.prooftestimplementation.model.Employee;


import com.kafka.rest.example.impl.prooftestimplementation.service.Producer;
import com.kafka.rest.example.impl.prooftestimplementation.repository.EmployeeRepository;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1")
@Api(value="Employee Management System", description="Operations pertaining to employee in Employee Management System")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;
    private final Producer producer;


    @Autowired
    EmployeeController(Producer producer) {
        this.producer = producer;
    }

    @ApiOperation(value = "View a list of available employees", response = List.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }


    @ApiOperation(value = "Add an employee")
    @PostMapping("/addEmployee")
    public Employee createEmployee(
            @ApiParam(value = "Employee object store in database table", required = true)
            @Valid @RequestBody Employee employee) {

        producer.sendMessage(employee);
        return employeeRepository.save(employee);
    }

}