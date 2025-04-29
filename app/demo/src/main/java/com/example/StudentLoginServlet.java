package com.example;

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
import java.util.ArrayList;

@WebServlet("/student/login")  // ← this connects /login to this class!
public class StudentLoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM student WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password); 
            ResultSet rs = stmt.executeQuery();

            

            if (rs.next()) {
                // ✅ Redirect to dashboard if login is successful
                HttpSession session = req.getSession(); // Create new session
                session.setAttribute("student_id", rs.getInt("id")); // store id
                session.setAttribute("student_name", rs.getString("name"));  // store name
                session.setAttribute("student_year", rs.getInt("year"));     // store year
                session.setAttribute("student_gpa", rs.getDouble("gpa"));    // store GPA
                res.sendRedirect(req.getContextPath() + "/student/dashboard"); 
            } else {    
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