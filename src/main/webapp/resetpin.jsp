<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
<title>Reset PIN</title>


<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

<style>
body{
background:#f0f2f5;
font-family:Poppins;
}

.box{
max-width:400px;
margin:120px auto;
background:white;
padding:30px;
border-radius:10px;
box-shadow:0 6px 18px rgba(0,0,0,0.1);
}
</style>

</head>

<body>

<div class="box">

<h4 class="text-center text-primary mb-3">Verify OTP</h4>

<form action="ResetPinServlet" method="post">

<div class="mb-3">
<label>Enter OTP</label>
<input type="text" name="otp" class="form-control" required>
</div>

<div class="mb-3">
<label>New PIN</label>
<input type="password" name="newpin"
       class="form-control"
       maxlength="4"
       pattern="[0-9]{4}"
       placeholder="Enter 4-digit PIN"
       required>
</div>

<button type="submit" class="btn btn-success w-100">
Reset PIN
</button>

</form>

</div>

</body>
</html>