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
    public ResponseEntity<Roll> saveRoll(@RequestBody Roll roll) throws SQLException {
        return ResponseEntity.ok(rollService.saveRoll(roll));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Roll> getRoll(@PathVariable long id) throws SQLException {
        Roll r = rollService.getRoll(id);
        if (r == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(r);
    }

    @GetMapping
    public ResponseEntity<List<Roll>> getRolls() throws SQLException {
        return ResponseEntity.ok(rollService.getRolls());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoll(@PathVariable long id) throws SQLException {
        rollService.deleteRoll(id);
        return ResponseEntity.noContent().build();
    }
}