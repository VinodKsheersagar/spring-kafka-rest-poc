package com.kafka.rest.example.impl.prooftestimplementation.repository;


import com.kafka.rest.example.impl.prooftestimplementation.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{
}
