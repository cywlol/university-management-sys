package com.example;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletException;
import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.security.MessageDigest;


@WebServlet("/professor/register") 
public class ProfessorRegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
            
        // Get parameters from request

        String username = req.getParameter("username");
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        String hashedPassword = null;
        try {
            hashedPassword = PasswordHash.hash(password);
        } catch (Exception e) {
            e.printStackTrace(); 
            res.getWriter().println("Error hashing password");
            return;
        }

        try {
            // Query database to insert professor
            Connection conn = DBConnection.getConnection();
            System.out.println("Connected to database successfully!");
            String sql = "INSERT INTO professor (username, password, email, name) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, email);
            stmt.setString(4, name);

            int rowsInserted = stmt.executeUpdate();

            res.setContentType("text/html");
            PrintWriter out = res.getWriter();

            if (rowsInserted > 0) {
                // If professor inserted successfully, set attributes in session

                // Get professor ID from database
                String sqlID = "SELECT id FROM professor WHERE username = ? AND password = ?";
                PreparedStatement stmt2 = conn.prepareStatement(sqlID);
                stmt2.setString(1, username);
                stmt2.setString(2, hashedPassword); 
                ResultSet rs = stmt2.executeQuery();
                rs.next();
                int profID = rs.getInt("id");
                HttpSession session = req.getSession();
                session.setAttribute("professor_id", profID); 
                session.setAttribute("professor_name", name);  
                session.setAttribute("professor_email", email);    
                res.sendRedirect(req.getContextPath() + "/professor/dashboard"); 
            } else {
                out.println("<h2> Failed to register professor.</h2>");
            }

            conn.close();

        } catch (Exception e) {
        
            res.setContentType("text/html");
            res.getWriter().println("<h2> Error is this: " + e.getMessage() + "</h2>");
            e.printStackTrace();
        }
    }

}