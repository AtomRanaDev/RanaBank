<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Signup | Bank Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: #f5f6fa;
            font-family: 'Poppins', sans-serif;
        }
        .form-container {
            max-width: 700px;
            background: #fff;
            padding: 40px;
            margin: 50px auto;
            border-radius: 15px;
            box-shadow: 0 6px 20px rgba(0,0,0,0.1);
        }
        h2 {
            text-align: center;
            margin-bottom: 30px;
            color: #007bff;
        }
    </style>
</head>
<body>

<div class="form-container">
    <h2>New Account Registration</h2>

    <form action="SignupServlet" method="post">
        <div class="mb-3">
            <label class="form-label">Full Name</label>
            <input type="text" name="name" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Father's Name</label>
            <input type="text" name="father_name" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Date of Birth</label>
            <input type="date" name="dob" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Gender</label>
            <select name="gender" class="form-select" required>
                <option value="">-- Select Gender --</option>
                <option>Male</option>
                <option>Female</option>
                <option>Other</option>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">Religion</label>
            <select name="religion" class="form-select" required>
                <option value="">-- Select Religion --</option>
                <option>Hindu</option>
                <option>Muslim</option>
                <option>Sikh</option>
                <option>Christian</option>
                <option>Other</option>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">Category</label>
            <select name="category" class="form-select" required>
                <option value="">-- Select Category --</option>
                <option>General</option>
                <option>OBC</option>
                <option>SC/ST</option>
                <option>BC</option>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">Education</label>
            <select name="education" class="form-select" required>
                <option value="">-- Select Education Level --</option>
                <option>High School</option>
                <option>Intermediate</option>
                <option>Graduate</option>
                <option>Post Graduate</option>
                <option>Doctorate</option>
            </select>
        </div>

        <div class="mb-3">
            <label class="form-label">Email</label>
            <input type="email" name="email" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Address</label>
            <textarea name="address" class="form-control" required></textarea>
        </div>

        <div class="mb-3">
            <label class="form-label">City</label>
            <input type="text" name="city" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label">State</label>
            <input type="text" name="state" class="form-control" required>
        </div>

        <div class="mb-3">
            <label class="form-label">Pincode</label>
            <input type="text" name="pincode" class="form-control" required pattern="[0-9]{6}">
        </div>

        <div class="d-grid">
            <button type="submit" class="btn btn-primary btn-lg">Create Account</button>
        </div>
    </form>
</div>

</body>
</html>
