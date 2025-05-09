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

// This is an endpoint for the student to search for courses
@WebServlet("/student/search")
public class StudentSearchCourseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // Get student ID from session, if not logged in, redirect to login page
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("student_id") == null) {
            res.sendRedirect("../login.jsp");
            return;
        }


        // Get course name from request
        String courseName = req.getParameter("courseName");
            
        try {
            Connection conn = DBConnection.getConnection();

            // Select all the courses that match the course name
            String courseQuery = "SELECT c.id, c.name, c.start_time, c.end_time, c.size, p.name AS professor_name, c.professor_id " +
                                "FROM course c JOIN professor p ON c.professor_id = p.id " +
                                 "WHERE LOWER(c.name) LIKE ? OR LOWER(c.id) LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(courseQuery);
            stmt.setString(1, "%" + courseName.toLowerCase() + "%");
            stmt.setString(2, "%" + courseName.toLowerCase() + "%");

            ResultSet rs = stmt.executeQuery();
            // Store the searched courses in a list
            ArrayList<Course> courses = new ArrayList<>();
            while (rs.next()) {
                Course c = new Course();
                c.setId(rs.getString("id"));
                c.setName(rs.getString("name"));
                c.setStartTime(rs.getString("start_time"));
                c.setSize(rs.getInt("size"));
                c.setProfessorId(rs.getInt("professor_id"));
                c.setProfessorName(rs.getString("professor_name"));
                c.setEndTime(rs.getString("end_time"));
                courses.add(c);
            }

            // Set attributes in request and forward to frontend
            req.setAttribute("courseSearch", courses);
            req.getRequestDispatcher("/student/dashboard.jsp").forward(req, res);
        } catch (Exception e) {
            e.printStackTrace();
            res.getWriter().println("Error: " + e.getMessage());
        }
    }
}
