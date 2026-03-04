<%
    HttpSession s = request.getSession(false);
    if (s != null) {
        s.invalidate();
    }
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
<link rel="manifest" href="manifest.json">
<meta name="theme-color" content="#00b4d8">
  <meta charset="UTF-8">
  <title>Bank Login | Bank Management System</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
  <style>
    :root { --bg1:#0f172a; --bg2:#1e293b; --glass:rgba(255,255,255,.08); --border:rgba(255,255,255,.15); }
    body{
      min-height:100vh; margin:0; display:grid; place-items:center;
      background: radial-gradient(80rem 60rem at -10% -20%, #334155 0%, transparent 60%),
                  radial-gradient(70rem 60rem at 120% 120%, #1d4ed8 0%, transparent 55%),
                  linear-gradient(135deg, var(--bg1), var(--bg2));
      color:#e5e7eb; font-family: system-ui,-apple-system,Segoe UI,Roboto,Inter,"Helvetica Neue",Arial;
    }
    .shell{
      width:min(1040px, 92vw);
      display:grid; grid-template-columns: 1.2fr 1fr; gap:28px; align-items:stretch;
    }
    .hero{
      padding:40px; border:1px solid var(--border); border-radius:24px;
      background: linear-gradient(180deg, rgba(255,255,255,.06), rgba(255,255,255,.03));
      backdrop-filter: blur(12px);
      box-shadow: 0 10px 35px rgba(0,0,0,.35);
    }
    .hero h1{ font-weight:800; letter-spacing:.4px; }
    .hero p{ color:#cbd5e1; }
    .badge-pill{ background:#0ea5e9; color:#06131f; font-weight:700; padding:.35rem .7rem; border-radius:999px; }
    .login-card{
      padding:34px; border:1px solid var(--border); border-radius:24px;
      background: var(--glass); backdrop-filter: blur(14px);
      box-shadow: 0 10px 35px rgba(0,0,0,.35);
    }
    .form-control, .form-control:focus{
      background:rgba(255,255,255,.06); color:#f8fafc; border-color:#334155;
    }
    .btn-primary{ background:#10b981; border:none; font-weight:700; }
    .btn-outline{ border:1px solid #475569; color:#cbd5e1; }
    .logo{ display:flex; gap:12px; align-items:center; font-weight:900; letter-spacing:.5px; }
    .logo i{ color:#22d3ee; }
    .footer{ text-align:center; margin-top:16px; color:#94a3b8; font-size:.9rem; }
    @media (max-width: 900px){ .shell{ grid-template-columns: 1fr; } }

    /* 💬 Chatbot styling */
    #chat-input {
      color: #111 !important;
      background-color: #fff !important;
      border: 1px solid #ccc;
    }
    .bot-msg {
      background:#e2e8f0; color:#1e293b; margin:6px 0; padding:10px 14px;
      border-radius:14px; max-width:80%; width:fit-content;
    }
    .user-msg {
      background:#0ea5e9; color:white; margin:6px 0; margin-left:auto;
      padding:10px 14px; border-radius:14px; max-width:80%; width:fit-content;
    }
  </style>
</head>
<body>

<div class="shell">
  <!-- Left brand / pitch -->
  <div class="hero">
    <div class="logo"><i class="fa-solid fa-building-columns fa-xl"></i> <span>RANA BANK</span></div>
    <h1 class="mt-4">Smarter Banking for Everyone</h1>
    <p class="mt-3">Secure deposits, instant transfers, and clean insights. Built with Java, JSP, Servlets, and MySQL — deployed on Tomcat.</p>
    <div class="d-flex gap-2 mt-4">
      <span class="badge-pill">Secure</span>
      <span class="badge-pill" style="background:#a78bfa;">Fast</span>
      <span class="badge-pill" style="background:#fb7185;">Reliable</span>
    </div>
    <div class="footer">© 2025 Bank Management Portal — Developed by <b>Anurag Rana</b></div>
  </div>

  <!-- Right login -->
  <div class="login-card">
    <h3 class="fw-bold mb-3">Welcome back 👋</h3>
    <p class="text-secondary mb-4">Log in with your Card Number & PIN</p>

    <form action="login" method="post" class="vstack gap-3">
      <div>
        <label class="form-label">Card Number</label>
        <input type="text" name="account" class="form-control" placeholder="16-digit card number" required>
      </div>
      <div>
        <label class="form-label">PIN</label>
        <input type="password" name="pin" class="form-control" placeholder="4-digit PIN" maxlength="4" required>
      </div>
      <div class="g-recaptcha" data-sitekey="6LcBcQgsAAAAAIo0rXCEK2OI6MIq_v0QkIiSrbH7"></div>

      <button class="btn btn-primary w-100 py-2 mt-2"><i class="fa-solid fa-right-to-bracket me-2"></i>Login</button>
      <a href="signup.jsp" class="btn btn-outline w-100 py-2">Create a new account</a>

      <p class="text-center mt-3"><a href="forgotpin.jsp" class="text-primary">Forgot PIN?</a></p>
      <p class="text-center mt-3"><a href="support.jsp" class="text-secondary">Contact Support</a></p>
    </form>
  </div>
</div>

<!-- ✅ Signup success toast -->
<%
  String created = request.getParameter("created");
  String card = request.getParameter("card");
%>
<script>
  <% if ("true".equals(created)) { %>
  Swal.fire({
    title: "Account Created 🎉",
    html: "Your card number: <b><%= card %></b><br/>Please keep your PIN safe.",
    icon: "success", confirmButtonText: "Login now"
  });
  <% } %>
</script>

<script src="https://www.google.com/recaptcha/api.js" async defer></script>
<script>
if ('serviceWorker' in navigator) {
  navigator.serviceWorker.register('<%= request.getContextPath() %>/service-worker.js')
    .then(() => console.log("Service Worker Registered"));
}
</script>

<jsp:include page="chatbot.jsp" />

</body>
</html>