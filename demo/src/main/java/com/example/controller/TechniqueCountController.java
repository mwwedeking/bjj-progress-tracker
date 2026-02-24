package com.example.controller;

import com.example.model.TechniqueCount;
import com.example.service.TechniqueCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/techniquecounts")
@CrossOrigin(origins = "*")
public class TechniqueCountController {

    private final TechniqueCountService service;

    @Autowired
    public TechniqueCountController(TechniqueCountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TechniqueCount> save(@RequestBody TechniqueCount tc) throws SQLException {
        return ResponseEntity.ok(service.saveTechniqueCount(tc));
    }
}