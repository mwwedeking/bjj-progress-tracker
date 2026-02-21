package com.example;

/**
 * Import statements for the Roll class
 */
import java.util.ArrayList;
import java.util.List;

/**
 * The Roll class represents a specific roll that took place during a training session in Brazilian Jiu-Jitsu,
 * including details such as:
 * length of the roll in minutes,
 * training partner,
 * number of rounds,
 * and lists of submissions and taps that occurred during the roll, represented as lists of TechniqueCount objects.
 * This class serves as a data model for rolls that can be tracked and analyzed in the BJJ Progress Tracker application.
 */
public class Roll {
    private long id;
    private int lengthMinutes;
    private String partner;
    private int numRounds;
    private List<TechniqueCount> subs = new ArrayList<>(); 
    private List<TechniqueCount> taps = new ArrayList<>(); 

    /**
     * Default constructor for Roll object
     */
    public Roll() {}

    /**
     * Constructor for Roll object
     * @param id the unique identifier for the roll
     * @param lengthMinutes the length of the roll in minutes
     * @param partner the training partner during the roll
     * @param numRounds the number of rounds during the roll
     * @param subs list of techniques used by the user to successfully submit their training partner during the roll, along with the count of how many times each technique was used
     * @param taps list of techniques used against the user by their training partner forcing the user to tap out, along with the count of how many times each technique was used
     */
    public Roll(long id, int lengthMinutes, String partner, int numRounds, List<TechniqueCount> subs, List<TechniqueCount> taps) {
        this.id = id;
        this.lengthMinutes = lengthMinutes;
        this.partner = partner;
        this.numRounds = numRounds;
        this.subs = subs;
        this.taps = taps;
    }

    /**
     * Get the unique identifier for the roll
     * @return the unique identifier for the roll
     */    public long getId() { 
        return id;
    }

    /**
     * Set the unique identifier for the roll
     * @param id the unique identifier for the roll
     */
    public void setId(long id) { 
        this.id = id;
    }

    /**
     * Get the length of the roll in minutes
     * @return the length of the roll in minutes
     */
    public int getLengthMinutes() { 
        return lengthMinutes; 
    }

    /**
     * Set the length of the roll in minutes
     * @param lengthMinutes the length of the roll in minutes
     */
    public void setLengthMinutes(int lengthMinutes) { 
        this.lengthMinutes = lengthMinutes;
    }

    /**
     * Get the training partner during the roll
     * @return the training partner during the roll
     */
    public String getPartner() { 
        return partner; 
    }

    /**
     * Set the training partner during the roll
     * @param partner the training partner during the roll
     */
    public void setPartner(String partner) { 
        this.partner = partner; 
    }

    /**
     * Get the number of rounds during the roll
     * @return the number of rounds during the roll
     */
    public int getNumRounds() { 
        return numRounds; 
    }

    /**
     * Set the number of rounds during the roll
     * @param numRounds the number of rounds during the roll
     */
    public void setNumRounds(int numRounds) { 
        this.numRounds = numRounds; 
    }

    /**
     * Get the list of techniques used by the user to successfully submit their training partner during the roll, along with the count of how many times each technique was used
     * @return the list of techniques used by the user to successfully submit their training partner during the roll, along with the count of how many times each technique was used
     */
    public List<TechniqueCount> getSubs() { 
        return subs; 
    }

    /**
     * Set the list of techniques used by the user to successfully submit their training partner during the roll, along with the count of how many times each technique was used
     * @param subs the list of techniques used by the user to successfully submit their training partner during the roll, along with the count of how many times each technique was used
     */
    public void setSubs(List<TechniqueCount> subs) { 
        this.subs = subs; 
    }

    /**
     * Get the list of techniques used against the user by their training partner forcing the user to tap out, along with the count of how many times each technique was used
     * @return the list of techniques used against the user by their training partner forcing the user to tap out, along with the count of how many times each technique was used
     */
    public List<TechniqueCount> getTaps() { 
        return taps; 
    }

    /**
     * Set the list of techniques used against the user by their training partner forcing the user to tap out, along with the count of how many times each technique was used
     * @param taps the list of techniques used against the user by their training partner forcing the user to tap out, along with the count of how many times each technique was used
     */
    public void setTaps(List<TechniqueCount> taps) { this.taps = taps; }

    /**
     * Find a TechniqueCount in the subs list for a given technique ID
     * @param techniqueId the ID of the technique to find
     * @return the TechniqueCount object if found, otherwise null
     */
    public TechniqueCount findSubsForTechnique(long techniqueId) {
        for (TechniqueCount tc : subs) if (tc.getTechnique().getId() == techniqueId) return tc;
        return null;
    }

    /**
     * Find a TechniqueCount in the taps list for a given technique ID
     * @param techniqueId the ID of the technique to find
     * @return the TechniqueCount object if found, otherwise null
     */
    public TechniqueCount findTapsForTechnique(long techniqueId) {
        for (TechniqueCount tc : taps) if (tc.getTechnique().getId() == techniqueId) return tc;
        return null;
    }

    /**
     * Increment the count of a technique used for submission or tap by a given amount. 
     * If the technique does not exist in the respective list, it will be added with the initial count equal to the increment amount.
     * @param technique the Technique object representing the technique to increment
     * @param by the amount to increment the count by
     */
    public void incrementSub(Technique technique, int by) {
        TechniqueCount tc = findSubsForTechnique(technique.getId());
        if (tc == null) {
            tc = new TechniqueCount(technique, by);
            subs.add(tc);
        } else {
            tc.increment(by);
        }
    }

    /**
     * Increment the count of a technique used for submission or tap by a given amount. 
     * If the technique does not exist in the respective list, it will be added with the initial count equal to the increment amount.
     * @param technique the Technique object representing the technique to increment
     * @param by the amount to increment the count by
     */
    public void incrementTap(Technique technique, int by) {
        TechniqueCount tc = findTapsForTechnique(technique.getId());
        if (tc == null) {
            tc = new TechniqueCount(technique, by);
            taps.add(tc);
        } else {
            tc.increment(by);
        }
    }

    /**
     * Override the toString method to provide a string representation of the Roll object, including its ID, length in minutes, training partner, number of rounds, and lists of submissions and taps.
     * @return a string representation of the Roll object
     */
    @Override
    public String toString() {
        return "Roll ID: " + id + ", Length (minutes): " + lengthMinutes + ", Partner: " + partner +", Number of Rounds: " + numRounds + ", Subs: " + subs + ", Taps: " + taps;
    }
}
