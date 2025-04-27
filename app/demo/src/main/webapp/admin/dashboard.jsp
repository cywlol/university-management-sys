<%@ page import="java.util.List" %>
<%@ page import="com.example.Course" %>
<%@ page import="com.example.Professor" %>
<%
List<Course> courses = (List<Course>) request.getAttribute("courses");
List<Professor> professors = (List<Professor>) request.getAttribute("professors");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #eef2f3; margin: 0; padding: 0; }
        header { background-color: #333; color: white; padding: 20px; text-align: center; }
        main { padding: 20px; max-width: 1000px; margin: auto; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 12px; border: 1px solid #ddd; text-align: center; }
        th { background-color: #555; color: white; }
    </style>
</head>
<body>

<header style="position: relative;">
    <h1 style="margin: 0;">Welcome, admin!</h1>
    <form action="<%= request.getContextPath() %>/logout" method="post" 
          style="position: absolute; top: 20px; right: 20px;">
        <button type="submit" style="
            background-color: #f44336;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 14px;
            transition: background-color 0.3s;">
            Logout
        </button>
    </form>
</header>

<main>

    <h2>Add New Course</h2>
    <form action="<%= request.getContextPath() %>/admin/addCourse" method="post" style="margin-bottom: 30px;">
        <input type="text" name="id" placeholder="Course ID (ex: CSE123)" required><br><br>
        <input type="text" name="name" placeholder="Course Name" required><br><br>
        <input type="text" name="startTime" placeholder="Start Time (ex: 09:00:00)" required><br><br>
        <input type="number" name="size" placeholder="Class Size" required><br><br>
        <input type="text" name="prerequisite" placeholder="Prerequisite Course ID (or leave blank)"><br><br>

        <label for="professor">Assign Professor:</label><br>
        <select name="professorId" required>
            <option value="">-- Select Professor --</option>
            <% for (Professor p : professors) { %>
                <option value="<%= p.getId() %>"><%= p.getName() %></option>
            <% } %>
        </select><br><br>

        <button type="submit">Add Course</button>
    </form>

    <h2>All Courses</h2>
    <table>
        <thead>
            <tr>
                <th>Course ID</th>
                <th>Name</th>
                <th>Start Time</th>
                <th>Size</th>
                <th>Prerequisite</th>
                <th>Professor ID</th>
            </tr>
        </thead>
        <tbody>
            <% for (Course c : courses) { %>
                <tr>
                    <td><%= c.getId() %></td>
                    <td><%= c.getName() %></td>
                    <td><%= c.getStartTime() %></td>
                    <td><%= c.getSize() %></td>
                    <td><%= c.getPrerequisite() != null ? c.getPrerequisite() : "None" %></td>
                    <td><%= c.getProfessorId() %></td>
                </tr>
            <% } %>
        </tbody>
    </table>

</main>

</body>
</html>
