package com.example.demo.service.impl;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.mapper.EmployeeMapper;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Mono<EmployeeDto> addEmployee(EmployeeDto employeeDto) {
        Employee employee = employeeMapper.toEntity(employeeDto);
        return employeeRepository.save(employee)
                .map(employeeMapper::toDto);
    }

    @Override
    public Mono<EmployeeDto> getEmployeeById(String id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toDto);
    }

    @Override
    public Flux<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll()
                .map(employeeMapper::toDto);
    }

    @Override
    public Mono<EmployeeDto> updateEmployee(String id, EmployeeDto employeeDto) {
        return employeeRepository.findById(id)
                .flatMap(existingEmployee -> {
                    existingEmployee.setName(employeeDto.getName());
                    existingEmployee.setRole(employeeDto.getRole());
                    return employeeRepository.save(existingEmployee);
                })
                .map(employeeMapper::toDto);
    }

    @Override
    public Mono<Void> deleteEmployee(String id) {
        return employeeRepository.deleteById(id);
    }
}
