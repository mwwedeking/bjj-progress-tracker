package com.example;

// Session.java
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Session {
    private long id;
    private LocalDate date;
    private LocalTime time;
    private boolean isGi;
    private String instructor;
    private String rank;    //TODO: refactor to "currentBelt"
    private List<Roll> rolls = new ArrayList<>();

    public Session() {}
    public Session(long id, LocalDate date, LocalTime time, boolean isGi, String instructor, String rank) {
        this.id = id; this.date = date; this.time = time; this.isGi = isGi; this.instructor = instructor; this.rank = rank;
    }
    // getters / setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }
    public boolean isGi() { return isGi; }
    public void setGi(boolean gi) { isGi = gi; }
    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }
    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }
    public List<Roll> getRolls() { return rolls; }
    public void setRolls(List<Roll> rolls) { this.rolls = rolls; }

    public void addRoll(Roll r) { rolls.add(r); }

    @Override
    public String toString() {
        return "Session{id=" + id + ", date=" + date + ", time=" + time + ", isGi=" + isGi +
               ", instructor=" + instructor + ", rank=" + rank + ", rolls=" + rolls + "}";
    }
}
