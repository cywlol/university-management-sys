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

@WebServlet("/professor/dashboard")
public class ProfessorDashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // Get professor ID from session
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("professor_id") == null) {
            res.sendRedirect("../login.html");
            return;
        }

        int professor = (int) session.getAttribute("professor_id");
        try {

            // Select all the courses that the student is enrolled in
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM course WHERE professor_id = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, professor);
            ResultSet rs = stmt.executeQuery();

            // Create a list of courses
            List<Course> courses = new ArrayList<>();
            while (rs.next()) {
                Course c = new Course();
                c.setId(rs.getString("id"));
                c.setSize(rs.getInt("size"));
                c.setStartTime(rs.getString("start_time"));
                c.setEndTime(rs.getString("end_time"));
                c.setName(rs.getString("name"));
                c.setProfessorId(rs.getInt("professor_id"));
                courses.add(c);
            }

            // Set attributes in request
            session.setAttribute("studentCourses", courses);
            session.setAttribute("professorCourses", courses);
            req.getRequestDispatcher("/professor/dashboard.jsp").forward(req, res);
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            res.getWriter().println("Error: " + e.getMessage());
        }
    }
}
