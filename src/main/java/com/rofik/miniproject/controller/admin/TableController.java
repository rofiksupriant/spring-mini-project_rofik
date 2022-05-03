package com.rofik.miniproject.controller.admin;

import com.rofik.miniproject.domain.dto.request.TableRequest;
import com.rofik.miniproject.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "v1/admin/tables")
public class TableController {
    @Autowired
    private TableService tableService;

    @PostMapping
    public ResponseEntity<Object> createOne(@Valid @RequestBody TableRequest request) {
        return tableService.createOne(request);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return tableService.getAll();
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Object> getOneById(@PathVariable(name = "id") Long id) {
        return tableService.getOneById(id);
    }

    @GetMapping(path = "/uuid/{uuid}")
    public ResponseEntity<Object> getOneByUuid(@PathVariable(name = "uuid") String uuid) {
        return tableService.getOneByUuid(uuid);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Object> updateOne(@PathVariable(name = "id") Long id, @Valid @RequestBody TableRequest request) {
        return tableService.updateOne(id, request);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Object> deleteOne(@PathVariable(name = "id") Long id) {
        return tableService.deleteOne(id);
    }
}
