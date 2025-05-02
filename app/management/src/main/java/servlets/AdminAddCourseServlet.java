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

// This is an endpoint for the admin to add a course
@WebServlet("/admin/addCourse")
public class AdminAddCourseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // Get the course details from the request
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String startTime = req.getParameter("startTime");
        String endTime = req.getParameter("endTime");
        int size = Integer.parseInt(req.getParameter("size"));
        int professorId = Integer.parseInt(req.getParameter("professorId"));

        // Validate start and end times (regex for time format)
        String timeRegex = "^(0?[1-9]|1[0-2]):[0-5][0-9]\\s?(AM|PM)$";
        if (!startTime.matches(timeRegex) || !endTime.matches(timeRegex)) {
            session.setAttribute("error", "Start and end time must be in format like '9:00 AM' or '2:15 PM'.");
            res.sendRedirect(req.getContextPath() + "/admin/dashboard");
            return;
        }
        
        try {
            Connection conn = DBConnection.getConnection();

            // Check if course with ID already exists
            String checkSql = "SELECT id FROM course WHERE id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, id);
            ResultSet rs = checkStmt.executeQuery();

            // If course with ID already exists, redirect to dashboard with error message
            if (rs.next()) {
                conn.close();
                session.setAttribute("error", "Course with ID '" + id + "' already exists.");
                res.sendRedirect(req.getContextPath() + "/admin/dashboard");
                return;
            }

            // Insert course into course table
            String sql = "INSERT INTO course (id, name, start_time, size, professor_id, end_time) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, startTime);
            stmt.setInt(4, size);
            stmt.setInt(5, professorId);
            stmt.setString(6, endTime);
            stmt.executeUpdate();
            conn.close();

            // No errors, redirect to dashboard
            session.setAttribute("error", null);
            res.sendRedirect(req.getContextPath() + "/admin/dashboard");
        } catch (Exception e) {
            e.printStackTrace();
            res.getWriter().println("Error: " + e.getMessage());
        }
    }
}
