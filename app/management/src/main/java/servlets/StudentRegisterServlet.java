package servlets;

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


// This is an endpoint for the student to register
@WebServlet("/student/register") 
public class StudentRegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        // Forward to the login page
        req.getRequestDispatcher("/professor/register.jsp").forward(req, res);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // Get student username, password, gpa, year, and name from request
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String gpaStr = req.getParameter("gpa");
        String year = req.getParameter("year");
        String name = req.getParameter("name");
        String hashedPassword = null;
        
        if (username.length() > 30) {
            req.setAttribute("errorMessage", "Username should be less than 30 characters");
            req.getRequestDispatcher("/student/register.jsp").forward(req, res);
            return;
        }

        if (name.length() > 30) {
            req.setAttribute("errorMessage", "Name should be less than 30 characters");
            req.getRequestDispatcher("/student/register.jsp").forward(req, res);
            return;
        }

        if (name.length() > 50) {
            req.setAttribute("errorMessage", "Full name should be less than 50 characters");
            req.getRequestDispatcher("/student/register.jsp").forward(req, res);
            return;
        } 

        if (!name.matches("^[A-Za-z]+\\s[A-Za-z]+$")) {
            req.setAttribute("errorMessage", "Full name must contain only letters with a single space between first and last name");
            req.getRequestDispatcher("/student/register.jsp").forward(req, res);
            return;
        }

        // Hash password
        try {
            hashedPassword = PasswordHash.hash(password);
        } catch (Exception e) {
            e.printStackTrace(); 
            res.getWriter().println("Error hashing password");
            return;
        }

        try {
            // Query database to insert student
            Connection conn = DBConnection.getConnection();

            String sqlTestError = "SELECT id FROM student WHERE username = ?";
            PreparedStatement stmtTest = conn.prepareStatement(sqlTestError);
            stmtTest.setString(1, username);
            ResultSet rsTest = stmtTest.executeQuery();
            if (rsTest.next()) {
                conn.close();
                req.setAttribute("errorMessage", "Username already exists");
                req.getRequestDispatcher("/student/register.jsp").forward(req, res);
                return;
            }

            String sql = "INSERT INTO student (username, password, year, gpa, name) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.setInt(3, Integer.parseInt(year)); 
            stmt.setDouble(4, Double.parseDouble(gpaStr)); 
            stmt.setString(5, name);

            int rowsInserted = stmt.executeUpdate();

            res.setContentType("text/html");
            PrintWriter out = res.getWriter();


            // Get student ID from database
            String sqlID = "SELECT id FROM student WHERE username = ? AND password = ?";
            PreparedStatement stmt2 = conn.prepareStatement(sqlID);
            stmt2.setString(1, username);
            stmt2.setString(2, hashedPassword); 
            ResultSet rs = stmt2.executeQuery();
            rs.next();
            int studentId = rs.getInt("id");

            // If student inserted successfully, set attributes in session
            if (rowsInserted > 0) {
                HttpSession session = req.getSession();
                session.setAttribute("student_id", studentId);  
                session.setAttribute("student_name", name); 
                session.setAttribute("student_year", year);  
                session.setAttribute("student_gpa", gpaStr);   
                res.sendRedirect(req.getContextPath() + "/student/dashboard"); 
            } else {
                // If student not inserted, redirect to register page with error message
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