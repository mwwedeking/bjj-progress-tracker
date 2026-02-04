package com.example;

// Technique.java
public class Technique {
    private long id;
    private String name;
    private String position;
    private int numFinishes; // aggregate field you may update from history
    private int numTaps;     // aggregate field you may update from history

    public Technique() {}
    public Technique(long id, String name, String position, int numFinishes, int numTaps) {
        this.id = id; this.name = name; this.position = position;
        this.numFinishes = numFinishes; this.numTaps = numTaps;
    }
    // getters / setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public int getNumFinishes() { return numFinishes; }
    public void setNumFinishes(int numFinishes) { this.numFinishes = numFinishes; }
    public int getNumTaps() { return numTaps; }
    public void setNumTaps(int numTaps) { this.numTaps = numTaps; }
    @Override
    public String toString() {
        return "Technique{id=" + id + ", name=" + name + ", position=" + position + 
               ", numFinishes=" + numFinishes + ", numTaps=" + numTaps + "}";
    }
}
