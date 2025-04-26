<%@ page import="java.util.List" %>
<%@ page import="com.example.Course" %>
<%
List<Course> courses = (List<Course>) session.getAttribute("professorCourses");
String professorName = (String) session.getAttribute("professor_name");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Professor Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #eef2f3;
            margin: 0;
            padding: 0;
        }
        header {
            background-color: #3f51b5;
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
        th, td {
            padding: 12px;
            border: 1px solid #ddd;
            text-align: center;
        }
        th {
            background-color: #3f51b5;
            color: white;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
    </style>
</head>
<body>
    <header>
        <h1>Welcome, <%= professorName %>!</h1>
    </header>

    <main>
        <h2>Your Courses</h2>
        <table>
            <thead>
                <tr>
                    <th>Course ID</th>
                    <th>Name</th>
                    <th>Start Time</th>
                    <th>Size</th>
                    <th>Prerequisite</th>
                </tr>
            </thead>
            <tbody>
                <% if (courses != null && !courses.isEmpty()) {
                    for (Course c : courses) { %>
                        <tr>
                            <td><%= c.getId() %></td>
                            <td><%= c.getName() %></td>
                            <td><%= c.getStartTime() %></td>
                            <td><%= c.getSize() %></td>
                            <td><%= c.getPrerequisite() != null ? c.getPrerequisite() : "None" %></td>
                            <td>
                    <form action="<%= request.getContextPath() %>/professor/course" method="get">
                        <input type="hidden" name="courseId" value="<%= c.getId() %>" />
                        <button type="submit">View Students</button>
                    </form>
                </td>
                        </tr>
                <% }
                } else { %>
                    <tr>
                        <td colspan="5">No courses assigned yet.</td>
                    </tr>
                <% } %>
                
            </tbody>
        </table>
    </main>
</body>
</html>
