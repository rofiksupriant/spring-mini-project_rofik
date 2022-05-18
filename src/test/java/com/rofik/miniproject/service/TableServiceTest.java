package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.common.ApiResponse;
import com.rofik.miniproject.domain.dao.Table;
import com.rofik.miniproject.domain.dto.request.TableRequest;
import com.rofik.miniproject.domain.dto.response.TableResponse;
import com.rofik.miniproject.repository.TableRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TableServiceTest {
    @MockBean
    private TableRepository tableRepository;

    @Autowired
    private TableService tableService;

    @Test
    void createOneSuccess() {
        Table table = Table.builder().id(1L).uuid("74706156-db51-492e-8e57-abcf134c0098").number(1).build();
        when(tableRepository.saveAndFlush(any())).thenReturn(table);

        TableRequest tableRequest = new TableRequest();
        tableRequest.setNumber(1);
        ResponseEntity responseEntity = tableService.createOne(tableRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        TableResponse data = (TableResponse) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(1, data.getNumber());
    }

    @Test
    void createOneBadRequest() {
        Table table = Table.builder().id(1L).uuid("74706156-db51-492e-8e57-abcf134c0098").number(1).build();
        when(tableRepository.findByNumber(any())).thenReturn(Optional.of(table));

        TableRequest tableRequest = new TableRequest();
        tableRequest.setNumber(1);

        ResponseEntity responseEntity = tableService.createOne(tableRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, apiResponse.getStatus());
    }

    @Test
    void getAll() {
        List<Table> tableList = Arrays.asList(
                Table.builder().id(1L).uuid("74706156-db51-492e-8e57-abcf134c0098").number(1).build(),
                Table.builder().id(2L).uuid("8c2ce321-2bf5-4e7e-9442-c24b5b9f7d9f").number(2).build(),
                Table.builder().id(3L).uuid("fadc97a9-3c44-4f60-9a87-f9ebb7aac850").number(3).build(),
                Table.builder().id(4L).uuid("1ac4b2d8-130e-4b8f-9a5e-e8b636d5d1e8").number(4).build(),
                Table.builder().id(5L).uuid("f7d0e313-6b3c-46cd-831f-b66b61833cdc").number(5).build()
        );
        when(tableRepository.findAll()).thenReturn(tableList);

        ResponseEntity responseEntity = tableService.getAll();
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        List<TableResponse> data = (List<TableResponse>) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(5, data.size());
    }

    @Test
    void getOneByIdSuccess() {
        Table table = Table.builder().id(1L).uuid("74706156-db51-492e-8e57-abcf134c0098").number(1).build();
        when(tableRepository.findById(any())).thenReturn(Optional.of(table));

        ResponseEntity responseEntity = tableService.getOneById(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        TableResponse data = (TableResponse) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(1L, data.getId());
    }

    @Test
    void getOneByIdNotFound() {
        when(tableRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        ResponseEntity responseEntity = tableService.getOneById(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
    }

    @Test
    void getOneByUuid() {
        Table table = Table.builder().id(1L).uuid("74706156-db51-492e-8e57-abcf134c0098").number(1).build();
        when(tableRepository.findByUuid(any())).thenReturn(Optional.ofNullable(table));

        ResponseEntity responseEntity = tableService.getOneByUuid("74706156-db51-492e-8e57-abcf134c0098");
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        TableResponse data = (TableResponse) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals("74706156-db51-492e-8e57-abcf134c0098", data.getUuid());
    }

    @Test
    void getOneByUuidNotFound() {
        when(tableRepository.findByUuid(any())).thenReturn(Optional.ofNullable(null));

        ResponseEntity responseEntity = tableService.getOneByUuid("74706156-xxxx-492e-8e57-abcf134c0098");
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
    }

    @Test
    void updateOneSuccess() {
        Table table = Table.builder().id(1L).uuid("74706156-db51-492e-8e57-abcf134c0098").number(1).build();
        when(tableRepository.findById(any())).thenReturn(Optional.of(table));
        when(tableRepository.findByNumber(any())).thenReturn(Optional.ofNullable(null));
        when(tableRepository.saveAndFlush(any())).thenReturn(table);

        TableRequest request = new TableRequest();
        request.setNumber(1);
        ResponseEntity responseEntity = tableService.updateOne(1L, request);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        TableResponse data = (TableResponse) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(1L, data.getId());
        assertEquals(1, data.getNumber());
    }

    @Test
    void updateOneNotFound() {
        when(tableRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        TableRequest request = new TableRequest();
        request.setNumber(2);
        ResponseEntity responseEntity = tableService.updateOne(1L, request);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
    }

    @Test
    void updateOneBadRequest() {
        Table table = Table.builder().id(1L).uuid("74706156-db51-492e-8e57-abcf134c0098").number(1).build();
        when(tableRepository.findById(any())).thenReturn(Optional.of(table));
        when(tableRepository.findByNumber(any())).thenReturn(Optional.of(table));

        TableRequest request = new TableRequest();
        request.setNumber(2);
        ResponseEntity responseEntity = tableService.updateOne(1L, request);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, apiResponse.getStatus());
    }

    @Test
    void deleteOneSuccess() {
        Table table = Table.builder().id(1L).uuid("74706156-db51-492e-8e57-abcf134c0098").number(1).build();
        when(tableRepository.findById(any())).thenReturn(Optional.of(table));

        ResponseEntity responseEntity = tableService.deleteOne(6L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
    }

    @Test
    void deleteOneNotFound() {
        when(tableRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        ResponseEntity responseEntity = tableService.deleteOne(6L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
    }
}