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

@WebServlet("/student/register")  // ← this connects /login to this class!
public class StudentRegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String gpaStr = req.getParameter("gpa");
        String year = req.getParameter("year");
        String name = req.getParameter("name");

        try {
            Connection conn = DBConnection.getConnection();
            System.out.println("✅ Connected to database successfully!");
            String sql = "INSERT INTO student (username, password, year, gpa, name) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password); // You should hash this in real apps
            stmt.setInt(3, Integer.parseInt(year));  // Year should be integer
            stmt.setDouble(4, Double.parseDouble(gpaStr)); 
            stmt.setString(5, name);

            int rowsInserted = stmt.executeUpdate();

            res.setContentType("text/html");
            PrintWriter out = res.getWriter();

            String sqlID = "SELECT id FROM student WHERE username = ? AND password = ?";
            PreparedStatement stmt2 = conn.prepareStatement(sqlID);
            stmt2.setString(1, username);
            stmt2.setString(2, password); 
            ResultSet rs = stmt2.executeQuery();
            rs.next();
            int studentId = rs.getInt("id");

            if (rowsInserted > 0) {
                HttpSession session = req.getSession(); // Create new session
                session.setAttribute("student_id", studentId);  // store id
                session.setAttribute("student_name", name);  // store name
                session.setAttribute("student_year", year);     // store year
                session.setAttribute("student_gpa", gpaStr);    // store GPA
                res.sendRedirect(req.getContextPath() + "/student/dashboard"); 
            } else {
                out.println("<h2> Failed to register student.</h2>");
            }

            conn.close();

        } catch (Exception e) {
            res.setContentType("text/html");
            res.getWriter().println("<h2> Error is this: " + e.getMessage() + "</h2>");
            e.printStackTrace();
        }
    }

}