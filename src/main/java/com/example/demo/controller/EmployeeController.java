package com.example.demo.controller;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public Mono<ResponseEntity<EmployeeDto>> addEmployee(@RequestBody EmployeeDto employeeDto) {
        return employeeService.addEmployee(employeeDto)
                .map(savedEmployee -> ResponseEntity.ok(savedEmployee));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<EmployeeDto>> getEmployeeById(@PathVariable String id) {
        return employeeService.getEmployeeById(id)
                .map(employee -> ResponseEntity.ok(employee))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<EmployeeDto> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<EmployeeDto>> updateEmployee(@PathVariable String id, @RequestBody EmployeeDto employeeDto) {
        return employeeService.updateEmployee(id, employeeDto)
                .map(updatedEmployee -> ResponseEntity.ok(updatedEmployee))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteEmployee(@PathVariable String id) {
        return employeeService.deleteEmployee(id)
                .map(deleted -> ResponseEntity.ok().<Void>build())
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
