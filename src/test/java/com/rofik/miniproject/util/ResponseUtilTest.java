package com.rofik.miniproject.util;

import com.rofik.miniproject.domain.common.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ResponseUtil.class)
class ResponseUtilTest {

    @Test
    void build1_Test() {
        ResponseEntity<Object> responseEntity = ResponseUtil.build("Success", HttpStatus.OK);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(apiResponse.getStatus(), HttpStatus.OK);
        assertEquals(apiResponse.getMessage(), "Success");
    }

    @Test
    void build2_Test() {
        ResponseEntity<Object> responseEntity = ResponseUtil.build("Success", HttpStatus.OK, "Data");
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(apiResponse.getStatus(), HttpStatus.OK);
        assertEquals("Success", apiResponse.getMessage());
        assertEquals("Data", apiResponse.getData());
    }
}