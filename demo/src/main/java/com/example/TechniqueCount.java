package com.example;

/**
 * The TechniqueCount class represents a specific technique in Brazilian Jiu-Jitsu along with a count of how many times that technique was used in a particular context 
 * (e.g., during a roll). 
 * It includes a Technique object and an integer count, as well as methods to get and set these fields, increment the count, 
 * and provide a string representation of the object. 
 * This class serves as a data model for tracking the usage of techniques in the BJJ Progress Tracker application.
 */
public class TechniqueCount {
    private Technique technique;
    private int count;

    /**
     * Default constructor for TechniqueCount object
     */
    public TechniqueCount() {}

    /**
     * Constructor for TechniqueCount object
     * @param technique the Technique object representing the specific technique being tracked
     * @param count the count of how many times the technique was used in a particular context (e.g., during a roll)
     */
    public TechniqueCount(Technique technique, int count) {
        this.technique = technique;
        this.count = count;
    }

    /**
     * Get the Technique object representing the specific technique being tracked
     * @return the Technique object representing the specific technique being tracked
     */
    public Technique getTechnique() { 
        return technique; 
    }

    /**
     * Set the Technique object representing the specific technique being tracked
     * @param technique the Technique object representing the specific technique being tracked
     */
    public void setTechnique(Technique technique) { 
        this.technique = technique; 
    }

    /**
     * Get the count of how many times the technique was used in a particular context (e.g., during a roll)
     * @return the count of how many times the technique was used in a particular context (e.g., during a roll)
     */
    public int getCount() { 
        return count; 
    }

    /**
     * Set the count of how many times the technique was used in a particular context (e.g., during a roll)
     * @param count the count of how many times the technique was used in a particular context (e.g., during a roll)
     */
    public void setCount(int count) { 
        this.count = count; 
    }
    
    /**
     * Increment the count of how many times the technique was used by 1
     */
    public void increment() { 
        this.count++; 
    }
    
    /**
     * Increment the count of how many times the technique was used by a specified amount
     * @param by
     */
    public void increment(int by) { 
        this.count += by; 
    }

    /**
     * Provide a string representation of the TechniqueCount object
     * @return a string representation of the TechniqueCount object
     */
    @Override
    public String toString() {
        return technique + ", count=" + count + "}";
    }
}
