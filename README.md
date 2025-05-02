# Student Management System – Java Web Application

## 📌 Project Overview

This is a Java-based web application using a **three-tier architecture**:

- **Presentation Layer**: JSP/HTML/Servlets (optional UI)
- **Application Layer**: Java with JDBC (Data Access Objects, logic)
- **Data Layer**: MySQL database

It enables basic university functions such as managing students, professors, courses, and enrollments. All interactions with the database are performed via JDBC.

---

## 🚀 Setup Instructions

### 1. Install Required Software

- **Java JDK 11 or higher**
- **MySQL Server**
- **Apache Maven**
- **Apache Tomcat** (optional, for web deployment)

---

### 2. Configure Database Connection

Open the file at:  
`app/src/main/resources/db.properties`

Replace the values with your own:

=== (only the port should be changed if using adifferent port)
db.url=jdbc:mysql://localhost:3306/management_sys
db.user=your_mysql_username
db.password=your_mysql_password

3. Set Up the Database
   Using MySQL Workbench or the MySQL CLI:

Run db/create_schema.sql to create tables and schema
Run db/initialize_data.sql to insert sample data

4. Compile and Build the Project
   Open a terminal inside the app/ folder and run:

mvn clean install

5. Run the JDBC Setup Class
   This connects to the DB and confirms setup:
   mvn exec:java

6. Deploy WAR to Tomcat

Copy the generated target/management.war

Start Tomcat

Visit: http://localhost:8080/management/

📦 Dependencies
This project uses the following Maven dependencies:

jakarta.servlet-api:6.0.0
mysql-connector-j:8.0.33
junit:4.11
Java 11+ (configured for Java 22)
All dependencies are handled in pom.xml.

⚙️ Notes
If you rename the database (management_sys), update both:

db.properties
create_schema.sql and initialize_data.sql

Ensure MySQL is running on port 3306 (or update the config)

👥 Authors
Sai
Rome
Jonathan
