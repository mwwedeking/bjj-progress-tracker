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
@CrossOrigin(origins = "http://localhost:5173")
public class SessionController {

    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public ResponseEntity<Session> saveSession(@RequestBody Session session) throws SQLException {
        return ResponseEntity.ok(sessionService.saveSession(session));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getSession(@PathVariable long id) throws SQLException {
        Session s = sessionService.getSession(id);
        if (s == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(s);
    }

    @GetMapping
    public ResponseEntity<List<Session>> getSessions() throws SQLException {
        return ResponseEntity.ok(sessionService.getSessions());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Session> updateSession(@PathVariable long id, @RequestBody Session session) throws SQLException {
        session.setId(id);
        return ResponseEntity.ok(sessionService.saveSession(session));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable long id) throws SQLException {
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}