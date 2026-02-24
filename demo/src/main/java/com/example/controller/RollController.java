package com.example.controller;

import com.example.model.Roll;
import com.example.service.RollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/rolls")
@CrossOrigin(origins = "*")
public class RollController {

    private final RollService rollService;

    @Autowired
    public RollController(RollService rollService) {
        this.rollService = rollService;
    }

    @PostMapping
    public ResponseEntity<Roll> save(@RequestBody Roll roll) throws SQLException {
        return ResponseEntity.ok(rollService.saveRoll(roll));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Roll> getById(@PathVariable long id) throws SQLException {
        Roll r = rollService.getRollById(id);
        if (r == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(r);
    }

    @GetMapping
    public ResponseEntity<List<Roll>> getAll() throws SQLException {
        return ResponseEntity.ok(rollService.getAllRolls());
    }
}