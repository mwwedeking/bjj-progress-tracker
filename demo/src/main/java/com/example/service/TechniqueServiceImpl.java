package com.example.service;

import com.example.business.BusinessManager;
import com.example.model.Technique;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class TechniqueServiceImpl implements TechniqueService {

    private final BusinessManager businessManager;

    @Autowired
    public TechniqueServiceImpl(BusinessManager businessManager) {
        this.businessManager = businessManager;
    }

    @Override
    public Technique saveTechnique(Technique technique) throws SQLException {
        return businessManager.saveTechnique(technique);
    }

    @Override
    public Technique getTechniqueById(long id) throws SQLException {
        return businessManager.getTechniqueById(id);
    }

    @Override
    public List<Technique> getAllTechniques() throws SQLException {
        return businessManager.getAllTechniques();
    }
}