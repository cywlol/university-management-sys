package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("admin_id") == null) {
            res.sendRedirect("../login.html");
            return;
        }

        try {
            Connection conn = DBConnection.getConnection();

            // Get all professors
            String sqlProfessors = "SELECT id, name, email FROM professor";
            PreparedStatement stmtProfessors = conn.prepareStatement(sqlProfessors);
            ResultSet rsProfessors = stmtProfessors.executeQuery();

            ArrayList<Professor> professors = new ArrayList<>();
            while (rsProfessors.next()) {
                Professor p = new Professor();
                p.setId(rsProfessors.getInt("id"));
                p.setName(rsProfessors.getString("name"));
                p.setEmail(rsProfessors.getString("email"));
                professors.add(p);
            }

            // Get all courses
            String sqlCourses = "SELECT * FROM course";
            PreparedStatement stmtCourses = conn.prepareStatement(sqlCourses);
            ResultSet rsCourses = stmtCourses.executeQuery();

            ArrayList<Course> courses = new ArrayList<>();
            while (rsCourses.next()) {
                Course c = new Course();
                c.setId(rsCourses.getString("id"));
                c.setName(rsCourses.getString("name"));
                c.setStartTime(rsCourses.getString("start_time"));
                c.setSize(rsCourses.getInt("size"));
                c.setProfessorId(rsCourses.getInt("professor_id"));
                c.setEndTime(rsCourses.getString("end_time"));
                courses.add(c);
            }

            req.setAttribute("professors", professors);
            req.setAttribute("courses", courses);
            req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, res);

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            res.getWriter().println("Error: " + e.getMessage());
        }
    }
}
