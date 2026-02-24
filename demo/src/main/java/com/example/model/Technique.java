package com.example.model;

/**
 * The Technique class represents a specific technique in Brazilian Jiu-Jitsu,
 * including its name, the position from which it is executed, and aggregate fields
 * for the number of finishes and taps the user is associated with that technique. This class
 * serves as a data model for techniques that can be tracked and analyzed in the BJJ Progress Tracker application.
 */
public class Technique {
    private long id;
    private String name;
    private String position;
    private int numFinishes; 
    private int numTaps;     

    /**
     * Default constructor for Technique object
     */
    public Technique() {}

    /**
     * Constructor for Technique object
     * @param id the id of the technique
     * @param name the name of the technique
     * @param position the position from which the technique was executed
     * @param numFinishes the number of finishes achieved with this technique
     * @param numTaps the number of times you've been tapped by this technique
     */
    public Technique(long id, String name, String position, int numFinishes, int numTaps) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.numFinishes = numFinishes;
        this.numTaps = numTaps;
    }

    /**
    * Get the id of the technique
    * @return the id of the technique
    */
    public long getId() {
        return id;
    }

    /**
     * Set the id of the technique
     * @param id the id the technique will be set to
     */
    public void setId(long id) {
        this.id = id;
    }
    
    /**
     * Get the name of the technique
     * @return the name of the technique
     */
    public String getName() { 
        return name; 
    }

    /**
     * Set the name of the technique
     * @param name the name the technique will be set to
     */
    public void setName(String name) { 
        this.name = name;
    }

    /**
     * Get the position from which the technique was executed
     * @return the position from which the technique was executed
     */
    public String getPosition() {
        return position;
    }

    /**
     * Set the position from which the technique was executed
     * @param position the position from which the technique was executed
     */
    public void setPosition(String position) {
        this.position = position;
    }
    
    /**
     * Get the number of finishes achieved with this technique
     * @return the number of finishes achieved with this technique
     */
    public int getNumFinishes() {
        return numFinishes;
    }

    /**
     * Set the number of finishes achieved with this technique
     * @param numFinishes the number of finishes achieved with this technique
     */
    public void setNumFinishes(int numFinishes) {
        this.numFinishes = numFinishes;
    }
    
    /**
     * Get the number of times you've been tapped by this technique
     * @return the number of times you've been tapped by this technique
     */
    public int getNumTaps() {
        return numTaps;
    }
    
    /**
     * Set the number of times you've been tapped by this technique
     * @param numTaps the number of times you've been tapped by this technique
     */
    public void setNumTaps(int numTaps) {
        this.numTaps = numTaps;
    }
    
    /**
     * Override the toString method to provide a string representation of the Technique object
     * @return a string representation of the Technique object
     */
    @Override
    public String toString() {
        return "Technique ID: " + id + ", Name: " + name + ", Position: " + position + ", Number of Finishes: " + numFinishes + ", Number of Taps: " + numTaps;
    }
}
