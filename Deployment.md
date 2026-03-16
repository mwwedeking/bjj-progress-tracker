# Deployment Guide -- BJJ Progression Tracker

## Overview

This document explains how to download, configure, build, and run the
full BJJ Progression Tracker project. It assumes the reader has basic
command-line familiarity but no prior knowledge of the project.

The project includes: - Backend: Java Spring Boot application -
Database: MySQL - Frontend: Web client (HTML/CSS/JS or Node/React)

------------------------------------------------------------------------

# Prerequisites

Install the following before running the project.

## Development Tools

-   Git
-   IDE (IntelliJ IDEA recommended, VS Code or Eclipse also work)

## Backend Requirements

-   Java 17
-   Maven 3.6+

Verify installations:

    java -version
    mvn -v

## Database

-   MySQL 8.x or MariaDB
-   Default port: 3306

## Frontend (if applicable)

-   Node.js 18+
-   npm

Optional tools: - Postman or curl - MySQL Workbench

------------------------------------------------------------------------

# Step 1 -- Download the Project

Option 1: Download ZIP from GitHub.

Option 2: Clone repository:

    git clone https://github.com/<your-username>/<repo-name>.git
    cd <repo-name>

------------------------------------------------------------------------

# Step 2 -- Database Setup

Start MySQL and create the project database.

    CREATE DATABASE bjj_progress;
    CREATE USER 'bjj_user'@'localhost' IDENTIFIED BY 'bjj_password';
    GRANT ALL PRIVILEGES ON bjj_progress.* TO 'bjj_user'@'localhost';
    FLUSH PRIVILEGES;

Run the SQL schema scripts included in the repository.

Example:

    mysql -u bjj_user -p bjj_progress < schema.sql
    mysql -u bjj_user -p bjj_progress < test-data.sql

Verify tables:

    USE bjj_progress;
    SHOW TABLES;

------------------------------------------------------------------------

# Step 3 -- Configure the Backend

Open:

    src/main/resources/application.properties

Configure database connection:

    spring.datasource.url=jdbc:mysql://localhost:3306/bjj_progress
    spring.datasource.username=bjj_user
    spring.datasource.password=bjj_password
    server.port=8080

------------------------------------------------------------------------

# Step 4 -- Build the Backend

Navigate to the backend project directory and run:

    mvn clean package

Run the application:

    mvn spring-boot:run

Or run the built JAR:

    java -jar target/*.jar

The API should start on:

    http://localhost:8080

------------------------------------------------------------------------

# Step 5 -- Run the Frontend

Navigate to the frontend folder.

If the project uses Node/React:

    npm install
    npm start

Typical dev URL:

    http://localhost:3000

If the project is static HTML:

    npx serve .

------------------------------------------------------------------------

# Step 6 -- Verify the System Works

Check backend health endpoint:

    http://localhost:8080/actuator/health

Expected response:

    {"status":"UP"}

Test an API endpoint:

    http://localhost:8080/api/techniques

Open the frontend in a browser and verify data loads.

------------------------------------------------------------------------

# Troubleshooting

Common issues:

Port already in use\
Change Spring Boot port:

    server.port=8081

Database connection errors\
Ensure MySQL is running and credentials match.

CORS errors\
Enable CORS in the Spring Boot controller or configuration.

------------------------------------------------------------------------

# Deployment Checklist

-   Java 17 installed
-   Maven installed
-   MySQL database created
-   Tables successfully created
-   Backend builds and runs
-   API endpoints return JSON
-   Frontend loads successfully
