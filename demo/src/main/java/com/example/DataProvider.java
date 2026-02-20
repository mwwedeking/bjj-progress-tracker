package com.example;

// Import statements for the DataProvider class
import java.sql.*;
import java.sql.Date;
import java.time.LocalTime;
import java.util.*;

/**
 * The DataProvider class is responsible for managing the connection to the MySQL database 
 * and providing methods to perform CRUD (Create, Read, Update, Delete) operations 
 * on the Technique, Session, and Roll entities. It also includes methods to manage 
 * the relationships between rolls and techniques, such as populating technique counts for rolls 
 * and upserting technique counts for rolls. This class serves as the data access layer for the BJJ Progress Tracker application, 
 * allowing other parts of the application to interact with the database in a structured and organized manner.
 */
public class DataProvider {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bjj_progress_tracker?serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";
    // private static final String DB_PASSWORD = System.getenv("PASS");

    /**
     * Constructor for DataProvider class that loads the MySQL JDBC driver
     * @throws ClassNotFoundException if the MySQL JDBC driver class is not found
     */
    public DataProvider() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver"); // load driver (optional with modern drivers)
    }

    /**
     * Helper method to establish a connection to the MySQL database
     * @return a Connection object to the MySQL database
     * @throws SQLException if a database access error occurs or the url is null
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // ========== Technique CRUD ==========

    /**
     * Creates a new technique in the database and returns the generated ID.
     * @param t the technique to be created
     * @return the generated ID of the technique
     * @throws SQLException if a database access error occurs
     */
    public long createTechnique(Technique t) throws SQLException {
        String sql = "INSERT INTO techniques (name, position, num_finishes, num_taps) VALUES (?, ?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getName());
            ps.setString(2, t.getPosition());
            ps.setInt(3, t.getNumFinishes());
            ps.setInt(4, t.getNumTaps());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    t.setId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    /**
     * Reads a technique from the database by its ID and returns a Technique object.
     * @param id the ID of the technique to be read
     * @return a Technique object representing the technique with the specified ID, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public Technique readTechniqueById(long id) throws SQLException {
        String sql = "SELECT * FROM techniques WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapTechnique(rs);
            }
        }
        return null;
    }

    /**
     * Reads all techniques from the database and returns a list of Technique objects.
     * @return a list of Technique objects representing all techniques in the database
     * @throws SQLException if a database access error occurs
     */
    public List<Technique> readAllTechniques() throws SQLException {
        List<Technique> list = new ArrayList<>();
        String sql = "SELECT * FROM techniques";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapTechnique(rs));
        }
        return list;
    }

    /**
     * Updates an existing technique in the database with the values from the provided Technique object.
     * @param t the Technique object with updated values
     * @return true if the update was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean updateTechnique(Technique t) throws SQLException {
        String sql = "UPDATE techniques SET name = ?, position = ?, num_finishes = ?, num_taps = ? WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, t.getName());
            ps.setString(2, t.getPosition());
            ps.setInt(3, t.getNumFinishes());
            ps.setInt(4, t.getNumTaps());
            ps.setLong(5, t.getId());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Deletes a technique from the database by its ID.
     * @param id the ID of the technique to be deleted
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean deleteTechnique(long id) throws SQLException {
        String sql = "DELETE FROM techniques WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Helper method to map a ResultSet row to a Technique object.
     * @param rs the ResultSet to be mapped
     * @return a Technique object representing the row in the ResultSet
     * @throws SQLException if a database access error occurs
     */
    private Technique mapTechnique(ResultSet rs) throws SQLException {
        Technique t = new Technique();
        t.setId(rs.getLong("id"));
        t.setName(rs.getString("name"));
        t.setPosition(rs.getString("position"));
        t.setNumFinishes(rs.getInt("num_finishes"));
        t.setNumTaps(rs.getInt("num_taps"));
        return t;
    }

    // ========== Session CRUD (and read rolls) ==========

    /**
     * Creates a new session in the database along with its associated rolls and returns the generated session ID.
     * @param s the Session object to be created, which may include a list of Roll objects to be associated with the session
     * @return the generated ID of the created session, or -1 if the creation failed
     * @throws SQLException if a database access error occurs
     */
    public long createSession(Session s) throws SQLException {
        String sql = "INSERT INTO sessions (session_date, session_time, is_gi, instructor, rank) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(s.getDate()));
            ps.setTime(2, Time.valueOf(s.getTime()));
            ps.setBoolean(3, s.isGi());
            ps.setString(4, s.getInstructor());
            ps.setString(5, s.getRank());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    s.setId(id);
                    // create rolls if any
                    for (Roll r : s.getRolls()) {
                        r.setId(createRollWithSession(id, r)); // sets id
                    }
                    return id;
                }
            }
        }
        return -1;
    }

    /**
     * Helper method to create a roll associated with a specific session. 
     * This method inserts the roll into the database and also handles the insertion of technique links for the roll if they are present.
     * @param sessionId the ID of the session to which the roll is associated
     * @param r the Roll object to be created, which may include lists of TechniqueCount objects for subs and taps
     * @return the generated ID of the created roll, or -1 if the creation failed
     * @throws SQLException if a database access error occurs
     */
    private long createRollWithSession(long sessionId, Roll r) throws SQLException {
        String sql = "INSERT INTO rolls (session_id, length_minutes, partner, num_rounds) VALUES (?, ?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, sessionId);
            ps.setInt(2, r.getLengthMinutes());
            ps.setString(3, r.getPartner());
            ps.setInt(4, r.getNumRounds());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long rollId = rs.getLong(1);
                    r.setId(rollId);
                    // insert technique links if present
                    upsertTechniqueCountsForRoll(rollId, r);
                    return rollId;
                }
            }
        }
        return -1;
    }

    /**
     * Reads a session from the database by its ID, including its associated rolls and their technique counts, and returns a Session object.
     * @param id the ID of the session to be read
     * @return the Session object if found, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public Session readSessionById(long id) throws SQLException {
        String sql = "SELECT * FROM sessions WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Session s = mapSession(rs);
                    s.setRolls(readRollsForSession(id));
                    return s;
                }
            }
        }
        return null;
    }

    /**
     * Reads all sessions from the database, including their associated rolls and technique counts, and returns a list of Session objects.
     * @return a list of Session objects
     * @throws SQLException if a database access error occurs
     */
    public List<Session> readAllSessions() throws SQLException {
        List<Session> out = new ArrayList<>();
        String sql = "SELECT * FROM sessions ORDER BY session_date DESC";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Session s = mapSession(rs);
                s.setRolls(readRollsForSession(s.getId()));
                out.add(s);
            }
        }
        return out;
    }

    /**
     * Updates an existing session in the database with the values from the provided Session object.
     * @param s the Session object to be updated
     * @return true if the update was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean updateSession(Session s) throws SQLException {
        String sql = "UPDATE sessions SET session_date = ?, session_time = ?, is_gi = ?, instructor = ?, rank = ? WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(s.getDate()));
            ps.setTime(2, Time.valueOf(s.getTime()));
            ps.setBoolean(3, s.isGi());
            ps.setString(4, s.getInstructor());
            ps.setString(5, s.getRank());
            ps.setLong(6, s.getId());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Deletes a session from the database by its ID. This method also deletes all rolls associated with the session due to the ON DELETE CASCADE constraint in the database schema.
     * @param id the ID of the session to be deleted
     * @return true if the deletion was successful, false otherwise
     */
    public boolean deleteSession(long id) throws SQLException {
        String sql = "DELETE FROM sessions WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Helper method to map a ResultSet row to a Session object. This method is used internally to convert database rows into Session objects when reading from the database.
     * @param rs the ResultSet containing the session data to be mapped
     * @return a Session object representing the data in the ResultSet row
     * @throws SQLException if a database access error occurs while reading from the ResultSet
     */
    private Session mapSession(ResultSet rs) throws SQLException {
        Session s = new Session();
        s.setId(rs.getLong("id"));
        s.setDate(rs.getDate("session_date").toLocalDate());
        Time t = rs.getTime("session_time");
        s.setTime(t != null ? t.toLocalTime() : LocalTime.MIDNIGHT);
        s.setGi(rs.getBoolean("is_gi"));
        s.setInstructor(rs.getString("instructor"));
        s.setRank(rs.getString("rank"));
        return s;
    }

    // ========== Roll CRUD ==========

    /**
     * Creates a new roll in the database associated with a specific session. 
     * This method is a helper that calls createRollWithSession to handle the insertion of the roll and its technique links.
     * @param sessionId the ID of the session to which the roll is associated
     * @param r the Roll object to be created, which may include lists of TechniqueCount objects for subs and taps
     * @return the generated ID of the created roll, or -1 if the creation failed
     * @throws SQLException if a database access error occurs
     */
    public long createRoll(long sessionId, Roll r) throws SQLException {
        return createRollWithSession(sessionId, r);
    }

    /**
     * Reads a roll from the database by its ID, including its associated technique counts, and returns a Roll object.
     * @param id the ID of the roll to be read
     * @return the Roll object if found, or null if not found
     * @throws SQLException if a database access error occurs
     */
    public Roll readRollById(long id) throws SQLException {
        String sql = "SELECT * FROM rolls WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Roll r = mapRoll(rs);
                    populateTechniqueCountsForRoll(r);
                    return r;
                }
            }
        }
        return null;
    }

    /**
     * Reads all rolls from the database, including their associated technique counts, and returns a list of Roll objects.
     * @return a list of Roll objects
     * @throws SQLException if a database access error occurs
     */
    public List<Roll> readAllRolls() throws SQLException {
        List<Roll> out = new ArrayList<>();
        String sql = "SELECT * FROM rolls ORDER BY created_at DESC";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Roll r = mapRoll(rs);
                populateTechniqueCountsForRoll(r);
                out.add(r);
            }
        }
        return out;
    }

    /**
     * Updates an existing roll in the database with the values from the provided Roll object. 
     * This method also updates the associated technique counts for the roll.
     * @param r the Roll object with updated values, including its ID which identifies the roll to be updated in the database
     * @return true if the update was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean updateRoll(Roll r) throws SQLException {
        String sql = "UPDATE rolls SET length_minutes = ?, partner = ?, num_rounds = ? WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, r.getLengthMinutes());
            ps.setString(2, r.getPartner());
            ps.setInt(3, r.getNumRounds());
            ps.setLong(4, r.getId());
            boolean ok = ps.executeUpdate() > 0;
            if (ok) {
                upsertTechniqueCountsForRoll(r.getId(), r); // persist counters
            }
            return ok;
        }
    }

    /**
     * Deletes a roll from the database by its ID. 
     * This method also deletes all technique links associated with the roll due to the ON DELETE CASCADE constraint in the database schema.
     * @param id the ID of the roll to be deleted
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean deleteRoll(long id) throws SQLException {
        String sql = "DELETE FROM rolls WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Helper method to map a ResultSet row to a Roll object. 
     * This method is used internally to convert database rows into Roll objects when reading from the database.
     * @param rs the ResultSet containing the roll data to be mapped
     * @return a Roll object representing the data in the ResultSet row
     * @throws SQLException if a database access error occurs while reading from the ResultSet
     */
    private Roll mapRoll(ResultSet rs) throws SQLException {
        Roll r = new Roll();
        r.setId(rs.getLong("id"));
        r.setLengthMinutes(rs.getInt("length_minutes"));
        r.setPartner(rs.getString("partner"));
        r.setNumRounds(rs.getInt("num_rounds"));
        return r;
    }
 
    /**
    * Helper method to read all rolls associated with a specific session ID from the database, including their associated technique counts, and returns a list of Roll objects.
    * @param sessionId the ID of the session for which to read the rolls
    * @return a list of Roll objects associated with the specified session ID
    * @throws SQLException if a database access error occurs
    */
    private List<Roll> readRollsForSession(long sessionId) throws SQLException {
        List<Roll> out = new ArrayList<>();
        String sql = "SELECT * FROM rolls WHERE session_id = ? ORDER BY created_at";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, sessionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Roll r = mapRoll(rs);
                    populateTechniqueCountsForRoll(r);
                    out.add(r);
                }
            }
        }
        return out;
    }

    // ========== roll_technique_links operations ==========

    /**
     * Helper method to populate the subs and taps lists of a Roll object with TechniqueCount objects based on the data in the roll_technique_links table for the given roll.
     * @param roll the Roll object for which to populate the technique counts. The roll's ID must be set, and this method will fill the subs and taps lists based on the database data.
     * @throws SQLException if a database access error occurs
     */
    private void populateTechniqueCountsForRoll(Roll roll) throws SQLException {
        String sql = "SELECT rtl.*, t.name, t.position, t.num_finishes, t.num_taps " +
                     "FROM roll_technique_links rtl JOIN techniques t ON rtl.technique_id = t.id " +
                     "WHERE rtl.roll_id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, roll.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Technique t = new Technique();
                    t.setId(rs.getLong("technique_id"));
                    t.setName(rs.getString("name"));
                    t.setPosition(rs.getString("position"));
                    t.setNumFinishes(rs.getInt("num_finishes"));
                    t.setNumTaps(rs.getInt("num_taps"));
                    int subs = rs.getInt("subs_count");
                    int taps = rs.getInt("taps_count");
                    if (subs > 0) roll.getSubs().add(new TechniqueCount(t, subs));
                    if (taps > 0) roll.getTaps().add(new TechniqueCount(t, taps));
                }
            }
        }
    }

    /**
     * Helper method to upsert (insert or update) the technique counts for a given roll based on the subs and taps lists in the Roll object.
     * @param rollId the ID of the roll for which to upsert the technique counts. This ID must correspond to an existing roll in the database.
     * @param r the Roll object containing the subs and taps lists to be upserted
     * @throws SQLException if a database access error occurs
     */
    private void upsertTechniqueCountsForRoll(long rollId, Roll r) throws SQLException {
        // gather per technique id the subs and taps
        Map<Long, Integer> subsMap = new HashMap<>();
        for (TechniqueCount tc : r.getSubs()) subsMap.put(tc.getTechnique().getId(), tc.getCount());
        Map<Long, Integer> tapsMap = new HashMap<>();
        for (TechniqueCount tc : r.getTaps()) tapsMap.put(tc.getTechnique().getId(), tc.getCount());

        // union of technique ids
        Set<Long> techniqueIds = new HashSet<>();
        techniqueIds.addAll(subsMap.keySet());
        techniqueIds.addAll(tapsMap.keySet());

        String selectSql = "SELECT id FROM roll_technique_links WHERE roll_id = ? AND technique_id = ?";
        String insertSql = "INSERT INTO roll_technique_links (roll_id, technique_id, subs_count, taps_count) VALUES (?, ?, ?, ?)";
        String updateSql = "UPDATE roll_technique_links SET subs_count = ?, taps_count = ? WHERE roll_id = ? AND technique_id = ?";

        try (Connection c = getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement psSelect = c.prepareStatement(selectSql);
                 PreparedStatement psInsert = c.prepareStatement(insertSql);
                 PreparedStatement psUpdate = c.prepareStatement(updateSql)) {

                for (Long techId : techniqueIds) {
                    int subs = subsMap.getOrDefault(techId, 0);
                    int taps = tapsMap.getOrDefault(techId, 0);

                    psSelect.setLong(1, rollId);
                    psSelect.setLong(2, techId);
                    try (ResultSet rs = psSelect.executeQuery()) {
                        if (rs.next()) { // update
                            psUpdate.setInt(1, subs);
                            psUpdate.setInt(2, taps);
                            psUpdate.setLong(3, rollId);
                            psUpdate.setLong(4, techId);
                            psUpdate.executeUpdate();
                        } else { // insert
                            psInsert.setLong(1, rollId);
                            psInsert.setLong(2, techId);
                            psInsert.setInt(3, subs);
                            psInsert.setInt(4, taps);
                            psInsert.executeUpdate();
                        }
                    }
                }
                c.commit();
            } catch (SQLException ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    /**
     * Helper method to upsert (insert or update) the technique counts for a single technique associated with a given roll. 
     * @param rollId the ID of the roll for which to upsert the technique count. This ID must correspond to an existing roll in the database.
     * @param techniqueId the ID of the technique for which to upsert the count. This ID must correspond to an existing technique in the database.
     * @param subsCount the number of subs to be upserted for the given technique and roll.
     * @param tapsCount the number of taps to be upserted for the given technique and roll.
     * @throws SQLException if a database access error occurs
     */
    public void upsertSingleTechniqueCount(long rollId, long techniqueId, int subsCount, int tapsCount) throws SQLException {
        String selectSql = "SELECT id FROM roll_technique_links WHERE roll_id = ? AND technique_id = ?";
        String insertSql = "INSERT INTO roll_technique_links (roll_id, technique_id, subs_count, taps_count) VALUES (?, ?, ?, ?)";
        String updateSql = "UPDATE roll_technique_links SET subs_count = subs_count + ?, taps_count = taps_count + ? WHERE roll_id = ? AND technique_id = ?";
        try (Connection c = getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement psSelect = c.prepareStatement(selectSql);
                 PreparedStatement psInsert = c.prepareStatement(insertSql);
                 PreparedStatement psUpdate = c.prepareStatement(updateSql)) {

                psSelect.setLong(1, rollId);
                psSelect.setLong(2, techniqueId);
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) { // update - add to existing counters
                        psUpdate.setInt(1, subsCount);
                        psUpdate.setInt(2, tapsCount);
                        psUpdate.setLong(3, rollId);
                        psUpdate.setLong(4, techniqueId);
                        psUpdate.executeUpdate();
                    } else { // insert with provided absolute counters
                        psInsert.setLong(1, rollId);
                        psInsert.setLong(2, techniqueId);
                        psInsert.setInt(3, subsCount);
                        psInsert.setInt(4, tapsCount);
                        psInsert.executeUpdate();
                    }
                }
                c.commit();
            } catch (SQLException ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    /**
     * Helper method to update the aggregate technique counts (num_finishes and num_taps) for a given technique based on the provided new counts.
     * @param techniqueId the ID of the technique for which to update the aggregate counts. This ID must correspond to an existing technique in the database.
     * @param newNumFinishes the new total number of finishes to be set for the technique. This value should represent the updated aggregate count of finishes for the technique across all rolls.
     * @param newNumTaps the new total number of taps to be set for the technique. This value should represent the updated aggregate count of taps for the technique across all rolls.
     * @return true if the technique was updated, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean updateTechniqueAggregates(long techniqueId, int newNumFinishes, int newNumTaps) throws SQLException {
        String sql = "UPDATE techniques SET num_finishes = ?, num_taps = ? WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, newNumFinishes);
            ps.setInt(2, newNumTaps);
            ps.setLong(3, techniqueId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Helper method to delete the link between a roll and a technique from the roll_technique_links table. This method can be used to remove a technique from a roll's subs or taps lists.
     * @param rollId the ID of the roll for which to delete the technique link. This ID must correspond to an existing roll in the database.
     * @param techniqueId the ID of the technique to be removed from the roll. This ID must correspond to an existing technique in the database.
     * @return true if the link was deleted, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean deleteRollTechniqueLink(long rollId, long techniqueId) throws SQLException {
        String sql = "DELETE FROM roll_technique_links WHERE roll_id = ? AND technique_id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, rollId);
            ps.setLong(2, techniqueId);
            return ps.executeUpdate() > 0;
        }
    }

    // ========== additional helpers ==========

    /**
     * Helper method to fetch a map of technique IDs to TechniqueCount objects for the subs associated with a given roll.
     * @param rollId the ID of the roll for which to fetch the subs map. This ID must correspond to an existing roll in the database.
     * @return a map of technique IDs to TechniqueCount objects for the subs associated with the given roll
     * @throws SQLException if a database access error occurs
     */
    public Map<Long, TechniqueCount> fetchSubsMapForRoll(long rollId) throws SQLException {
        Map<Long, TechniqueCount> map = new HashMap<>();
        String sql = "SELECT rtl.technique_id, rtl.subs_count, t.name, t.position, t.num_finishes, t.num_taps " +
                     "FROM roll_technique_links rtl JOIN techniques t ON rtl.technique_id = t.id WHERE rtl.roll_id = ? AND rtl.subs_count > 0";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, rollId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Technique t = new Technique();
                    t.setId(rs.getLong("technique_id"));
                    t.setName(rs.getString("name"));
                    t.setPosition(rs.getString("position"));
                    t.setNumFinishes(rs.getInt("num_finishes"));
                    t.setNumTaps(rs.getInt("num_taps"));
                    int subs = rs.getInt("subs_count");
                    map.put(t.getId(), new TechniqueCount(t, subs));
                }
            }
        }
        return map;
    }

    /**
     * Helper method to fetch a map of technique IDs to TechniqueCount objects for the taps associated with a given roll.
     * @param rollId the ID of the roll for which to fetch the taps map. This ID must correspond to an existing roll in the database.
     * @return a map of technique IDs to TechniqueCount objects for the taps associated with the given roll
     * @throws SQLException if a database access error occurs
     */
    public Map<Long, TechniqueCount> fetchTapsMapForRoll(long rollId) throws SQLException {
        Map<Long, TechniqueCount> map = new HashMap<>();
        String sql = "SELECT rtl.technique_id, rtl.taps_count, t.name, t.position, t.num_finishes, t.num_taps " +
                     "FROM roll_technique_links rtl JOIN techniques t ON rtl.technique_id = t.id WHERE rtl.roll_id = ? AND rtl.taps_count > 0";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, rollId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Technique t = new Technique();
                    t.setId(rs.getLong("technique_id"));
                    t.setName(rs.getString("name"));
                    t.setPosition(rs.getString("position"));
                    t.setNumFinishes(rs.getInt("num_finishes"));
                    t.setNumTaps(rs.getInt("num_taps"));
                    int taps = rs.getInt("taps_count");
                    map.put(t.getId(), new TechniqueCount(t, taps));
                }
            }
        }
        return map;
    }
}

