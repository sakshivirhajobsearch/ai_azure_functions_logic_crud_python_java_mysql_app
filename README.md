# AI + Azure Functions & Logic Apps CRUD Application

## Technologies:
- Python Flask API
- Java Swing GUI
- MySQL Database
- OpenAI GPT-4

## Features:
- CRUD operations on Azure Functions & Logic Apps
- AI analysis of Function responses using OpenAI
- Full GUI with tables and popups

## Setup

### 1. MySQL DB

```sql
CREATE DATABASE azure_crud;
USE azure_crud;

CREATE TABLE azure_functions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    url TEXT
);

CREATE TABLE logic_apps (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    url TEXT
);
