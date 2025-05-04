DROP DATABASE IF EXISTS management_sys;
CREATE DATABASE management_sys;
USE management_sys;

    CREATE TABLE professor (
        id INT AUTO_INCREMENT,
        name VARCHAR(255) NOT NULL,
        password VARCHAR(255) NOT NULL,
        username VARCHAR(50) UNIQUE NOT NULL,
        PRIMARY KEY (id)
    );

    CREATE TABLE student (
        id INT AUTO_INCREMENT,
        name VARCHAR(255) NOT NULL,
        password VARCHAR(255) NOT NULL,
        year INT NOT NULL,
        gpa FLOAT NOT NULL,
        username VARCHAR(50) UNIQUE NOT NULL,
        PRIMARY KEY (id)
    );

    CREATE TABLE course (
        id VARCHAR(50),
        size INT NOT NULL,
        start_time VARCHAR(10) NOT NULL,
        name VARCHAR(255) NOT NULL,
        professor_id INT NOT NULL,
        end_time VARCHAR(10) NOT NULL,
        PRIMARY KEY (id),
        FOREIGN KEY (professor_id) REFERENCES professor(id)
    );

    CREATE TABLE enrollment (
        id INT AUTO_INCREMENT,
        grade VARCHAR(20) NOT NULL,
        student_id INT NOT NULL,
        course_id VARCHAR(50) NOT NULL,
        PRIMARY KEY (id),
        FOREIGN KEY (student_id) REFERENCES student(id),
        FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
    );
