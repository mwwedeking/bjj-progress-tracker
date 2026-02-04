package com.example;

// TechniqueCount.java
public class TechniqueCount {
    private Technique technique;
    private int count;

    public TechniqueCount() {}
    public TechniqueCount(Technique technique, int count) {
        this.technique = technique;
        this.count = count;
    }
    public Technique getTechnique() { return technique; }
    public void setTechnique(Technique technique) { this.technique = technique; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    public void increment() { this.count++; }
    public void increment(int by) { this.count += by; }
    @Override
    public String toString() {
        return "TechniqueCount{technique=" + technique + ", count=" + count + "}";
    }
}
