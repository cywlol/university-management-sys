package servlets;

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

// This is an endpoint for the student to view the dashboard
@WebServlet("/student/dashboard")
public class StudentDashboardServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // Get student ID from session, if not logged in, redirect to login page
        if (session == null || session.getAttribute("student_id") == null) {
            res.sendRedirect("../login.html");
            return;
        }
        
        // Get student ID from session
        int studentId = (int) session.getAttribute("student_id");

        try {

            // Select all the courses that the student is enrolled in
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT c.id, c.size, c.start_time, c.end_time,c.name, c.professor_id, e.grade, p.name AS professor_name " +
             "FROM course c " +
             "JOIN enrollment e ON c.id = e.course_id " +
             "JOIN professor p ON c.professor_id = p.id " +
             "WHERE e.student_id = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            // Create a list of courses that the student is enrolled in
            List<Course> courses = new ArrayList<>();
            while (rs.next()) {
                Course c = new Course();
                c.setId(rs.getString("id"));
                c.setSize(rs.getInt("size"));
                c.setStartTime(rs.getString("start_time"));
                c.setEndTime(rs.getString("end_time"));
                c.setName(rs.getString("name"));
                c.setProfessorId(rs.getInt("professor_id"));
                c.setGrade(rs.getString("grade")); 
                c.setProfessorName(rs.getString("professor_name"));
                courses.add(c);
            }

            // Set attributes in request and forward to frontend
            session.setAttribute("studentCourses", courses);

            // Select all the courses that are available for the student to enroll in
            String sql2 = "SELECT * FROM course WHERE id NOT IN (SELECT course_id  FROM enrollment WHERE student_id = ?)";
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            stmt2.setInt(1, studentId);
            ResultSet rs2 = stmt2.executeQuery();

            // Add those courses to a list
            ArrayList<Course> allCourses = new ArrayList<>();
                while (rs2.next()) {
                    Course c = new Course();
                    c.setId(rs2.getString("id"));
                    c.setSize(rs2.getInt("size"));
                    c.setStartTime(rs2.getString("start_time"));
                    c.setEndTime(rs2.getString("end_time"));
                    c.setName(rs2.getString("name"));
                    c.setProfessorId(rs2.getInt("professor_id"));
                    allCourses.add(c);
                }
                
            // Set attributes in request and forward to frontend
            session.setAttribute("allCourses", allCourses);  
            req.getRequestDispatcher("/student/dashboard.jsp").forward(req, res);
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            res.getWriter().println("Error: " + e.getMessage());
        }
    }
}
