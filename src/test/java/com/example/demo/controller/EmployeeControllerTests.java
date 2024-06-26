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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnListOfEmployees() {
        //given
        List<EmployeeDto> employeeList = new ArrayList<>();

        EmployeeDto employeeDto1 = new EmployeeDto();
        employeeDto1.setFirstName("oguz");
        employeeDto1.setLastName("karadag");
        employeeDto1.setEmail("karadagoguzkaan@gmail.com");
        employeeList.add(employeeDto1);

        EmployeeDto employeeDto2 = new EmployeeDto();
        employeeDto2.setFirstName("selcuk");
        employeeDto2.setLastName("karadag");
        employeeDto2.setEmail("karadagselcuk@gmail.com");
        employeeList.add(employeeDto2);

        //convert list to Flux type
        Flux<EmployeeDto> employeeDtoFlux = Flux.fromIterable(employeeList);

        BDDMockito.given(employeeService.getAllEmployees())
                .willReturn(employeeDtoFlux);

        //when
        WebTestClient.ResponseSpec response = webTestClient.get().uri("/api/employees")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        //then
        response.expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .consumeWith(System.out::println);
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() {
        // given - pre-conditions
        String employeeId = "123";

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employeeId);  // Ensure the DTO has the correct ID
        employeeDto.setFirstName("oguz");
        employeeDto.setLastName("karadag");
        employeeDto.setEmail("karadagoguzkaan@gmail.com");

        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.eq(employeeId), ArgumentMatchers.any(EmployeeDto.class)))
                .willReturn(Mono.just(employeeDto));

        // when - action or behaviour
        WebTestClient.ResponseSpec response = webTestClient.put().uri("/api/employees/{id}", Collections.singletonMap("id", employeeId))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(employeeDto), EmployeeDto.class)
                .exchange();

        // then - verify the result or output
        response.expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(employeeDto.getLastName())
                .jsonPath("$.email").isEqualTo(employeeDto.getEmail());
    }


    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnNothing(){

        // given - pre-conditions
        String employeeId = "123";
        Mono<Void> voidMono = Mono.empty();
        BDDMockito.given(employeeService.deleteEmployee(employeeId))
                .willReturn(voidMono);

        // when - action or behaviour
        WebTestClient.ResponseSpec response = webTestClient
                .delete()
                .uri("/api/employees/{id}", Collections.singletonMap("id", employeeId))
                .exchange();

        // then - verify the output
        response.expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);
    }

}
