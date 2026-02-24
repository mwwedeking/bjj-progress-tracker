package com.example.service;

import com.example.business.BusinessManager;
import com.example.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class SessionServiceImpl implements SessionService {

    private final BusinessManager businessManager;

    @Autowired
    public SessionServiceImpl(BusinessManager businessManager) {
        this.businessManager = businessManager;
    }

    @Override
    public Session saveSession(Session session) throws SQLException {
        return businessManager.saveSession(session);
    }

    @Override
    public Session getSessionById(long id) throws SQLException {
        return businessManager.getSessionById(id);
    }

    @Override
    public List<Session> getAllSessions() throws SQLException {
        return businessManager.getAllSessions();
    }
}