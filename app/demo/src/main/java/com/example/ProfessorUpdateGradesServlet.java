package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;

@WebServlet("/professor/updateGrades")
public class ProfessorUpdateGradesServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("professor_id") == null) {
            res.sendRedirect("../login.html");
            return;
        }

        String courseId = req.getParameter("courseId");

        try {
            Connection conn = DBConnection.getConnection();

            // Form submits grades as grades[studentId] = newGrade
            Map<String, String[]> gradeMap = req.getParameterMap();

            for (Map.Entry<String, String[]> entry : gradeMap.entrySet()) {
                String param = entry.getKey();
                
                if (param.startsWith("grades[")) {
                    // Extract studentId from the key: grades[123] -> 123
                    String studentIdStr = param.substring(7, param.length() - 1);
                    int studentId = Integer.parseInt(studentIdStr);
                    String newGrade = entry.getValue()[0];

                    if (newGrade != null && !newGrade.trim().isEmpty()) {
                        String sql = "UPDATE enrollment SET grade = ? WHERE student_id = ? AND course_id = ?";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setString(1, newGrade);
                        stmt.setInt(2, studentId);
                        stmt.setString(3, courseId);
                        stmt.executeUpdate();
                    }
                }
            }

            conn.close();

            res.sendRedirect(req.getContextPath() + "/professor/dashboard");
        } catch (Exception e) {
            e.printStackTrace();
            res.getWriter().println("Error: " + e.getMessage());
        }
    }
}
