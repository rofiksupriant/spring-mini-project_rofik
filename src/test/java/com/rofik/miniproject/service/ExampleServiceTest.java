package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.common.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ExampleService.class)
class ExampleServiceTest {
    @Autowired
    private ExampleService exampleService;

    @Test
    void greeting_Test() {
        ResponseEntity<Object> responseEntity = exampleService.helloWorld();
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals("Hello World!", apiResponse.getData());
    }

    @Test
    void greetingNameSuccess_Test() {
        ResponseEntity<Object> responseEntity = exampleService.greeting("Jhon Doe");
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals("Hello Jhon Doe", apiResponse.getData());
    }

    @Test
    void greetingNameNull_Test() {
        ResponseEntity<Object> responseEntity = exampleService.greeting(null);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals("Please Provide a Name!", apiResponse.getData());
    }

    @Test
    void greetingNameEmpty_Test() {
        ResponseEntity<Object> responseEntity = exampleService.greeting(" ");
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals("Please Provide a Name!", apiResponse.getData());
    }
}