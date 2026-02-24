package com.example.service;

import com.example.model.Session;

import java.sql.SQLException;
import java.util.List;

public interface SessionService {
    Session saveSession(Session session) throws SQLException;
    Session getSessionById(long id) throws SQLException;
    List<Session> getAllSessions() throws SQLException;
}