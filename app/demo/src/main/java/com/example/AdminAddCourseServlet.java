package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/admin/addCourse")
public class AdminAddCourseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String startTime = req.getParameter("startTime");
        int size = Integer.parseInt(req.getParameter("size"));
        int professorId = Integer.parseInt(req.getParameter("professorId"));

        try {
            Connection conn = DBConnection.getConnection();

            String sql = "INSERT INTO course (id, name, start_time, size, professor_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, startTime);
            stmt.setInt(4, size);
            stmt.setInt(6, professorId);

            stmt.executeUpdate();
            conn.close();

            res.sendRedirect(req.getContextPath() + "/admin/dashboard");
        } catch (Exception e) {
            e.printStackTrace();
            res.getWriter().println("Error: " + e.getMessage());
        }
    }
}
