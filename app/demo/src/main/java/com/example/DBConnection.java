package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.io.InputStream;

public class DBConnection {

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();

        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("Could not find db.properties");
            }

            props.load(input);
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String pass = props.getProperty("db.password");

            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read DB config", e);
        }
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            System.out.println("Connected to database successfully!");
    
            // Drop and recreate schema
            stmt.execute("DROP DATABASE IF EXISTS management_sys");
            stmt.execute("CREATE DATABASE management_sys");
            stmt.execute("USE management_sys");
    
            // Create tables
            stmt.execute("""
                CREATE TABLE professor (
                    id INT AUTO_INCREMENT,
                    name VARCHAR(255) NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    username VARCHAR(50) NOT NULL,
                    PRIMARY KEY (id)
                )
            """);
    
            stmt.execute("""
                CREATE TABLE student (
                    id INT AUTO_INCREMENT,
                    name VARCHAR(255) NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    year INT NOT NULL,
                    gpa FLOAT NOT NULL,
                    username VARCHAR(50) NOT NULL,
                    PRIMARY KEY (id)
                )
            """);
    
            stmt.execute("""
                CREATE TABLE course (
                    id VARCHAR(10),
                    size INT NOT NULL,
                    start_time VARCHAR(10) NOT NULL,
                    name VARCHAR(255) NOT NULL,
                    professor_id INT NOT NULL,
                    end_time VARCHAR(10) NOT NULL,
                    PRIMARY KEY (id),
                    FOREIGN KEY (professor_id) REFERENCES professor(id)
                )
            """);
    
            stmt.execute("""
                CREATE TABLE enrollment (
                    id INT AUTO_INCREMENT,
                    grade VARCHAR(20) NOT NULL,
                    student_id INT NOT NULL,
                    course_id VARCHAR(10) NOT NULL,
                    PRIMARY KEY (id),
                    FOREIGN KEY (student_id) REFERENCES student(id),
                    FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
                )
            """);
    
            // Insert professors
            stmt.execute("""
                INSERT INTO professor (id, username, name, email, password) VALUES
                (1, 'jsmith', 'John Smith', 'jsmith@university.edu', 'pass123'),
                (2, 'adoe', 'Alice Doe', 'adoe@university.edu', 'secure456'),
                (3, 'rlee', 'Robert Lee', 'rlee@university.edu', 'qwerty789'),
                (4, 'mwilson', 'Mary Wilson', 'mwilson@university.edu', 'abc123'),
                (5, 'tjohnson', 'Tom Johnson', 'tjohnson@university.edu', 'pass321'),
                (6, 'kpatel', 'Kiran Patel', 'kpatel@university.edu', 'hello123'),
                (7, 'nbrown', 'Nina Brown', 'nbrown@university.edu', 'letmein22'),
                (8, 'pscott', 'Paul Scott', 'pscott@university.edu', 'login789'),
                (9, 'lthomas', 'Laura Thomas', 'lthomas@university.edu', 'mypassword'),
                (10, 'dgarcia', 'David Garcia', 'dgarcia@university.edu', 'testpass'),
                (11, 'csimmons', 'Cathy Simmons', 'csimmons@university.edu', 'welcome456'),
                (12, 'hwang', 'Henry Wang', 'hwang@university.edu', 'openup999'),
                (13, 'bkim', 'Betty Kim', 'bkim@university.edu', 'profpass'),
                (14, 'jlopez', 'Jose Lopez', 'jlopez@university.edu', 'sunshine'),
                (15, 'rwilson', 'Rachel Wilson', 'rwilson@university.edu', 'teach123')
            """);
    
            // Insert students
            stmt.execute("""
                INSERT INTO student (id, username, name, password, year, gpa) VALUES
                (1, 'slee', 'Samuel Lee', 'pass001', 1, 3.2),
                (2, 'jchan', 'Jenny Chan', 'pass002', 2, 3.5),
                (3, 'knguyen', 'Kevin Nguyen', 'pass003', 3, 3.8),
                (4, 'mlara', 'Maria Lara', 'pass004', 1, 2.9),
                (5, 'dwhite', 'Daniel White', 'pass005', 4, 3.7),
                (6, 'ashah', 'Anita Shah', 'pass006', 2, 3.1),
                (7, 'bramos', 'Brian Ramos', 'pass007', 3, 3.0),
                (8, 'jgreen', 'Jill Green', 'pass008', 1, 3.3),
                (9, 'tmorris', 'Tom Morris', 'pass009', 2, 2.8),
                (10, 'llee', 'Linda Lee', 'pass010', 4, 3.9),
                (11, 'csmith', 'Chris Smith', 'pass011', 3, 3.6),
                (12, 'kali', 'Kiran Ali', 'pass012', 2, 3.4),
                (13, 'npatel', 'Nina Patel', 'pass013', 1, 3.0),
                (14, 'jwang', 'Jason Wang', 'pass014', 3, 2.7),
                (15, 'aroberts', 'Alice Roberts', 'pass015', 4, 3.85)
            """);
    
            // Insert courses
            stmt.execute("""
                INSERT INTO course (id, name, start_time, end_time, size, professor_id) VALUES
                ('CSE101', 'Intro to Computer Science', '8:00 AM', '9:15 AM', 50, 1),
                ('CSE102', 'Data Structures', '9:30 AM', '10:45 AM', 45, 2),
                ('CSE201', 'Algorithms', '11:00 AM', '12:15 PM', 40, 3),
                ('CSE202', 'Operating Systems', '1:00 PM', '2:15 PM', 35, 4),
                ('CSE203', 'Databases', '2:30 PM', '3:45 PM', 40, 5),
                ('MATH101', 'Calculus I', '8:00 AM', '9:15 AM', 60, 6),
                ('MATH102', 'Calculus II', '9:30 AM', '10:45 AM', 55, 6),
                ('MATH201', 'Linear Algebra', '11:00 AM', '12:15 PM', 50, 7),
                ('PHY101', 'Physics I', '1:00 PM', '2:15 PM', 45, 8),
                ('PHY102', 'Physics II', '2:30 PM', '3:45 PM', 40, 8),
                ('ENG101', 'English Composition', '8:30 AM', '9:45 AM', 70, 9),
                ('ENG201', 'Advanced Writing', '10:00 AM', '11:15 AM', 60, 9),
                ('HIST101', 'World History', '12:00 PM', '1:15 PM', 65, 10),
                ('HIST201', 'American History', '1:30 PM', '2:45 PM', 60, 11),
                ('ECON101', 'Microeconomics', '8:00 AM', '9:15 AM', 50, 12),
                ('ECON102', 'Macroeconomics', '9:30 AM', '10:45 AM', 50, 12),
                ('BIO101', 'Biology I', '11:00 AM', '12:15 PM', 45, 13),
                ('BIO102', 'Biology II', '1:00 PM', '2:15 PM', 40, 13),
                ('CHEM101', 'Chemistry I', '2:30 PM', '3:45 PM', 50, 14),
                ('CHEM102', 'Chemistry II', '4:00 PM', '5:15 PM', 45, 15)
            """);
    
            // Insert enrollments
            stmt.execute("""
                INSERT INTO enrollment (grade, student_id, course_id) VALUES
                ('A', 1, 'CSE101'),
                ('B+', 2, 'MATH101'),
                ('A-', 3, 'PHY101'),
                ('B', 4, 'ENG101'),
                ('A', 5, 'HIST101'),
                ('B-', 6, 'CSE102'),
                ('C+', 7, 'BIO101'),
                ('A', 8, 'CHEM101'),
                ('B+', 9, 'ECON101'),
                ('A-', 10, 'CSE201'),
                ('B', 11, 'MATH201'),
                ('A', 12, 'CSE203'),
                ('B+', 13, 'BIO102'),
                ('C', 14, 'PHY102'),
                ('A', 15, 'ENG201')
            """);
    
            System.out.println("Database and sample data setup completed!");
    
        } catch (Exception e) {
            System.out.println("Database setup failed.");
            e.printStackTrace();
        }
    }
    
}
