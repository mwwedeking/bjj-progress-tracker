# BJJ Progression Tracker

A simple full‑stack application for tracking Brazilian Jiu‑Jitsu
training sessions, rolls, and techniques.

------------------------------------------------------------------------

# Tech Stack

Backend - Java - Spring Boot - Maven

Database - MySQL

Frontend - HTML/CSS/JavaScript (or React depending on version)

------------------------------------------------------------------------

# Prerequisites

Install the following:

-   Java 17
-   Maven
-   MySQL
-   Git
-   Node.js + npm (if using React frontend)

------------------------------------------------------------------------

# Quick Start

## 1 Clone the Repository

    git clone https://github.com/<your-username>/<repo-name>.git
    cd <repo-name>

------------------------------------------------------------------------

## 2 Setup Database

    CREATE DATABASE bjj_progress;
    CREATE USER 'bjj_user'@'localhost' IDENTIFIED BY 'bjj_password';
    GRANT ALL PRIVILEGES ON bjj_progress.* TO 'bjj_user'@'localhost';

Run SQL scripts provided in the repository to create tables.

------------------------------------------------------------------------

## 3 Run Backend

    cd backend
    mvn clean package
    mvn spring-boot:run

Backend runs at:

    http://localhost:8080

------------------------------------------------------------------------

## 4 Run Frontend

    cd frontend
    npm install
    npm start

Frontend usually runs at:

    http://localhost:3000

------------------------------------------------------------------------

# Verify Installation

Backend health check:

    http://localhost:8080/actuator/health

Test API:

    http://localhost:8080/api/techniques

If the frontend loads and data appears, the setup succeeded.

------------------------------------------------------------------------

# Documentation

Full deployment instructions are available in:

    DEPLOYMENT.md

------------------------------------------------------------------------

# License

Add your project license here.
