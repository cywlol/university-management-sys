package com.example;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;


@WebServlet("/student/enroll")
public class StudentEnrollmentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
            
        HttpSession session = req.getSession(false);
        // Get student ID from session
        if (session == null || session.getAttribute("student_id") == null) {
            res.sendRedirect("../login.html");
            return;
        }

        String courseID = (String) req.getParameter("courseId");

         try {
            Connection conn = DBConnection.getConnection();

            // Query database to insert enrollment
            String sql = "INSERT INTO enrollment (course_id, student_id, grade) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, courseID);
            stmt.setInt(2, Integer.parseInt(session.getAttribute("student_id").toString()));
            stmt.setString(3, "In Progress");
            int rowsInserted = stmt.executeUpdate();

            // Query database to get course
            String sql2 = "SELECT * FROM course WHERE id = ?";
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            stmt2.setString(1, courseID);
            ResultSet rs2 = stmt2.executeQuery();

            // Create a course object
            Course c = new Course();
            if (rs2.next()) {
                c.setId(rs2.getString("id"));
                c.setSize(rs2.getInt("size"));
                c.setStartTime(rs2.getString("start_time"));
                c.setEndTime(rs2.getString("end_time"));
                c.setName(rs2.getString("name"));
                c.setProfessorId(rs2.getInt("professor_id"));
            }
            
            // If enrollment inserted successfully, update studentCourses in session
            if (rowsInserted > 0) {
                Object obj = session.getAttribute("studentCourses");
        
                ArrayList<Course> currentCourses;
                
                if (obj instanceof ArrayList<?>) {
                    currentCourses = (ArrayList<Course>) obj;
                } else {
                    currentCourses = new ArrayList<>();
                }                
                
                currentCourses.add(c);
                session.setAttribute("studentCourses", currentCourses);
                res.sendRedirect(req.getContextPath() + "/student/dashboard");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            res.getWriter().println("Error: " + e.getMessage());
        }
        
    }

}