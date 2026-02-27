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
    public Session getSession(long id) throws SQLException {
        return businessManager.getSession(id);
    }

    @Override
    public List<Session> getSessions() throws SQLException {
        return businessManager.getSessions();
    }

    @Override
    public void deleteSession(long id) throws SQLException {
        businessManager.deleteSession(id);
    }   
}