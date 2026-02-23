-- schema.sql

CREATE DATABASE IF NOT EXISTS bjj_progress_tracker;
USE bjj_progress_tracker;

-- Techniques table
CREATE TABLE IF NOT EXISTS techniques (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  position VARCHAR(255),
  num_finishes INT DEFAULT 0,
  num_taps INT DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sessions table
CREATE TABLE IF NOT EXISTS sessions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  session_date DATE NOT NULL,
  session_time TIME,
  is_gi BOOLEAN DEFAULT TRUE,
  instructor VARCHAR(255),
  currentBelt VARCHAR(50),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Rolls table
CREATE TABLE IF NOT EXISTS rolls (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  session_id BIGINT NOT NULL,
  length_minutes INT,
  partner VARCHAR(255),
  num_rounds INT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (session_id) REFERENCES sessions(id) ON DELETE CASCADE
);

-- Link table between rolls and techniques with counters for subs and taps
CREATE TABLE IF NOT EXISTS roll_technique_links (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  roll_id BIGINT NOT NULL,
  technique_id BIGINT NOT NULL,
  count INT DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY ux_roll_technique (roll_id, technique_id),
  FOREIGN KEY (roll_id) REFERENCES rolls(id) ON DELETE CASCADE,
  FOREIGN KEY (technique_id) REFERENCES techniques(id) ON DELETE CASCADE
);
