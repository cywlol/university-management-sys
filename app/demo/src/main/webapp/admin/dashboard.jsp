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
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
    <style>
        :root {
            --primary-color: #333333;
            --primary-dark: #1a1a1a;
            --primary-light: #4d4d4d;
            --accent-color: #f44336;
            --accent-dark: #d32f2f;
            --success-color: #4caf50;
            --background-color: #eef2f3;
            --card-color: #ffffff;
            --border-color: #e0e0e0;
            --text-color: #333333;
            --text-light: #757575;
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
            background-image: linear-gradient(to right, var(--primary-color), var(--primary-dark));
            color: white;
            padding: 1.5rem;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
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
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
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
        
        .logout-button {
            position: absolute;
            top: 1.5rem;
            right: 1.5rem;
        }
        
        .btn {
            display: inline-block;
            padding: 10px 20px;
            border-radius: 4px;
            font-weight: 500;
            border: none;
            cursor: pointer;
            transition: all 0.3s ease;
            font-size: 0.9rem;
        }
        
        .btn-primary {
            background-color: var(--primary-color);
            color: white;
        }
        
        .btn-primary:hover {
            background-color: var(--primary-dark);
        }
        
        .btn-danger {
            background-color: var(--accent-color);
            color: white;
        }
        
        .btn-danger:hover {
            background-color: var(--accent-dark);
        }
        
        .btn-success {
            background-color: var(--success-color);
            color: white;
        }
        
        .btn-success:hover {
            background-color: #3d8b40;
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
            background-color: rgba(0, 0, 0, 0.05);
        }
        
        .form-group {
            margin-bottom: 1.2rem;
        }
        
        .form-label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 500;
        }
        
        .form-control {
            width: 100%;
            padding: 10px;
            border: 1px solid var(--border-color);
            border-radius: 4px;
            font-size: 1rem;
            transition: border-color 0.3s, box-shadow 0.3s;
        }
        
        .form-control:focus {
            outline: none;
            border-color: var(--primary-color);
            box-shadow: 0 0 0 2px rgba(51, 51, 51, 0.2);
        }
        
        .form-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            grid-gap: 1rem;
        }
        
        .form-grid-full {
            grid-column: 1 / -1;
        }
        
        .empty-state {
            text-align: center;
            padding: 2rem;
            color: var(--text-light);
        }
        
        .badge {
            display: inline-block;
            padding: 0.25rem 0.5rem;
            border-radius: 4px;
            font-size: 0.75rem;
            font-weight: 500;
            text-transform: uppercase;
            background-color: var(--primary-light);
            color: white;
        }
    </style>
</head>
<body>
    <header>
        <div class="container">
            <h1>Welcome, Administrator</h1>
            <form action="<%= request.getContextPath() %>/logout" method="post" class="logout-button">
                <button type="submit" class="btn btn-danger">Logout</button>
            </form>
        </div>
    </header>

    <div class="container">
        <div class="card">
            <h2 class="section-title">Add New Course</h2>
            <% String error = (String) request.getAttribute("error");
            if (error != null) { %>
                <div style="color: red; margin-bottom: 1rem; font-weight: bold;">
                    <%= error %>
                </div>
            <% } %>
            <form action="<%= request.getContextPath() %>/admin/addCourse" method="post">
                <div class="form-grid">
                    <div class="form-group">
                        <label class="form-label" for="courseId">Course ID</label>
                        <input type="text" id="courseId" name="id" class="form-control" placeholder="e.g. CSE123" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="courseName">Course Name</label>
                        <input type="text" id="courseName" name="name" class="form-control" placeholder="e.g. Introduction to Programming" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="startTime">Start Time</label>
                        <input type="text" id="startTime" name="startTime" class="form-control" placeholder="e.g. 9:00 PM" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="endTime">End Time</label>
                        <input type="text" id="endTime" name="endTime" class="form-control" placeholder="e.g. 10:00 AM" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="size">Class Size</label>
                        <input type="number" id="size" name="size" class="form-control" placeholder="e.g. 30" required>
                    </div>
                    <div class="form-group">
                        <label class="form-label" for="professor">Assign Professor</label>
                        <select id="professor" name="professorId" class="form-control" required>
                            <option value="">-- Select Professor --</option>
                            <% if (professors != null && !professors.isEmpty()) {
                                for (Professor p : professors) { %>
                                    <option value="<%= p.getId() %>"><%= p.getName() %></option>
                            <% }
                            } %>
                        </select>
                    </div>
                    <div class="form-group form-grid-full" style="text-align: right; margin-top: 1rem;">
                        <button type="submit" class="btn btn-success">Add Course</button>
                    </div>
                </div>
            </form>
        </div>

        <div class="card">
            <h2 class="section-title">
                All Courses
                <% if (courses != null) { %>
                    <span class="badge"><%= courses.size() %> courses</span>
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
                            <th>Professor Name</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Course c : courses) { %>
                            <tr>
                                <td><%= c.getId() %></td>
                                <td><%= c.getName() %></td>
                                <td><%= c.getStartTime() %></td>
                                <td><%= c.getEndTime() %></td>
                                <td><%= c.getSize() %></td>
                                <td><%= c.getProfessorName() %></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <div class="empty-state">
                    <p>No courses available. Add a new course using the form above.</p>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>