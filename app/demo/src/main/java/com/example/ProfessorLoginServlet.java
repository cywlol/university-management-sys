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
import java.sql.ResultSet;
import jakarta.servlet.http.HttpSession;

@WebServlet("/professor/login")  // ← this connects /login to this class!
public class ProfessorLoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM professor WHERE name = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password); 
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                HttpSession session = req.getSession(); // Create new session
                session.setAttribute("professor_id", rs.getInt("id")); // store id
                session.setAttribute("professor_name", rs.getString("name"));  // store name
                session.setAttribute("professor_email", rs.getString("email"));     // store year
                res.sendRedirect(req.getContextPath() + "/professor/dashboard"); 
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