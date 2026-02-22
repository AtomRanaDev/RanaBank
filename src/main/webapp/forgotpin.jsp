<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Forgot PIN | Bank System</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
  <style>
    body {
      background: #f0f2f5;
      font-family: 'Poppins', sans-serif;
    }
    .box {
      max-width: 400px;
      margin: 100px auto;
      background: white;
      padding: 30px;
      border-radius: 10px;
      box-shadow: 0 6px 18px rgba(0,0,0,0.1);
    }
  </style>
</head>
<body>
<div class="box">
  <h4 class="text-center mb-3 text-primary">Reset Your PIN</h4>
  <form action="ForgotPinServlet" method="post">
    <div class="mb-3">
      <label class="form-label">Enter Registered Card Number</label>
      <input type="text" name="card_number" class="form-control" required>
    </div>
    <button type="submit" class="btn btn-primary w-100">Send OTP</button>
  </form>
</div>
</body>
</html>
