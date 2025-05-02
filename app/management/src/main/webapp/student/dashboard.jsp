<%@ page import="java.util.List" %>
<%@ page import="servlets.Course" %>
<%
List<Course> courses = (List<Course>) session.getAttribute("studentCourses");
List<Course> allCourses = (List<Course>) session.getAttribute("allCourses");
List<Course> coursesQuery = (List<Course>) request.getAttribute("courseSearch");
String studentName = (String) session.getAttribute("student_name");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard</title>
    <style>
        :root {
            --primary-color: #4caf50;
            --primary-dark: #3e8e41;
            --danger-color: #f44336;
            --danger-dark: #d32f2f;
            --background-color: #f5f5f5;
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
            background-color: rgba(76, 175, 80, 0.05);
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
        
        .enroll-form {
            display: flex;
            gap: 10px;
            justify-content: center;
            align-items: center;
            margin: 1.5rem 0;
        }
        
        select {
            padding: 10px;
            border-radius: 4px;
            border: 1px solid var(--border-color);
            font-size: 1rem;
            min-width: 300px;
        }
        
        .empty-state {
            text-align: center;
            padding: 2rem;
            color: #666;
        }
        
        .status-pill {
            display: inline-block;
            padding: 4px 10px;
            border-radius: 20px;
            font-size: 0.85rem;
            color: white;
            background-color: #2196F3;
        }
        
        .status-in-progress {
            background-color: #2196F3;
        }
        
        .status-graded {
            background-color: #FF9800;
        }

        .search {
            padding: 10px;
            border-radius: 4px;
            border: 1px solid var(--border-color);
            font-size: 1rem;
            min-width: 300px;
        }
    </style>
</head>
<body>
    <header>
        <div class="container">
            <h1>Welcome, <%= studentName %>!</h1>
            <form action="<%= request.getContextPath() %>/logout" method="post" class="logout-button">
                <button type="submit" class="btn btn-danger">Logout</button>
            </form>
        </div>
    </header>
    
    <div class="container">
        <div class="card">
            <h2 class="section-title">Your Enrolled Courses</h2>
            
            <% if (courses != null && !courses.isEmpty()) { %>
                <table>
                    <thead>
                        <tr>
                            <th>Course ID</th>
                            <th>Name</th>
                            <th>Start Time</th>
                            <th>End Time</th>
                            <th>Size</th>
                            <th>Professor</th>
                            <th>Grade</th>
                            <th>Action</th>
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
                                <td>
                                    <span class="status-pill <%= c.getGrade() != null ? "status-graded" : "status-in-progress" %>">
                                        <%= c.getGrade() != null ? c.getGrade() : "In Progress" %>
                                    </span>
                                </td>
                                <td>
                                    <form action="<%= request.getContextPath() %>/student/unenroll" method="post">
                                        <input type="hidden" name="courseId" value="<%= c.getId() %>" />
                                        <button type="submit" class="btn btn-danger">Unenroll</button>
                                    </form>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <div class="empty-state">
                    <p>You are not enrolled in any courses yet.</p>
                </div>
            <% } %>
        </div>
        
        <div class="card">
            <h2 class="section-title">Enroll in a New Course</h2>
            
            <% if (allCourses != null && !allCourses.isEmpty()) { %>
                <form action="<%= request.getContextPath() %>/student/enroll" method="post" class="enroll-form">
                    <select name="courseId" required>
                        <% for (Course c : allCourses) { %>
                            <option value="<%= c.getId() %>">
                                <%= c.getId() %> - <%= c.getName() %>
                            </option>
                        <% } %>
                    </select>
                    <button type="submit" class="btn btn-primary">Enroll</button>
                </form>
            <% } else { %>
                <div class="empty-state">
                    <p>No courses to display. Use the search function above to search for a course.</p>
                </div>
            <% } %>
        </div>
         <div class="card">
            <h2 class="section-title">Search for a Course</h2>
            <form action="<%= request.getContextPath() %>/student/search" method="post" class="search-form">
                <input type="text" class="search" name="courseName" placeholder="Enter course name" required>
                <button type="submit" class="btn btn-primary">Search</button>
            </form>
            <h2 class="section-title">
                Courses
                <% if (coursesQuery != null) { %>
                    <span class="badge"><%="[" + coursesQuery.size() + "]" %></span>
                <% } %>
            </h2>
            
            <% if (coursesQuery != null && !coursesQuery.isEmpty()) { %>
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
                        <% for (Course c : coursesQuery) { %>
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