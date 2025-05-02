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

// This is an endpoint for the professor to view the courses they are teaching
@WebServlet("/professor/course")
public class ProfessorCourseServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // Get professor ID from session, if not logged in, redirect to login page
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("professor_id") == null) {
            res.sendRedirect("../login.html");
            return;
        }

        // Get course ID from request
        String courseId = req.getParameter("courseId");

        // Query database for students and their grades
        try {
            Connection conn = DBConnection.getConnection();

            String sql = "SELECT s.id, s.name, e.grade " +
                         "FROM student s " +
                         "JOIN enrollment e ON s.id = e.student_id " +
                         "WHERE e.course_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, courseId);
            ResultSet rs = stmt.executeQuery();

            // Create a list of students and their grades
            ArrayList<StudentGrade> students = new ArrayList<>();
            while (rs.next()) {
                StudentGrade sg = new StudentGrade();
                sg.setStudentId(rs.getInt("id"));
                sg.setStudentName(rs.getString("name"));
                sg.setGrade(rs.getString("grade"));
                sg.setCourseId(courseId);
                students.add(sg);
            }

            // Set attributes in request
            req.setAttribute("students", students);
            req.setAttribute("courseId", courseId);
            req.getRequestDispatcher("/professor/students.jsp").forward(req, res);

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            res.getWriter().println("Error: " + e.getMessage());
        }
    }
}
