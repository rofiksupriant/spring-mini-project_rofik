package com.rofik.miniproject.service;

import com.rofik.miniproject.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExampleService {
    Logger logger = LogManager.getLogger(ExampleService.class);
    public ResponseEntity<Object> greeting() {
        logger.info("Dikasih Info Massseehh");
        log.info("dikasih info dari slf4j");
        return ResponseUtil.build("Hello World!", HttpStatus.OK);
    }

    public ResponseEntity<Object> greetingName(String name) {
        if (name == null || name.trim().length() == 0) {
            return ResponseUtil.build("greeting success!", HttpStatus.OK, "Please Provide a Name!");
        } else {
            return ResponseUtil.build("greeting success!", HttpStatus.OK, String.format("Hello %s", name));
        }
    }

}
