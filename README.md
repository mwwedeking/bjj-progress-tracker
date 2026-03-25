# BJJ Progress Tracker

A simple full‑stack application for tracking Brazilian Jiu‑Jitsu training.
This app tracks training sessions, rolls you had during that session, as well as technique progression.
This project is a monolithic repository containing both front-end and back-end projects.

------------------------------------------------------------------------

# Tech Stack

Frontend - HTML/CSS/JavaScript (Vite React)

Backend - Maven - Java (Spring Boot)

Database - MySQL (MySQL Workbench)

------------------------------------------------------------------------

# Dependencies

Install the following dependencies:

-   Java 17
-   Maven
-   MySQL
-   Git
-   Node.js + npm (if using React frontend)

------------------------------------------------------------------------

# Architecture
 
**bjj-progress-tracker** (Monorepo)

- Overall root directory of the entire project (including both front-end and back-end)

**bjj-frontend**

- Root directory of the front-end (vite-react) web application client project

**demo**

- Root directory of the back-end java (spring-boot) server project 

------------------------------------------------------------------------

# Quick Start

## 1 Clone the Repository

    git clone https://github.com/mwwedeking/bjj-progress-tracker
    cd bjj-progress-tracker

Clone the repository and change to root directory

------------------------------------------------------------------------

## 2 Setup Database 

 1) **db_creation.sql**

Run this script to create the database
  
 2) **sample_data.sql**

Run this script to create the database tables and insert all corresponding example data

------------------------------------------------------------------------

## 3 Set-up Environment Variables

`/bjj-frontend/.env`

Create a `.env` file at the root directory of the front-end
 
    VITE_URL="http://localhost:8080"

Add the VITE_URL environment variable and set it to localhost with desired port

`/demo/.env`  

Create a `.env` file at the root directory of the back-end
 
    URL="<database-link>"
    USER="<database-username>"
    PASS="<database-password>"

Create the URL, USER, and PASS variables and set them to the database credentials

------------------------------------------------------------------------

## 3 Run Backend

In a terminal instance

    cd demo
    mvn clean package
    mvn spring-boot:run

Backend runs at:

    http://localhost:8080

------------------------------------------------------------------------

## 4 Run Frontend

*Note:* In a separate terminal instance from the back-end

    cd bjj-frontend
    npm install
    npm run dev

Frontend runs at:

    http://localhost:5173

------------------------------------------------------------------------

# Verify Installation

If the frontend loads and data appears, the setup succeeded.

------------------------------------------------------------------------

# Documentation

Full deployment instructions are available in:

(Needs updated)

    DEPLOYMENT.md

------------------------------------------------------------------------

# License

Add your project license here.
