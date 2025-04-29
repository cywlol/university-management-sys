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
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Professor Dashboard</title>
    <style>
        :root {
            --primary-color: #3f51b5;
            --primary-dark: #303f9f;
            --danger-color: #f44336;
            --danger-dark: #d32f2f;
            --background-color: #eef2f3;
            --card-color: #ffffff;
            --border-color: #e0e0e0;
            --text-color: #333333;
        }
        
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: var(--background-color);
            color: var(--text-color);
            line-height: 1.6;
        }
        
        header {
            background-color: var(--primary-color);
            color: white;
            padding: 1.5rem;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            position: relative;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 1.5rem;
        }
        
        .card {
            background-color: var(--card-color);
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
            padding: 1.5rem;
            margin-bottom: 2rem;
        }
        
        .section-title {
            color: var(--text-color);
            margin-bottom: 1.5rem;
            padding-bottom: 0.5rem;
            border-bottom: 2px solid var(--primary-color);
            font-size: 1.5rem;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 1rem;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12);
            border-radius: 4px;
            overflow: hidden;
        }
        
        th, td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid var(--border-color);
        }
        
        th {
            background-color: var(--primary-color);
            color: white;
            font-weight: 600;
        }
        
        tr:nth-child(even) {
            background-color: rgba(0, 0, 0, 0.02);
        }
        
        tr:hover {
            background-color: rgba(63, 81, 181, 0.05);
        }
        
        .btn {
            display: inline-block;
            padding: 8px 16px;
            border-radius: 4px;
            font-weight: 500;
            border: none;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .btn-primary {
            background-color: var(--primary-color);
            color: white;
        }
        
        .btn-primary:hover {
            background-color: var(--primary-dark);
        }
        
        .btn-danger {
            background-color: var(--danger-color);
            color: white;
        }
        
        .btn-danger:hover {
            background-color: var(--danger-dark);
        }
        
        .logout-button {
            position: absolute;
            top: 1.5rem;
            right: 1.5rem;
        }
        
        .empty-state {
            text-align: center;
            padding: 2rem;
            color: #666;
        }
        
        .course-count {
            display: inline-block;
            background-color: var(--primary-color);
            color: white;
            padding: 0.2rem 0.6rem;
            border-radius: 20px;
            font-size: 0.8rem;
            margin-left: 0.5rem;
        }
    </style>
</head>
<body>
    <header>
        <div class="container">
            <h1>Welcome, <%= professorName %>!</h1>
            <form action="<%= request.getContextPath() %>/logout" method="post" class="logout-button">
                <button type="submit" class="btn btn-danger">Logout</button>
            </form>
        </div>
    </header>

    <div class="container">
        <div class="card">
            <h2 class="section-title">
                Your Courses
                <% if (courses != null && !courses.isEmpty()) { %>
                    <span class="course-count"><%= courses.size() %></span>
                <% } %>
            </h2>
            
            <% if (courses != null && !courses.isEmpty()) { %>
                <table>
                    <thead>
                        <tr>
                            <th>Course ID</th>
                            <th>Name</th>
                            <th>Start Time</th>
                            <th>End Time</th>
                            <th>Size</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Course c : courses) { %>
                            <tr>
                                <td><%= c.getId() %></td>
                                <td><%= c.getName() %></td>
                                <td><%= c.getStartTime() %></td>
                                <td><%= c.getSize() %></td>
                                <td><%= c.getEndTime() %></td>
                                <td>
                                    <form action="<%= request.getContextPath() %>/professor/course" method="get">
                                        <input type="hidden" name="courseId" value="<%= c.getId() %>" />
                                        <button type="submit" class="btn btn-primary">View Students</button>
                                    </form>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <div class="empty-state">
                    <p>No courses assigned yet.</p>
                    <p>Your courses will appear here once they are assigned.</p>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>