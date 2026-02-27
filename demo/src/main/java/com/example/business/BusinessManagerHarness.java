package com.example.business;

import com.example.data.DataProvider;
import com.example.model.Technique;

public class BusinessManagerHarness {
 
    public static void main(String[] args) throws Exception {
        DataProvider provider = new DataProvider();
        BusinessManager manager = new BusinessManager(provider);

        System.out.println("=== BJJ Technique CRUD demo ===");

        // 1) Create a new Technique (id==0 means insert)
        Technique newTechnique = new Technique(0, "Test Harness example", "Mount", 3, 1);
        manager.saveTechnique(newTechnique);
        System.out.println("Created new technique: " + newTechnique);

        // 2) Read it back by id
        System.out.println("\n-- Reading technique by id --");
        Technique readTechnique = manager.getTechnique(newTechnique.getId());
        System.out.println("Read technique: " + readTechnique);

        // 3) Update the technique
        System.out.println("\n-- Updating technique --");
        readTechnique.setNumFinishes(99);
        manager.saveTechnique(readTechnique);
        System.out.println("Updated technique: " + readTechnique);

        //4) Delete the technique
        System.out.println("\n-- Deleting technique --");
        manager.deleteTechnique(readTechnique.getId());

        //5) Try to read it back again (should be null)
        System.out.println("\n-- Reading deleted technique --");
        Technique deletedTechnique = manager.getTechnique(readTechnique.getId());
        System.out.println("Deleted technique read result (should be null): " + deletedTechnique);

        System.out.println("\n=== Demo complete ===");
    }   

}
