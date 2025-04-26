package com.example;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;


@WebServlet("/student/enroll")
public class StudentEnrollmentServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("student_id") == null) {
            res.sendRedirect("../login.html");
            return;
        }

        String courseID = (String) req.getParameter("courseId");

         try {
            Connection conn = DBConnection.getConnection();

        
            String sql = "INSERT INTO enrollment (course_id, student_id, completed, name, grade) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, courseID);
            stmt.setInt(2, Integer.parseInt(session.getAttribute("student_id").toString()));
            stmt.setBoolean(3, false);
            stmt.setString(4, session.getAttribute("student_name").toString());
            stmt.setString(5, "In Progress");
            int rowsInserted = stmt.executeUpdate();

            String sql2 = "SELECT * FROM course WHERE id = ?";
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            stmt2.setString(1, courseID);
            ResultSet rs2 = stmt2.executeQuery();
            Course c = new Course();
            if (rs2.next()) {
                c.setId(rs2.getString("id"));
                c.setSize(rs2.getInt("size"));
                c.setStartTime(rs2.getString("start_time"));
                c.setName(rs2.getString("name"));
                if (rs2.getString("prerequisite") == null) {
                    c.setPrerequisite("None");
                } else {
                    c.setPrerequisite(rs2.getString("prerequisite"));
                }
                c.setProfessorId(rs2.getInt("professor_id"));
            }
            


            if (rowsInserted > 0) {
                Object obj = session.getAttribute("courses");
        
                ArrayList<Course> currentCourses;
                
                if (obj instanceof ArrayList<?>) {
                    currentCourses = (ArrayList<Course>) obj;
                } else {
                    currentCourses = new ArrayList<>();
                }                
                
                currentCourses.add(c);
                ArrayList<Course> allCourses = (ArrayList<Course>) session.getAttribute("allCourses");
                allCourses.remove(c);
                session.setAttribute("allCourses", allCourses);
                session.setAttribute("courses", currentCourses);
                res.sendRedirect(req.getContextPath() + "/student/dashboard");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            res.getWriter().println("Error: " + e.getMessage());
        }
        
    }

}