package com.example.controller;

import com.example.model.Technique;
import com.example.service.TechniqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/techniques")
@CrossOrigin(origins = "*")
public class TechniqueController {

    private final TechniqueService techniqueService;

    @Autowired
    public TechniqueController(TechniqueService techniqueService) {
        this.techniqueService = techniqueService;
    }

    @PostMapping
    public ResponseEntity<Technique> save(@RequestBody Technique technique) throws SQLException {
        return ResponseEntity.ok(techniqueService.saveTechnique(technique));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Technique> getById(@PathVariable long id) throws SQLException {
        Technique t = techniqueService.getTechniqueById(id);
        if (t == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(t);
    }

    @GetMapping
    public ResponseEntity<List<Technique>> getAll() throws SQLException {
        return ResponseEntity.ok(techniqueService.getAllTechniques());
    }
}