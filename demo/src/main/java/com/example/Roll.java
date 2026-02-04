package com.example;

// Roll.java
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Roll {
    private long id;
    private int lengthMinutes;       // length in seconds
    private String partner;
    private int numRounds;
    // subs and taps represented as lists of TechniqueCount so counters are per-technique
    private List<TechniqueCount> subs = new ArrayList<>();
    private List<TechniqueCount> taps = new ArrayList<>();

    public Roll() {}
    public Roll(long id, int lengthMinutes, String partner, int numRounds) {
        this.id = id; this.lengthMinutes = lengthMinutes; this.partner = partner; this.numRounds = numRounds;
    }
    // getters / setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public int getLengthMinutes() { return lengthMinutes; }
    public void setLengthMinutes(int lengthMinutes) { this.lengthMinutes = lengthMinutes; }
    public String getPartner() { return partner; }
    public void setPartner(String partner) { this.partner = partner; }
    public int getNumRounds() { return numRounds; }
    public void setNumRounds(int numRounds) { this.numRounds = numRounds; }
    public List<TechniqueCount> getSubs() { return subs; }
    public void setSubs(List<TechniqueCount> subs) { this.subs = subs; }
    public List<TechniqueCount> getTaps() { return taps; }
    public void setTaps(List<TechniqueCount> taps) { this.taps = taps; }

    // convenience: find TechniqueCount in subs by technique id
    public TechniqueCount findSubsForTechnique(long techniqueId) {
        for (TechniqueCount tc : subs) if (tc.getTechnique().getId() == techniqueId) return tc;
        return null;
    }
    public TechniqueCount findTapsForTechnique(long techniqueId) {
        for (TechniqueCount tc : taps) if (tc.getTechnique().getId() == techniqueId) return tc;
        return null;
    }

    // increment utilities:
    public void incrementSub(Technique technique, int by) {
        TechniqueCount tc = findSubsForTechnique(technique.getId());
        if (tc == null) {
            tc = new TechniqueCount(technique, by);
            subs.add(tc);
        } else {
            tc.increment(by);
        }
    }
    public void incrementTap(Technique technique, int by) {
        TechniqueCount tc = findTapsForTechnique(technique.getId());
        if (tc == null) {
            tc = new TechniqueCount(technique, by);
            taps.add(tc);
        } else {
            tc.increment(by);
        }
    }

    @Override
    public String toString() {
        return "Roll{id=" + id + ", lengthMinutes=" + lengthMinutes + ", partner=" + partner +
               ", numRounds=" + numRounds + ", subs=" + subs + ", taps=" + taps + "}";
    }
}
