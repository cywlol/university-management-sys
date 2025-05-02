package servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import jakarta.servlet.http.HttpSession;
import java.sql.ResultSet;

// This is an endpoint for the student to login
@WebServlet("/student/login") 
public class StudentLoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // Get student username and password from request
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            // Query database for student
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM student WHERE username = ? AND password = ?";
            String hashedPassword = null;

            // Hash password
            try {
                hashedPassword = PasswordHash.hash(password);
            } catch (Exception e) {
                e.printStackTrace(); // optional: log it
                res.getWriter().println("Error hashing password");
                return;
            }

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword); 
            ResultSet rs = stmt.executeQuery();

            
            // If student found, set attributes in session and redirect to dashboard
            if (rs.next()) {
                // Set attributes in session
                HttpSession session = req.getSession(); 
                session.setAttribute("student_id", rs.getInt("id"));
                session.setAttribute("student_name", rs.getString("name")); 
                session.setAttribute("student_year", rs.getInt("year"));   
                session.setAttribute("student_gpa", rs.getDouble("gpa")); 
                res.sendRedirect(req.getContextPath() + "/student/dashboard"); 
            } else {    
                // If student not found, redirect to login page
                res.setContentType("text/html");
                PrintWriter out = res.getWriter();
                out.println("<h3 style='color:red;'>Invalid username or password</h3>");
            }

        } catch (Exception e) {
            res.setContentType("text/html");
            res.getWriter().println("<h2> Error is this: " + e.getMessage() + "</h2>");
            e.printStackTrace();
        }
    }

}