<!DOCTYPE html>
<html>
<head>
    <title>Signup - Step 2</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-5">
    <h2>Signup - Step 2</h2>
    <form action="SignupTwoServlet" method="post">
        <div class="mb-3"><label>Religion:</label><input type="text" name="religion" class="form-control" required></div>
        <div class="mb-3"><label>Category:</label><input type="text" name="category" class="form-control" required></div>
        <div class="mb-3"><label>Income:</label><input type="text" name="income" class="form-control"></div>
        <div class="mb-3"><label>Education:</label><input type="text" name="education" class="form-control"></div>
        <div class="mb-3"><label>Occupation:</label><input type="text" name="occupation" class="form-control"></div>
        <button type="submit" class="btn btn-primary">Next</button>
    </form>
</body>
</html>
