package com.example.service;

import com.example.model.Roll;

import java.sql.SQLException;
import java.util.List;

public interface RollService {
    Roll saveRoll(Roll roll) throws SQLException;
    Roll getRollById(long id) throws SQLException;
    List<Roll> getAllRolls() throws SQLException;
}