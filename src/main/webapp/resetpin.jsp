<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Reset PIN</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="bg-light">
<div class="container mt-5" style="max-width:450px;">
  <div class="card shadow p-4">
    <h4 class="text-center mb-3 text-primary">Enter OTP & New PIN</h4>
    <form action="ResetPinServlet" method="post">
      <div class="mb-3">
        <label>Enter OTP</label>
        <input type="text" name="otp" class="form-control" required>
      </div>
      <div class="mb-3">
        <label>New PIN</label>
        <input type="password" name="newpin" class="form-control" required minlength="4" maxlength="4">
      </div>
      <button type="submit" class="btn btn-success w-100">Update PIN</button>
    </form>
  </div>
</div>
</body>
</html>
