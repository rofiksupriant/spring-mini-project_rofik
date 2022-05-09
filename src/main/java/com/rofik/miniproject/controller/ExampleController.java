package com.rofik.miniproject.controller;

import com.rofik.miniproject.domain.dto.request.GreetingRequest;
import com.rofik.miniproject.service.ExampleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "v1/example")
public class ExampleController {
    @Autowired
    private ExampleService exampleService;

    @GetMapping(value = "hello-world")
    public ResponseEntity<Object> helloWorld() {
        return exampleService.helloWorld();
    }

    @Operation(summary = "Get greeting by provide a name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success Greeting",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GreetingRequest.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid name supplied",
                    content = @Content)
    })
    @GetMapping(value = "greeting")
    public ResponseEntity<Object> greeting(@RequestParam GreetingRequest request) {
        return exampleService.greeting(request.getName());
    }

}
