package com.example.service;

import com.example.model.Session;

import java.sql.SQLException;
import java.util.List;

public interface SessionService {
    Session saveSession(Session session) throws SQLException;
    Session getSession(long id) throws SQLException;
    List<Session> getSessions() throws SQLException;
    void deleteSession(long id) throws SQLException;
}