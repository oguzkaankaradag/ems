package com.example.demo.controller;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = EmployeeController.class)
public class EmployeeControllerTests {

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee() {
        //given
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("oguz");
        employeeDto.setLastName("karadag");
        employeeDto.setEmail("karadagoguzkaan@gmail.com");

        BDDMockito.given(employeeService.addEmployee(ArgumentMatchers.any(EmployeeDto.class)))
                .willReturn(Mono.just(employeeDto));
        //when
        WebTestClient.ResponseSpec response = webTestClient.post().uri("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();
        //then
        response.expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());

    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        //given
        String employeeId = "123";

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employeeId);
        employeeDto.setFirstName("oguz");
        employeeDto.setLastName("karadag");
        employeeDto.setEmail("karadagoguzkaan@gmail.com");

        BDDMockito.given(employeeService.getEmployeeById(employeeId))
                .willReturn(Mono.just(employeeDto));
        //when
        WebTestClient.ResponseSpec response = webTestClient.get().uri("/api/employees/{id}", Collections.singletonMap("id", employeeId))
                .exchange();

        //then
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.id").isEqualTo(employeeDto.getId())
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }

}
