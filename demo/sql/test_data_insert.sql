-- test_data.sql
USE bjj_progress_tracker;

-- Techniques
INSERT INTO techniques (name, position, num_finishes, num_taps)
VALUES
  ('Armbar', 'Guard', 5, 12),
  ('Triangle', 'Guard', 3, 8),
  ('Rear Naked Choke', 'Back', 7, 15),
  ('Kimura', 'Top', 2, 4);

-- Sessions
INSERT INTO sessions (session_date, session_time, is_gi, instructor, currentBelt)
VALUES
  ('2026-01-20', '18:30:00', TRUE, 'Professor Silva', 'Blue'),
  ('2026-01-27', '19:00:00', FALSE, 'Coach Mendes', 'Purple');

-- Rolls (link to sessions)
INSERT INTO rolls (session_id, length_minutes, partner, num_rounds)
VALUES
  (1, 300, 'Partner A', 3),
  (1, 180, 'Partner B', 2),
  (2, 360, 'Partner C', 4);

-- roll_technique_links: e.g., in roll 1 executed Armbar twice and tapped once
INSERT INTO roll_technique_links (roll_id, technique_id, subs_count, taps_count)
VALUES
  (1, 1, 2),  -- Roll 1: Armbar subs=2, taps=1
  (1, 2, 1),  -- Roll 1: Triangle subs=1
  (2, 3, 0),  -- Roll 2: saw back control but no subs/taps
  (3, 1, 1),  -- Roll 3: Armbar attempted once
  (3, 4, 0);  -- Roll 3: Kimura tapped once
