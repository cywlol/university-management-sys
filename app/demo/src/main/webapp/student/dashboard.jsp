<%@ page import="java.util.List" %>
<%@ page import="com.example.Course" %>
<%
List<Course> courses = (List<Course>) session.getAttribute("studentCourses");
List<Course> allCourses = (List<Course>) session.getAttribute("allCourses"); // NEW line
%>

    <!DOCTYPE html>
    <html lang="en">
      <head>
        <meta charset="UTF-8" />
        <title>Student Dashboard</title>
        <style>
          body {
            font-family: Arial, sans-serif;
            background-color: #f2f2f2;
            margin: 0;
            padding: 0;
          }
          header {
            background-color: #4caf50;
            color: white;
            padding: 20px;
            text-align: center;
          }
          main {
            padding: 20px;
            max-width: 1000px;
            margin: auto;
          }
          table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
          }
          th,
          td {
            padding: 12px;
            border: 1px solid #ddd;
            text-align: center;
          }
          th {
            background-color: #4caf50;
            color: white;
          }
          tr:nth-child(even) {
            background-color: #f9f9f9;
          }
          .title {
            margin-top: 20px;
            text-align: center;
          }
        </style>
      </head>
      <body>
        <header>
          <h1>Welcome to Your Dashboard</h1>
        </header>

        <main>
          <h2 class="title">Your Enrolled Courses</h2>

          <table>
            <thead>
              <tr>
                <th>Course ID</th>
                <th>Name</th>
                <th>Start Time</th>
                <th>Size</th>
                <th>Prerequisite</th>
                <th>Professor ID</th>
                <th>Grade</th> 
 
              </tr>
            </thead>
            <tbody>
              <% if (courses != null && !courses.isEmpty()) { for (Course c :
              courses) { %>
              <tr>
                <td><%= c.getId() %></td>
                <td><%= c.getName() %></td>
                <td><%= c.getStartTime() %></td>
                <td><%= c.getSize() %></td>
                <td><%= c.getPrerequisite() %></td>
                <td><%= c.getProfessorId() %></td>
                <td><%= c.getGrade() != null ? c.getGrade() : "In Progress" %></td> <!-- NEW -->
              </tr>
              <% } } else { %>
              <tr>
                <td colspan="6">You are not enrolled in any courses yet.</td>
              </tr>
              <% } %>
              
            </tbody>
            
          </table>
        </main>
        <h2 class="title">Enroll in a New Course</h2>

      <form action="<%= request.getContextPath() %>/student/enroll" method="post" style="text-align: center; margin-top: 20px;">
          <select name="courseId" required>
            <% if (allCourses != null && !allCourses.isEmpty()) {
                for (Course c : allCourses) { %>
                  <option value="<%= c.getId() %>">
                    <%= c.getId() %> - <%= c.getName() %>
                  </option>
            <% } } else { %>
                  <option disabled>No courses available</option>
            <% } %>
          </select>
          <button type="submit">Enroll</button>
      </form>
      </body>
    </html>
  </Course></Course
>
