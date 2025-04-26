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
    <title>Manage Students - Course <%= courseId %></title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f2f2f2; margin: 0; padding: 0; }
        header { background-color: #3f51b5; color: white; padding: 20px; text-align: center; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { padding: 12px; border: 1px solid #ddd; text-align: center; }
        th { background-color: #3f51b5; color: white; }
    </style>
</head>
<body>

<header>
    <h1>Students in Course: <%= courseId %></h1>
</header>

<main style="padding:20px;">
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
                    <td><%= s.getGrade() %></td>
                    <td>
                        <input type="text" name="grades[<%= s.getStudentId() %>]" placeholder="Enter New Grade">
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
        <button type="submit" style="margin-top:20px;">Update Grades</button>
    </form>
</main>

</body>
</html>
