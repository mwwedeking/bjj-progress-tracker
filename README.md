# BJJ Progress Tracker

A simple full‑stack application for tracking Brazilian Jiu‑Jitsu training.
This app tracks training sessions, rolls you had during that session, as well as technique progression.

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
 
 /bjj-progress-tracker

The "bjj-progress-tracker" file is the overall root directory of the entire project (including both front-end and back-end)

 /bjj-progress-tracker/bjj-frontend

The "bjj-frontend" file is the root directory of the front-end (vite-react) web application client project

 /bjj-progress-tracker/demo

The "demo" file is the root directory of the back-end java (spring-boot) server project 

------------------------------------------------------------------------

# Quick Start

## 1 Clone the Repository

    git clone https://github.com/mwwedeking/bjj-progress-tracker
    cd bjj-progress-tracker

Clone the repository and change to root directory

------------------------------------------------------------------------

## 2 Setup Database 

 db_creation.sql
 sample_data.sql

Run SQL scripts provided in the repository to create database and tables

------------------------------------------------------------------------

## 3 Set-up Environment Variables

 /bjj-frontend/.env

Create a .env file in the front-end
 
 VITE_URL="http://localhost:8080"

Add the VITE_URL environment variable and set it to localhost with desired port
 
 /demo/.env

Create a .env file in the back-end
 
 URL="database-link-goes-here"
 USER="root"
 PASS="database-password-goes-here"

Create the URL, USER, and PASS variables and set them accordingly

------------------------------------------------------------------------

## 3 Run Backend

    cd demo
    mvn clean package
    mvn spring-boot:run

Backend runs at:

    http://localhost:8080

------------------------------------------------------------------------

## 4 Run Frontend

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

    DEPLOYMENT.md

------------------------------------------------------------------------

# License

Add your project license here.
