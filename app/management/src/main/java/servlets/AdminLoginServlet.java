package servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;

// This is an endpoint for the admin to login
@WebServlet("/admin/login") 
public class AdminLoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        // Forward to the login page
        req.getRequestDispatcher("/admin/login.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        
        // Get admin username and password from request
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // Check if admin username and password are correct (hardcoded for simplicity)
        if ("admin".equals(username) && "securepassword".equals(password)) {
            HttpSession session = req.getSession();
            session.setAttribute("admin_id", 1); // Assign a static admin ID
            res.sendRedirect(req.getContextPath() + "/admin/dashboard");
        } else {
            // Set error message as request attribute
            req.setAttribute("errorMessage", "Invalid username or password");
            
            // Forwar back to the login page with error message
            req.getRequestDispatcher("/admin/login.jsp").forward(req, res);
        }
    }
}