package com.example.controller;

import com.example.model.Session;
import com.example.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "*")
public class SessionController {

    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public ResponseEntity<Session> save(@RequestBody Session session) throws SQLException {
        // Session saved = sessionService.saveSession(session);
        return ResponseEntity.ok(sessionService.saveSession(session));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getById(@PathVariable long id) throws SQLException {
        Session s = sessionService.getSessionById(id);
        if (s == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(s);
    }

    @GetMapping
    public ResponseEntity<List<Session>> getAll() throws SQLException {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }
}