package com.example;

// ExampleIncrement.java
public class DataProviderHarness {
    public static void main(String[] args) throws Exception {
        DataProvider provider = new DataProvider();

        provider.getAllTechniques().forEach(System.out::println);

        // Example database manipulation used for Project 1
        // Assume technique id 1 is Armbar and roll id 1 exists
        // long rollId = 1;
        // long techniqueId = 1;

        // // Add 2 subs for this technique in that roll (this will insert or update)
        // provider.upsertSingleTechniqueCount(rollId, techniqueId, 2, 0);

        // // If you want to increment taps too (e.g., 1 tap)
        // provider.upsertSingleTechniqueCount(rollId, techniqueId, 0, 1);

        // // Read roll and print populated technique lists
        // Roll roll = provider.readRollById(rollId);
        // System.out.println("Subs: " + roll.getSubs());
        // System.out.println("Taps: " + roll.getTaps());
    }
}

