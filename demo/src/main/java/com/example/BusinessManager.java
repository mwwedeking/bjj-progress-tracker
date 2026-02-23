package com.example;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The BusinessManager class serves as the main service layer for the BJJ Progress Tracker application, 
 * providing methods to perform CRUD operations on the core entities: Technique, TechniqueCount, Roll, and Session.
 * It interacts with a DataProvider to persist and retrieve data from the underlying database, ensuring that business logic and data access are properly separated.
 * The BusinessManager handles the complexities of saving and deleting entities, including managing relationships between them
 * (e.g., ensuring that when a Roll is deleted, its associated TechniqueCounts are also handled appropriately).
 */
public class BusinessManager {

    private final DataProvider provider;

    /**
     * Constructor for BusinessManager
     * @param provider the DataProvider instance that the BusinessManager will use to interact with the database.
     */
    public BusinessManager(DataProvider provider) {
        this.provider = Objects.requireNonNull(provider);
    }

    // ======================================================================================================================================
    //                                 Session CRUD
    // ======================================================================================================================================

    /**
     * Save a Session object to the database. If the Session has an id of 0, it will be treated as a new record and inserted; otherwise, it will be treated as an existing record and updated. 
     * The method also handles saving any nested Rolls (and their associated TechniqueCounts) associated with the Session, ensuring that they are saved and linked properly. The method returns the saved Session object with its id field populated (for new records) or unchanged (for updates).
     * @param s the Session object to be saved
     * @return the saved Session object with its id field populated (for new records) or unchanged (for updates)
     * @throws SQLException if there is an error during database access
     */
    public Session saveSession(Session s) throws SQLException {
        if (s == null) throw new IllegalArgumentException("Session is null");

        // Save nested rolls first so they can be referenced by session (if session stores roll ids)
        if (s.getRolls() != null) {
            List<Roll> newRolls = new ArrayList<>();
            for (Roll r : s.getRolls()) {
                Roll saved = saveRoll(r);
                newRolls.add(saved);
            }
            s.setRolls(newRolls);
        }

        if (s.getId() == 0) {
            long newId = provider.saveSession(s);
            s.setId(newId);
        } else {
            boolean ok = provider.updateSession(s);
            if (!ok) throw new RuntimeException("Failed to update Session with id " + s.getId());
        }

        return s;
    }

    /**
     * Retrieve a Session from the database by its id. The method returns the Session object if found, or null if no record with the given id exists.
     * @param id the id of the Session to be retrieved
     * @return the Session object if found, or null if no record with the given id exists
     * @throws SQLException if there is an error during database access
     */ 
    public Session getSessionById(long id) throws SQLException {
        return provider.getSessionById(id);
    }

    /**
     * Retrieve all Session records from the database. The method returns a list of Session objects, which may be empty if no records exist.
     * @return a list of Session objects
     * @throws SQLException if there is an error during database access
     */
    public List<Session> getAllSessions() throws SQLException {
        return provider.getAllSessions();
    }

    /**
     * Delete a Session from the database by its id. The method returns true if the deletion was successful, or false if no record with the given id was found.
     * Note: if the Session is referenced by other tables, database cascade rules or checks will apply, which may prevent deletion or cause related records to be deleted as well.
     * @param id the id of the Session to be deleted
     * @return true if the deletion was successful, or false if no record with the given id was found
     * @throws SQLException if there is an error during database access
     */
    public boolean deleteSession(long id) throws SQLException {
        // Best-effort: delete rolls associated with this session first (if they are not reused elsewhere)
        try {
            Session existing = provider.getSessionById(id);
            if (existing != null && existing.getRolls() != null) {
                for (Roll r : existing.getRolls()) {
                    if (r != null && r.getId() != 0) {
                        deleteRoll(r.getId());
                    }
                }
            }
        } catch (Exception e) {
            // ignore and attempt delete; DB cascade may handle this
        }
        return provider.deleteSession(id);
    }

    // ======================================================================================================================================
    //                              Roll CRUD
    // ======================================================================================================================================

    /**
     * Save a Roll object to the database. If the Roll has an id of 0, it will be treated as a new record and inserted; otherwise, it will be treated as an existing record and updated.
     * The method also handles saving any nested TechniqueCounts (subs and taps) associated with the Roll, ensuring that they are saved and linked properly. The method returns the saved Roll object with its id field populated (for new records) or unchanged (for updates).
     * @param r the Roll object to be saved
     * @return the saved Roll object
     * @throws SQLException if there is an error during database access
     */
    public Roll saveRoll(Roll r) throws SQLException {
        if (r == null) throw new IllegalArgumentException("Roll is null");

        // Save nested TechniqueCounts (subs and taps). 
        // Each TechniqueCount will ensure its Technique is saved.
        if (r.getSubs() != null) {
            List<TechniqueCount> newSubs = new ArrayList<>();
            for (TechniqueCount tc : r.getSubs()) {
                TechniqueCount saved = saveTechniqueCount(tc);
                newSubs.add(saved);
            }
            r.setSubs(newSubs);
        }

        if (r.getTaps() != null) {
            List<TechniqueCount> newTaps = new ArrayList<>();
            for (TechniqueCount tc : r.getTaps()) {
                TechniqueCount saved = saveTechniqueCount(tc);
                newTaps.add(saved);
            }
            r.setTaps(newTaps);
        }

        if (r.getId() == 0) {
            long newId = provider.saveRoll(r.getId(), r); //TODO: I don't think this is correct; should be session ID???
            r.setId(newId);
        } else {
            boolean ok = provider.updateRoll(r);
            if (!ok) throw new RuntimeException("Failed to update Roll with id " + r.getId());
        }
        return r;
    }

