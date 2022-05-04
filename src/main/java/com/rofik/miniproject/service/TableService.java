package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.dao.Table;
import com.rofik.miniproject.domain.dto.request.TableRequest;
import com.rofik.miniproject.domain.dto.response.TableResponse;
import com.rofik.miniproject.repository.TableRepository;
import com.rofik.miniproject.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TableService {
    @Autowired
    private TableRepository tableRepository;

    public ResponseEntity<Object> createOne(TableRequest request) {
        try {
            Optional<Table> tableOptional = tableRepository.findByNumber(request.getNumber());
            if (tableOptional.isPresent())
                return ResponseUtil.build("table number already used", HttpStatus.BAD_REQUEST);

            Table table = new Table();
            table.setNumber(request.getNumber());
            table = tableRepository.saveAndFlush(table);

            TableResponse response = new TableResponse();
            BeanUtils.copyProperties(table, response);
            return ResponseUtil.build("table created successfully", HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error create new table: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> getAll() {
        try {
            log.info("Get all table");
            List<Table> tableList = tableRepository.findAll();

            List<TableResponse> result = new ArrayList<>();
            tableList.forEach(table -> result.add(
                    TableResponse.builder()
                            .id(table.getId())
                            .uuid(table.getUuid())
                            .number(table.getNumber())
                            .build()
            ));

            return ResponseUtil.build("list table", HttpStatus.OK, result);
        } catch (Exception e) {
            log.error("Error get all table: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> getOneById(Long id) {
        try {
            Optional<Table> tableOptional = tableRepository.findById(id);

            if (tableOptional.isEmpty()) return ResponseUtil.build("data not found", HttpStatus.NOT_FOUND);

            Table table = tableOptional.get();
            TableResponse response = new TableResponse();
            BeanUtils.copyProperties(table, response);

            return ResponseUtil.build("table by id", HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error get table by id: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> getOneByUuid(String uuid) {
        try {
            Optional<Table> tableOptional = tableRepository.findByUuid(uuid);

            if (tableOptional.isEmpty()) return ResponseUtil.build("data not found", HttpStatus.NOT_FOUND);

            Table table = tableOptional.get();
            TableResponse response = new TableResponse();
            BeanUtils.copyProperties(table, response);
            return ResponseUtil.build("table by id", HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error get table by id: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> updateOne(Long id, TableRequest request) {
        try {
            log.info("Update table {}", id);

            Optional<Table> tableOptional = tableRepository.findById(id);
            if (tableOptional.isEmpty()) return ResponseUtil.build("table not found", HttpStatus.NOT_FOUND);

            Optional<Table> tableOptionalByNumber = tableRepository.findByNumber(request.getNumber());
            if (tableOptionalByNumber.isPresent())
                return ResponseUtil.build("table number already used", HttpStatus.BAD_REQUEST);

            Table table = tableOptional.get();
            table.setNumber(request.getNumber());
            table = tableRepository.saveAndFlush(table);

            TableResponse response = new TableResponse();
            BeanUtils.copyProperties(table, response);
            return ResponseUtil.build("table updatedd successfully", HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error update table with id {}: {}", id, e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> deleteOne(Long id) {
        try {
            log.info("Delete table with id {}", id);

            Optional<Table> tableOptional = tableRepository.findById(id);
            if (tableOptional.isEmpty()) return ResponseUtil.build("table not found", HttpStatus.NOT_FOUND);

            tableRepository.deleteById(id);
            return ResponseUtil.build("table deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error delete table with id {}: {}", id, e.getLocalizedMessage());
            throw e;
        }

    }
}
