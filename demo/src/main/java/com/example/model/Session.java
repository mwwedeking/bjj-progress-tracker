package com.example.model;

/**
 * Import statements for the Session class
 */
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The Session class represents a training session in Brazilian Jiu-Jitsu,
 * including details such as:
 * date, 
 * time, 
 * whether session was gi or no-gi, 
 * instructor, 
 * user's currentBelt at the time of the session, 
 * and a list of rolls that took place during the session. 
 * This class serves as a data model for training sessions that can be tracked and analyzed in the BJJ Progress Tracker application.
 */
public class Session {
    private long id;
    private LocalDate date;
    private LocalTime time;
    private boolean isGi;
    private String instructor;
    private String currentBelt;    
    private List<Roll> rolls = new ArrayList<>();

    /**
     * Default constructor for Session object
     */
    public Session() {}

    /**
     * Constructor for Session object
     * @param id the id of the training session
     * @param date the date of the training session
     * @param time the time of the training session
     * @param isGi whether the session was a gi or no-gi session
     * @param instructor the instructor of the training session
     * @param currentBelt the user's currentBelt at the time of the training session
     * @param rolls the list of rolls that took place during the training session
     */
    public Session(long id, LocalDate date, LocalTime time, boolean isGi, String instructor, String currentBelt, List<Roll> rolls) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.isGi = isGi;
        this.instructor = instructor;
        this.currentBelt = currentBelt;
        this.rolls = rolls;
    }

    /**
     * Get the id of the training session
     * @return the id of the training session
     */
    public long getId() {
        return id;
    }

    /**
     * Set the id of the training session
     * @param id the id of the training session
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get the date of the training session
     * @return the date of the training session
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Set the date of the training session
     * @param date the date of the training session
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Get the time of the training session
     * @return the time of the training session
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Set the time of the training session
     * @param time the time of the training session
     */
    public void setTime(LocalTime time) {
        this.time = time;
    }
    
    /**
     * Get whether the session was a gi or no-gi session
     * @return True/False whether the session was a gi or no-gi session
     */
    public boolean isGi() {
        return isGi;
    }

    /**
     * Set whether the session was a gi or no-gi session
     * @param gi True/False whether the session was a gi or no-gi session
     */
    public void setGi(boolean gi) {
        isGi = gi;
    }

    /**
     * Get the instructor of the training session
     * @return the instructor of the training session
     */
    public String getInstructor() {
        return instructor;
    }
    
    /**
     * Set the instructor of the training session
     * @param instructor the instructor of the training session
     */
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    /**
     * Get the user's currentBelt at the time of the training session
     * @return the user's currentBelt at the time of the training session
     */
    public String getcurrentBelt() {
        return currentBelt;
    }

    /**
     * Set the user's currentBelt at the time of the training session
     * @param currentBelt the user's currentBelt at the time of the training session
     */
    public void setcurrentBelt(String currentBelt) {
        this.currentBelt = currentBelt;
    }
    
    /**
     * Get the list of rolls that took place during the training session
     * @return the list of rolls that took place during the training session
     */
    public List<Roll> getRolls() {
        return rolls;
    }

    /**
     * Set the list of rolls that took place during the training session
     * @param rolls the list of rolls that took place during the training session
     */
    public void setRolls(List<Roll> rolls) {
        this.rolls = rolls;
    }

    /**
     * Add a roll to the list of rolls that took place during the training session
     * @param roll the roll to be added to the list of rolls that took place during the training session
     */
    public void addRoll(Roll roll) {
        rolls.add(roll);
    }

    /**
     * Override the toString method to provide a string representation of the Session object
     * @return a string representation of the Session object
     */
    @Override
    public String toString() {
        return "Session ID: " + id + ", Date: " + date + ", Time: " + time + ", Is Gi: " + isGi + ", Instructor: " + instructor + ", currentBelt: " + currentBelt + ", Rolls: " + rolls;
    }
}
