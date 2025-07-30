package com.diogomekie.binderbuddy.backend.controllers;

import com.diogomekie.binderbuddy.backend.dto.BinderDto;
import com.diogomekie.binderbuddy.backend.dto.CreateBinderRequest;
import com.diogomekie.binderbuddy.backend.service.BinderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/binders")
public class BinderController {

    private final BinderService binderService;

    public BinderController(BinderService binderService) {
        this.binderService = binderService;
    }

    @PostMapping
    public ResponseEntity<BinderDto> createBinder(@RequestBody CreateBinderRequest request) {
        BinderDto newBinder = binderService.createBinder(request);
        return new ResponseEntity<>(newBinder, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BinderDto>> getAllBinders() {
        List<BinderDto> binders = binderService.getAllBinders();
        return ResponseEntity.ok(binders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BinderDto> getBinderById(@PathVariable Long id) {
        return binderService.getBinderById(id)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BinderDto> updateBinder(@PathVariable Long id, @RequestBody CreateBinderRequest request) {
        return binderService.updateBinder(id, request)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBinder(@PathVariable Long id) {
        if (binderService.deleteBinder(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}