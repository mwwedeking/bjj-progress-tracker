package com.example.service;

import com.example.business.BusinessManager;
import com.example.model.Roll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class RollServiceImpl implements RollService {

    private final BusinessManager businessManager;

    @Autowired
    public RollServiceImpl(BusinessManager businessManager) {
        this.businessManager = businessManager;
    }

    @Override
    public Roll saveRoll(Roll roll) throws SQLException {
        return businessManager.saveRoll(roll);
    }

    @Override
    public Roll getRoll(long id) throws SQLException {
        return businessManager.getRoll(id); 
    }

    @Override
    public List<Roll> getRolls() throws SQLException {
        return businessManager.getRolls();
    }

    @Override
    public void deleteRoll(long id) throws SQLException {
        businessManager.deleteRoll(id);
    }
}