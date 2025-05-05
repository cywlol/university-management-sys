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


// This is an endpoint for the professor to register
@WebServlet("/professor/register") 
public class ProfessorRegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        // Forward to the login page
        req.getRequestDispatcher("/professor/register.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
            
        // Get parameters from request
        String username = req.getParameter("username");
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        String hashedPassword = null;

        if (username.length() > 30) {
            req.setAttribute("errorMessage", "Username should be less than 30 characters");
            req.getRequestDispatcher("/professor/register.jsp").forward(req, res);
            return;
        }

        if (name.length() > 30) {
            req.setAttribute("errorMessage", "Name should be less than 30 characters");
            req.getRequestDispatcher("/professor/register.jsp").forward(req, res);
            return;
        }

        if (name.length() > 50) {
            req.setAttribute("errorMessage", "Full name should be less than 50 characters");
            req.getRequestDispatcher("/professor/register.jsp").forward(req, res);
            return;
        } 

        if (!name.matches("^[A-Za-z]+\\s[A-Za-z]+$")) {
            req.setAttribute("errorMessage", "Full name must contain only letters with a single space between first and last name");
            req.getRequestDispatcher("/professor/register.jsp").forward(req, res);
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
            // Query database to insert professor
            Connection conn = DBConnection.getConnection();

            // Check if username already exists
            String sqlTestError = "SELECT id FROM professor WHERE username = ?";
            PreparedStatement stmtTest = conn.prepareStatement(sqlTestError);
            stmtTest.setString(1, username);
            ResultSet rsTest = stmtTest.executeQuery();
            if (rsTest.next()) {
                conn.close();
                req.setAttribute("errorMessage", "Username already exists");
                req.getRequestDispatcher("/professor/register.jsp").forward(req, res);
                return;
            }


            System.out.println("Connected to database successfully!");
            String sql = "INSERT INTO professor (username, password, name) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, name);

            int rowsInserted = stmt.executeUpdate();

            res.setContentType("text/html");
            PrintWriter out = res.getWriter();

            // If professor inserted successfully, set attributes in session
            if (rowsInserted > 0) {

                // Get professor ID from database to store attriburtes in session
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
                res.sendRedirect(req.getContextPath() + "/professor/dashboard"); 
            } else {
                // If professor not inserted, redirect to register page with error message
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