    /**
     * Retrieve a Roll from the database by its id. The method returns the Roll object if found, or null if no record with the given id exists.
     * @param id the id of the Roll to be retrieved
     * @return the Roll object if found, or null if no record with the given id exists 
     * @throws SQLException if there is an error during database access
     */
    public Roll getRollById(long id) throws SQLException {
        return provider.getRollById(id);
    }

    /**
     * Retrieve all Roll records from the database. The method returns a list of Roll objects, which may be empty if no records exist.
     * @return a list of Roll objects
     * @throws SQLException if there is an error during database access
     */
    public List<Roll> getAllRolls() throws SQLException {
        return provider.getAllRolls();
    }

    /**
     * Delete a Roll from the database by its id. The method returns true if the deletion was successful, or false if no record with the given id was found.
     * Note: if the Roll is referenced by Session or other tables, database cascade rules or checks will apply, which may prevent deletion or cause related records to be deleted as well.
     * @param id the id of the Roll to be deleted
     * @return true if the deletion was successful, or false if no record with the given id was found
     * @throws SQLException if there is an error during database access
     */
    public boolean deleteRoll(long id) throws SQLException {
        // Best-effort: fetch roll, delete its technique counts first (if you want)
        try {
            Roll existing = provider.getRollById(id);
            if (existing != null) {
                if (existing.getSubs() != null) {
                    for (TechniqueCount tc : existing.getSubs()) {
                        if (tc != null && tc.getTechnique().getId() != 0) provider.deleteTechniqueLink(id, tc.getTechnique().getId());
                    }
                }
                if (existing.getTaps() != null) {
                    for (TechniqueCount tc : existing.getTaps()) {
                        if (tc != null && tc.getTechnique().getId() != 0) provider.deleteTechniqueLink(id, tc.getTechnique().getId());
                    }
                }
            }
        } catch (Exception e) {
            // ignore and attempt delete; provider or DB may handle cascade
        }
        return provider.deleteRoll(id);
    }

    // ======================================================================================================================================
    //                             Technique CRUD
    // ======================================================================================================================================

    /**
     * Save a Technique object to the database. If the Technique has an id of 0, it will be treated as a new record and inserted;
     * otherwise, it will be treated as an existing record and updated. 
     * The method returns the saved Technique object with its id field populated (for new records) or unchanged (for updates).
     * @param t the Technique object to be saved
     * @return the saved Technique object with its id field populated (for new records) or unchanged (for updates)
     * @throws SQLException if there is an error during database access
     */
    public Technique saveTechnique(Technique t) throws SQLException {
        if (t == null) throw new IllegalArgumentException("Technique is null");
        if (t.getId() == 0) {
            long newId = provider.saveTechnique(t);
            t.setId(newId);
        } else {
            boolean ok = provider.updateTechnique(t);
            if (!ok) throw new RuntimeException("Failed to update Technique with id " + t.getId());
        }
        return t;
    }

    /**
     * Retrieve a Technique from the database by its id. The method returns the Technique object if found, or null if no record with the given id exists.
     * @param id the id of the Technique to be retrieved
     * @return the Technique object if found, or null if no record with the given id exists
     * @throws SQLException if there is an error during database access
     */
    public Technique getTechniqueById(long id) throws SQLException {
        return provider.getTechniqueById(id);
    }

    /**
     * Retrieve all Technique records from the database. The method returns a list of Technique objects, which may be empty if no records exist.
     * @return a list of Technique objects
     * @throws SQLException if there is an error during database access
     */
    public List<Technique> getAllTechniques() throws SQLException {
        return provider.getAllTechniques();
    }

    /**
     * Delete a Technique from the database by its id. The method returns true if the deletion was successful, or false if no record with the given id was found.
     * Note: if the Technique is referenced by TechniqueCount or other tables, database cascade rules or checks will apply, 
     * which may prevent deletion or cause related records to be deleted as well.
     * @param id the id of the Technique to be deleted
     * @return true if the deletion was successful, or false if no record with the given id was found
     * @throws SQLException if there is an error during database access
     */
    public boolean deleteTechnique(long id) throws SQLException {
        // Note: if Technique is referenced by TechniqueCount/other tables, DB cascade or checks apply.
        return provider.deleteTechnique(id);
    }

    // ======================================================================================================================================
    //                        Technique Counts CRUD
    // ======================================================================================================================================

    // Chat notes: TechniqueCount is a bit tricky since it references Technique and is referenced by Roll. 
    // You may want to handle it as part of Roll CRUD operations instead of standalone, depending on your use case.
    // If you want standalone CRUD for TechniqueCount, you need to ensure the referenced Technique exists before 
    // inserting/updating TechniqueCount, and handle cascading deletes if a Technique is deleted.

    /**
     * Save a TechniqueCount object to the database. If the TechniqueCount has an id of 0, it will be treated as a new record and inserted;
     * otherwise, it will be treated as an existing record and updated.
     * @param tc the TechniqueCount object to be saved
     * @return the saved TechniqueCount object with its id field populated (for new records) or unchanged (for updates)
     * @throws SQLException if there is an error during database access
     */
    public TechniqueCount saveTechniqueCount(TechniqueCount tc) throws SQLException {
        if (tc == null) throw new IllegalArgumentException("TechniqueCount is null");

        // Ensure Technique is saved first (so TechniqueCount can reference it)
        Technique tech = tc.getTechnique();
        if (tech != null) {
            tech = saveTechnique(tech); // will insert/update as needed
        }
        provider.saveTechniqueCount(tc.getRollID(), tc.getTechnique().getId(), tc.getCount());

        return tc;
    }

}