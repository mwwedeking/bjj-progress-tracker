package com.example.service;

import com.example.business.BusinessManager;
import com.example.model.TechniqueCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class TechniqueCountServiceImpl implements TechniqueCountService {

    private final BusinessManager businessManager;

    @Autowired
    public TechniqueCountServiceImpl(BusinessManager businessManager) {
        this.businessManager = businessManager;
    }

    @Override
    public TechniqueCount saveTechniqueCount(TechniqueCount techniqueCount) throws SQLException {
        return businessManager.saveTechniqueCount(techniqueCount);
    }
}