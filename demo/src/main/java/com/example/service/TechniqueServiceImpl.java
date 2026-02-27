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
    public Technique getTechnique(long id) throws SQLException {
        return businessManager.getTechnique(id);
    }

    @Override
    public List<Technique> getTechniques() throws SQLException {
        return businessManager.getTechniques();
    }

    @Override
    public void deleteTechnique(long id) throws SQLException {
        businessManager.deleteTechnique(id);
    }
}