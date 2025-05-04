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
import java.sql.ResultSet;
import jakarta.servlet.http.HttpSession;


// This is an endpoint for the professor to login
@WebServlet("/professor/login")  
public class ProfessorLoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        // Forward to the login page
        req.getRequestDispatcher("/professor/login.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // Get professor username and password from request
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // Hash password
        String hashedPassword = null;
        try {
            hashedPassword = PasswordHash.hash(password);
        } catch (Exception e) {
            e.printStackTrace(); 
            res.getWriter().println("Error hashing password");
            return;
        }


        try {
            // Query database for professor
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM professor WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword); 
            ResultSet rs = stmt.executeQuery();
            
            // If professor found, set attributes in session and redirect to dashboard
            if (rs.next()) {
                HttpSession session = req.getSession(); 
                // Set attributes in session
                session.setAttribute("professor_id", rs.getInt("id")); 
                session.setAttribute("professor_name", rs.getString("name")); 
                res.sendRedirect(req.getContextPath() + "/professor/dashboard"); 
            } else {
                // If professor not found, redirect to login page
                req.setAttribute("errorMessage", "Invalid username or password");
                req.getRequestDispatcher("/professor/login.jsp").forward(req, res);
            }

        } catch (Exception e) {
            res.setContentType("text/html");
            res.getWriter().println("<h2> Error is this: " + e.getMessage() + "</h2>");
            e.printStackTrace();
        }
    }

}