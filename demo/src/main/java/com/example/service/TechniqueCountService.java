package com.example.service;

import com.example.model.TechniqueCount;

import java.sql.SQLException;

public interface TechniqueCountService {
    TechniqueCount saveTechniqueCount(TechniqueCount tc) throws SQLException;
}