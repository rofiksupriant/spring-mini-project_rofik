package com.rofik.miniproject.controller;

import com.rofik.miniproject.service.ExampleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "v1")
public class ExampleController {
    @Autowired
    private ExampleService exampleService;

    @GetMapping(value = "greeting")
    public ResponseEntity<Object> greeting() {
        return exampleService.greeting();
    }

}
