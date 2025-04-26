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
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/student/dashboard")
public class StudentDashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("student_id") == null) {
            res.sendRedirect("../login.html");
            return;
        }

        int studentId = (int) session.getAttribute("student_id");

        try {

            // Select all the courses that the student is enrolled in
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT c.id, c.size, c.start_time, c.name, c.prerequisite, c.professor_id " +
                         "FROM course c " +
                         "JOIN enrollment e ON c.id = e.course_id " +  
                         "WHERE e.student_id = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            List<Course> courses = new ArrayList<>();
            while (rs.next()) {
                Course c = new Course();
                c.setId(rs.getString("id"));
                c.setSize(rs.getInt("size"));
                c.setStartTime(rs.getString("start_time"));
                c.setName(rs.getString("name"));
                c.setPrerequisite(rs.getString("prerequisite"));
                c.setProfessorId(rs.getInt("professor_id"));
                courses.add(c);
            }

            System.out.println("Courses" + courses);
            
            session.setAttribute("courses", courses);

            // Select all the courses that are available for the student to enroll in
            String sql2 = "SELECT * FROM course WHERE id NOT IN (SELECT course_id  FROM enrollment WHERE student_id = ?)";
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            stmt2.setInt(1, studentId);
            ResultSet rs2 = stmt2.executeQuery();

            ArrayList<Course> allCourses = new ArrayList<>();
                while (rs2.next()) {
                    Course c = new Course();
                    c.setId(rs2.getString("id"));
                    c.setSize(rs2.getInt("size"));
                    c.setStartTime(rs2.getString("start_time"));
                    c.setName(rs2.getString("name"));
                    c.setPrerequisite(rs2.getString("prerequisite"));
                    c.setProfessorId(rs2.getInt("professor_id"));
                    allCourses.add(c);
                }

            session.setAttribute("allCourses", allCourses);  
            session.setAttribute("courses", courses);
            req.getRequestDispatcher("/student/dashboard.jsp").forward(req, res);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            res.getWriter().println("Error: " + e.getMessage());
        }
    }
}
