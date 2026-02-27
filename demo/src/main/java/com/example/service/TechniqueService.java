package com.example.service;

import com.example.model.Technique;

import java.sql.SQLException;
import java.util.List;

public interface TechniqueService {
    Technique saveTechnique(Technique technique) throws SQLException;
    Technique getTechnique(long id) throws SQLException;
    List<Technique> getTechniques() throws SQLException;
    void deleteTechnique(long id) throws SQLException;
}