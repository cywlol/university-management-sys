<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Professor Register</title>
    <style>
      body {
        font-family: Arial, sans-serif;
        background-color: #f2f2f2;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
      }
      form {
        background-color: white;
        padding: 30px;
        border-radius: 10px;
        box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
        width: 300px;
      }
      h2 {
        text-align: center;
        margin-bottom: 20px;
      }
      label {
        display: block;
        margin-top: 10px;
        margin-bottom: 5px;
        font-weight: bold;
      }
      input {
        width: 100%;
        padding: 8px;
        margin-bottom: 15px;
        border: 1px solid #ccc;
        border-radius: 5px;
      }
      button {
        width: 100%;
        padding: 10px;
        background-color: #4caf50;
        color: white;
        border: none;
        border-radius: 5px;
        font-size: 16px;
        cursor: pointer;
      }
      button:hover {
        background-color: #45a049;
      }
      .link {
        margin-top: 15px;
        text-align: center;
      }
      .link a {
        text-decoration: none;
        color: #4caf50;
        font-size: 14px;
      }
    </style>
  </head>
  <body>
    <form action="<%=request.getContextPath()%>/professor/register" method="post">
      <h2>Professor Register</h2>
      
      <% if (request.getAttribute("errorMessage") != null) { %>
        <div style="color: red; text-align: center; margin-bottom: 15px;">
          <%= request.getAttribute("errorMessage") %>
        </div>
      <% } %>

      <label for="username">Username</label>
      <input id="username" name="username" type="text" required/>

      <label for="name">Full Name</label>
      <input
        id="name"
        name="name"
        type="text"
        placeholder="Enter your name (eg. John Doe)"
        required
      />

      <label for="password">Password</label>
      <input id="password" name="password" type="password" required />

      <button type="submit">Register</button>

      <div class="link">
        Already have an account? <a href="login.jsp">Login here</a>
      </div>
    </form>
  </body>
</html>