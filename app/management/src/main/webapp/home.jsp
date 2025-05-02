<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Welcome - Select Role</title>
    <style>
      body {
        font-family: "Segoe UI", Roboto, Arial, sans-serif;
        background-color: #f5f7fa;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 100vh;
        margin: 0;
      }

      .logo-container {
        margin-bottom: 20px;
        text-align: center;
      }

      .logo {
        font-size: 42px;
        font-weight: 700;
        color: #2e7d32;
        letter-spacing: 3px;
        text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
        position: relative;
        padding: 5px 15px;
        display: inline-block;
      }

      .logo::after {
        content: "";
        position: absolute;
        bottom: 0;
        left: 0;
        width: 100%;
        height: 3px;
        background: linear-gradient(90deg, transparent, #4caf50, transparent);
      }

      .logo-tagline {
        font-size: 14px;
        color: #666;
        margin-top: 5px;
        letter-spacing: 1px;
      }

      h1 {
        margin-bottom: 40px;
        color: #333;
        font-weight: 500;
      }

      .btn-group {
        display: flex;
        gap: 20px;
      }

      a {
        display: inline-block;
        padding: 15px 30px;
        background-color: #4caf50;
        color: white;
        text-decoration: none;
        font-size: 18px;
        border-radius: 8px;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        transition: all 0.3s ease;
      }

      a:hover {
        background-color: #45a049;
        transform: translateY(-2px);
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
      }
    </style>
  </head>
  <body>
    <div class="logo-container">
      <div class="logo">SMART</div>
      <div class="logo-tagline">Student Management And Resource Tool</div>
    </div>

    <h1>Welcome! Please select your role</h1>

    <div class="btn-group">
      <a href="<%= request.getContextPath() %>/student/login.jsp">Student Login</a>
      <a href="<%= request.getContextPath() %>/professor/login.jsp">Professor Login</a>
      <a href="<%= request.getContextPath() %>/admin/login.jsp">Admin Login</a>
    </div>
  </body>
</html>
