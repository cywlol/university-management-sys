package com.example;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

@WebServlet("/student/unenroll")
public class StudentUnenrollmentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("student_id") == null) {
            res.sendRedirect("../login.html");
            return;
        }

        int studentId = (int) session.getAttribute("student_id");
        String courseId = req.getParameter("courseId");

        try {
            Connection conn = DBConnection.getConnection();
            // Query database to delete enrollment
            String sql = "DELETE FROM enrollment WHERE student_id = ? AND course_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            stmt.setString(2, courseId);

            stmt.executeUpdate();
            conn.close();
            
            // Update studentCourses in session
            Object obj = session.getAttribute("studentCourses");
            ArrayList<Course> currentCourses;

            if (obj instanceof ArrayList<?>) {
                currentCourses = (ArrayList<Course>) obj;
            } else {
                currentCourses = new ArrayList<>();
            }

            // Find and remove the course by ID
            currentCourses.removeIf(course -> course.getId().equals(courseId));

            // Save the updated list back into session
            session.setAttribute("studentCourses", currentCourses);

            res.sendRedirect(req.getContextPath() + "/student/dashboard");
        } catch (Exception e) {
            e.printStackTrace();
            res.getWriter().println("Error: " + e.getMessage());
        }
    }
}
