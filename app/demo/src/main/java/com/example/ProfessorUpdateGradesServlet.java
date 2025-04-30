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
        
        // Get professor ID from session
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("professor_id") == null) {
            res.sendRedirect("../login.html");
            return;
        }

        // Get course ID and grades from request
        String courseId = req.getParameter("courseId");
        boolean hasInvalidGrade = false;

        try {
            Connection conn = DBConnection.getConnection();
            Map<String, String[]> gradeMap = req.getParameterMap();

            // Iterate through grades
            for (Map.Entry<String, String[]> entry : gradeMap.entrySet()) {
                String param = entry.getKey();
                
                // Check if grade is valid
                if (param.startsWith("grades[")) {

                    // Extract student ID and new grade
                    String studentIdStr = param.substring(7, param.length() - 1);
                    int studentId = Integer.parseInt(studentIdStr);
                    String newGrade = entry.getValue()[0].trim().toUpperCase();

                    boolean isLetterGrade = newGrade.matches("^(A\\+|A-|A|B\\+|B-|B|C\\+|C-|C|D\\+|D-|D|F)$");
                    boolean isNumericGrade = newGrade.matches("^\\d{1,3}(\\.\\d{1,2})?$");

                    if (isLetterGrade || (isNumericGrade && isValidNumericGrade(newGrade))) {
                        String sql = "UPDATE enrollment SET grade = ? WHERE student_id = ? AND course_id = ?";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setString(1, newGrade);
                        stmt.setInt(2, studentId);
                        stmt.setString(3, courseId);
                        stmt.executeUpdate();
                    } else {
                        hasInvalidGrade = true;
                    }
                }
            }

            conn.close();

            // Redirect to dashboard if no invalid grades
            if (hasInvalidGrade) {
                session.setAttribute("error", "One or more grades were invalid. Use A–F or 0–100.");
            } else {
                session.setAttribute("success", "Grades updated successfully.");
            }

            res.sendRedirect(req.getContextPath() + "/professor/dashboard");

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Error updating grades: " + e.getMessage());
            res.sendRedirect(req.getContextPath() + "/professor/dashboard");
        }
    }
    // Validates if a grade is a valid numeric grade (e.g., 0.5, 4.0)
    private boolean isValidNumericGrade(String grade) {
        try {
            double g = Double.parseDouble(grade);
            return g >= 0.0 && g <= 100.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
