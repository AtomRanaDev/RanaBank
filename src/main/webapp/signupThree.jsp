<!DOCTYPE html>
<html>
<head>
    <title>Signup - Step 3</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-5">
    <h2>Signup - Step 3</h2>
    <form action="SignupThreeServlet" method="post">
        <div class="mb-3"><label>Account Type:</label><input type="text" name="accountType" class="form-control" required></div>
        <div class="mb-3"><label>Card Number:</label><input type="text" name="cardNumber" class="form-control" required></div>
        <div class="mb-3"><label>PIN:</label><input type="password" name="pin" class="form-control" required></div>
        <div class="mb-3"><label>Facility:</label><input type="text" name="facility" class="form-control"></div>
        <button type="submit" class="btn btn-success">Finish</button>
    </form>
</body>
</html>
