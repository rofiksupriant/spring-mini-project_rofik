package com.rofik.miniproject.service;

import com.rofik.miniproject.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExampleService {
    public ResponseEntity<Object> helloWorld() {
        return ResponseUtil.build("hello success", HttpStatus.OK, "Hello World!");
    }

    public ResponseEntity<Object> greeting(String name) {
        if (name == null || name.trim().length() == 0) {
            return ResponseUtil.build("greeting success!", HttpStatus.OK, "Please Provide a Name!");
        } else {
            return ResponseUtil.build("greeting success!", HttpStatus.OK, String.format("Hello %s", name));
        }
    }

}
