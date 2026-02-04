package com.example;

// DataProvider.java
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DataProvider {
    // Change these to your DB's credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bjj_progress_tracker?serverTimezone=UTC";
    private static final String DB_USER = "root";
    // private static final String DB_PASSWORD = System.getenv("PASS");
    private static final String DB_PASSWORD = "password";

    public DataProvider() throws ClassNotFoundException {
        // load driver (optional with modern drivers)
        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // ========== Technique CRUD ==========
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

    public List<Technique> readAllTechniques() throws SQLException {
        List<Technique> list = new ArrayList<>();
        String sql = "SELECT * FROM techniques";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapTechnique(rs));
        }
        return list;
    }

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

    public boolean deleteTechnique(long id) throws SQLException {
        String sql = "DELETE FROM techniques WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

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

    public boolean deleteSession(long id) throws SQLException {
        String sql = "DELETE FROM sessions WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

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
    public long createRoll(long sessionId, Roll r) throws SQLException {
        return createRollWithSession(sessionId, r);
    }

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

    public boolean deleteRoll(long id) throws SQLException {
        String sql = "DELETE FROM rolls WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Roll mapRoll(ResultSet rs) throws SQLException {
        Roll r = new Roll();
        r.setId(rs.getLong("id"));
        r.setLengthMinutes(rs.getInt("length_minutes"));
        r.setPartner(rs.getString("partner"));
        r.setNumRounds(rs.getInt("num_rounds"));
        return r;
    }

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
    // populate both subs and taps lists for the roll using technique rows
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

    // upsert technique counts for a roll from the Roll object's lists
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

    // upsert single technique-count for a roll (useful if incrementing)
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

    // update aggregate counts on technique (example helper if you want to update technique.total fields)
    public boolean updateTechniqueAggregates(long techniqueId, int newNumFinishes, int newNumTaps) throws SQLException {
        String sql = "UPDATE techniques SET num_finishes = ?, num_taps = ? WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, newNumFinishes);
            ps.setInt(2, newNumTaps);
            ps.setLong(3, techniqueId);
            return ps.executeUpdate() > 0;
        }
    }

    // delete link row for technique+roll
    public boolean deleteRollTechniqueLink(long rollId, long techniqueId) throws SQLException {
        String sql = "DELETE FROM roll_technique_links WHERE roll_id = ? AND technique_id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, rollId);
            ps.setLong(2, techniqueId);
            return ps.executeUpdate() > 0;
        }
    }

    // ========== additional helpers ==========
    // Fetch technique counts for one roll as a map
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

