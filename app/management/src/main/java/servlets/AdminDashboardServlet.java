package servlets;

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

// This is an endpoint for the admin to view the dashboard
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {


        // Get admin ID from session (if not logged in, redirect to login page)
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("admin_id") == null) {
            res.sendRedirect("../login.html");
            return;
        }

        // If there is an error, set it as an attribute
        if (session.getAttribute("error") != null) {
            req.setAttribute("error", session.getAttribute("error"));
        }

        try {
            Connection conn = DBConnection.getConnection();

            // Get all professors from professor table
            String sqlProfessors = "SELECT id, name FROM professor";
            PreparedStatement stmtProfessors = conn.prepareStatement(sqlProfessors);
            ResultSet rsProfessors = stmtProfessors.executeQuery();

            // Store professors in an array list
            ArrayList<Professor> professors = new ArrayList<>();
            while (rsProfessors.next()) {
                Professor p = new Professor();
                p.setId(rsProfessors.getInt("id"));
                p.setName(rsProfessors.getString("name"));
                professors.add(p);
            }

            // Get all courses from course table, including the professor_name from professor table
            String sqlCourses = "SELECT c.id, c.name, c.start_time, c.end_time, c.size, p.name AS professor_name, c.professor_id " +
             "FROM course c " +
             "JOIN professor p ON c.professor_id = p.id";
            PreparedStatement stmtCourses = conn.prepareStatement(sqlCourses);
            ResultSet rsCourses = stmtCourses.executeQuery();

            // Create a list of courses to be displayed on the dashboard
            ArrayList<Course> courses = new ArrayList<>();
            while (rsCourses.next()) {
                Course c = new Course();
                c.setId(rsCourses.getString("id"));
                c.setName(rsCourses.getString("name"));
                c.setStartTime(rsCourses.getString("start_time"));
                c.setSize(rsCourses.getInt("size"));
                c.setProfessorId(rsCourses.getInt("professor_id"));
                c.setProfessorName(rsCourses.getString("professor_name"));
                c.setEndTime(rsCourses.getString("end_time"));
                courses.add(c);
            }

            // Set attributes in request and forward to the frontend
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
