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

@WebServlet("/professor/register")  // ← this connects /login to this class!
public class ProfessorRegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        try {
            Connection conn = DBConnection.getConnection();
            System.out.println("✅ Connected to database successfully!");
            String sql = "INSERT INTO professor (name, password, email) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password); // You should hash this in real apps
            stmt.setString(3, email); // You should hash this in real apps

            int rowsInserted = stmt.executeUpdate();

            res.setContentType("text/html");
            PrintWriter out = res.getWriter();

            if (rowsInserted > 0) {
                out.println("<h2> professor registered successfully!</h2>");
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