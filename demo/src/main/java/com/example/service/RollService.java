package com.example.service;

import com.example.model.Roll;

import java.sql.SQLException;
import java.util.List;

public interface RollService {
    Roll saveRoll(Roll roll) throws SQLException;
    Roll getRoll(long id) throws SQLException;
    List<Roll> getRolls() throws SQLException;
    void deleteRoll(long id) throws SQLException;
}