package com.example;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import java.io.PrintWriter;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/login") 
public class AdminLoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // Check if admin username and password are correct (hardcoded for simplicity)

        if ("admin".equals(username) && "securepassword".equals(password)) {
            HttpSession session = req.getSession();
            session.setAttribute("admin_id", 1); // Assign a static admin ID
            res.sendRedirect(req.getContextPath() + "/admin/dashboard");
        } else {
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            out.println("<h3 style='color:red;'>Invalid username or password</h3>");
        }
    }
}
