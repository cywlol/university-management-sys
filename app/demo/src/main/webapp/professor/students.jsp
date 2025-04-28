<%@ page import="java.util.List" %>
<%@ page import="com.example.StudentGrade" %>
<%
List<StudentGrade> students = (List<StudentGrade>) request.getAttribute("students");
String courseId = (String) request.getAttribute("courseId");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Students - Course <%= courseId %></title>
    <style>
        :root {
            --primary-color: #3f51b5;
            --primary-dark: #303f9f;
            --primary-light: #c5cae9;
            --accent-color: #ff4081;
            --accent-dark: #c60055;
            --success-color: #4caf50;
            --background-color: #f5f5f5;
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
        
        .page-title {
            text-align: center;
            margin-bottom: 0.5rem;
        }
        
        .course-id {
            display: inline-block;
            background-color: rgba(255, 255, 255, 0.2);
            padding: 0.2rem 0.8rem;
            border-radius: 20px;
            font-size: 1rem;
            margin-left: 0.5rem;
        }
        
        .back-btn {
            position: absolute;
            top: 1.5rem;
            left: 1.5rem;
            display: inline-flex;
            align-items: center;
            color: white;
            text-decoration: none;
            font-size: 0.9rem;
        }
        
        .back-arrow {
            margin-right: 5px;
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
        
        .grade-input {
            width: 100%;
            padding: 8px 12px;
            border: 1px solid var(--border-color);
            border-radius: 4px;
            font-size: 0.9rem;
            transition: border-color 0.3s;
        }
        
        .grade-input:focus {
            outline: none;
            border-color: var(--primary-color);
            box-shadow: 0 0 0 2px rgba(63, 81, 181, 0.2);
        }
        
        .current-grade {
            font-weight: 500;
            padding: 4px 10px;
            border-radius: 20px;
            background-color: var(--primary-light);
            display: inline-block;
        }
        
        .submit-btn {
            background-color: var(--primary-color);
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 4px;
            font-size: 1rem;
            cursor: pointer;
            margin-top: 1.5rem;
            float: right;
            transition: background-color 0.3s;
        }
        
        .submit-btn:hover {
            background-color: var(--primary-dark);
        }
        
        .empty-state {
            text-align: center;
            padding: 2rem;
            color: var(--text-light);
        }
    </style>
</head>
<body>
    <header>
        <div class="container">
            <a href="<%= request.getContextPath() %>/professor/dashboard" class="back-btn">
                <span class="back-arrow"><-</span> Back to Dashboard
            </a>
            <h1 class="page-title">
                Students in Course <span class="course-id"><%= courseId %></span>
            </h1>
        </div>
    </header>

    <div class="container">
        <div class="card">
            <% if (students != null && !students.isEmpty()) { %>
                <form action="<%= request.getContextPath() %>/professor/updateGrades" method="post">
                    <input type="hidden" name="courseId" value="<%= courseId %>">
                    <table>
                        <thead>
                            <tr>
                                <th>Student ID</th>
                                <th>Name</th>
                                <th>Current Grade</th>
                                <th>New Grade</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (StudentGrade s : students) { %>
                            <tr>
                                <td><%= s.getStudentId() %></td>
                                <td><%= s.getStudentName() %></td>
                                <td>
                                    <% if (s.getGrade() != null && !s.getGrade().isEmpty()) { %>
                                        <span class="current-grade"><%= s.getGrade() %></span>
                                    <% } else { %>
                                        <span class="text-light">Not graded</span>
                                    <% } %>
                                </td>
                                <td>
                                    <input type="text" 
                                           class="grade-input" 
                                           name="grades[<%= s.getStudentId() %>]" 
                                           placeholder="Enter New Grade"
                                           value="<%= s.getGrade() != null ? s.getGrade() : "" %>">
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                    <button type="submit" class="submit-btn">Update Grades</button>
                </form>
            <% } else { %>
                <div class="empty-state">
                    <p>No students are currently enrolled in this course.</p>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>