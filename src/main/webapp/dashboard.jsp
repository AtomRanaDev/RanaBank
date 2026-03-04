<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard | Bank Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <style>
        :root {
            --bg-color: #f8f9fa;
            --text-color: #212529;
            --card-bg: #ffffff;
            --sidebar-bg: #1e1e2f;
            --sidebar-text: #fff;
        }

        body.dark-mode {
            --bg-color: #121212;
            --text-color: #f8f9fa;
            --card-bg: #1f1f1f;
            --sidebar-bg: #0f0f1a;
            --sidebar-text: #bbb;
        }

        body {
            font-family: 'Poppins', sans-serif;
            background-color: var(--bg-color);
            color: var(--text-color);
            display: flex;
            min-height: 100vh;
            margin: 0;
        }

        .sidebar {
            width: 260px;
            background-color: var(--sidebar-bg);
            color: var(--sidebar-text);
            display: flex;
            flex-direction: column;
            padding-top: 30px;
        }

        .sidebar h4 {
            text-align: center;
            font-weight: 600;
            margin-bottom: 40px;
        }

        .sidebar a {
            color: #bbb;
            text-decoration: none;
            display: block;
            padding: 12px 25px;
            transition: all 0.3s ease;
        }

        .sidebar a:hover, .sidebar a.active {
            background: #34344e;
            color: #fff;
        }

        .content {
            flex-grow: 1;
            padding: 40px;
        }

        .welcome-box {
            background-color: var(--card-bg);
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            padding: 30px;
        }

        .balance-card {
            background: linear-gradient(135deg, #007bff, #00b4d8);
            color: white;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 5px 10px rgba(0,0,0,0.1);
            margin-bottom: 25px;
        }

        .btn-custom {
            border-radius: 25px;
            font-weight: 500;
            margin: 8px 0;
            width: 180px;
        }

        footer {
            position: fixed;
            bottom: 10px;
            right: 20px;
            left: 260px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 0 20px;
            color: #6c757d;
            font-size: 14px;
            font-weight: 500;
        }

        #themeToggle {
            border-radius: 20px;
        }
    </style>
</head>
<body>

<!-- Sidebar -->
<div class="sidebar">
    <h4><i class="fa-solid fa-building-columns"></i> Bank Portal</h4>
    <a href="DashboardServlet?pin=${pin}" class="active"><i class="fa-solid fa-house me-2"></i> Dashboard</a>
    <a href="#" onclick="openDepositModal()"><i class="fa-solid fa-arrow-down me-2"></i> Deposit</a>
    <a href="#" onclick="openWithdrawModal()"><i class="fa-solid fa-arrow-up me-2"></i> Withdraw</a>
    <a href="MiniStatementServlet?pin=${pin}"><i class="fa-solid fa-receipt me-2"></i> Transactions</a>
    <a href="support.jsp"><i class="fa-solid fa-headset me-2"></i> Support</a>
    <a href="LogoutServlet">
        <i class="fa-solid fa-right-from-bracket me-2"></i> Logout
    </a>
</div>

<!-- Content -->
<div class="content">
    <div class="welcome-box text-center">
        <h2>Welcome, ${name != null ? name : "User"} 👋</h2>
        <p class="text-muted">Card Number: <b>${cardNumber}</b></p>
        <div class="balance-card">
            <h4>Current Balance</h4>
            <h2 id="balanceDisplay">
                ₹ <%= String.format("%.2f", (Double) request.getAttribute("balance")) %>
            </h2>
        </div>
    </div>
</div>

<!-- Footer -->
<footer>
    <span>Developed by <b>Anurag Rana</b> 💻</span>
    <button id="themeToggle" class="btn btn-sm btn-outline-secondary">
        <i class="fa-solid fa-moon"></i> Dark Mode
    </button>
</footer>

<!-- Deposit Modal -->
<div class="modal fade" id="depositModal" tabindex="-1">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Deposit Money</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <input type="number" id="depositAmount" class="form-control" placeholder="Enter amount" min="1">
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
        <button type="button" class="btn btn-success" onclick="processDeposit()">Deposit</button>
      </div>
    </div>
  </div>
</div>

<!-- Withdraw Modal -->
<div class="modal fade" id="withdrawModal" tabindex="-1">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Withdraw Money</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
        <input type="number" id="withdrawAmount" class="form-control" placeholder="Enter amount" min="1">
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
        <button type="button" class="btn btn-warning" onclick="processWithdraw()">Withdraw</button>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    const pin = "${pin}";
    let balance = parseFloat("${balance}") || 0;

    // Deposit and Withdraw
    function openDepositModal() { new bootstrap.Modal(document.getElementById('depositModal')).show(); }
    function openWithdrawModal() { new bootstrap.Modal(document.getElementById('withdrawModal')).show(); }

    async function processDeposit() {
        const amountField = document.getElementById('depositAmount');
        const amount = parseFloat(amountField.value);
        if (!amount || amount <= 0) {
            Swal.fire("Invalid amount", "Please enter a valid deposit value", "error");
            return;
        }
        const res = await fetch("DepositApiServlet", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
         body: new URLSearchParams({ amount })
        });
        const data = await res.json();
        if (data.ok) {
            balance = data.newBalance;
            document.getElementById("balanceDisplay").innerText = "₹ " + balance.toFixed(2);
            Swal.fire("Deposit Successful 💰", `₹${amount.toFixed(2)} added!`, "success");
        } else Swal.fire("Error", data.error || "Deposit failed", "error");
        bootstrap.Modal.getInstance(document.getElementById('depositModal')).hide();
        amountField.value = "";
    }

    async function processWithdraw() {
        const amountField = document.getElementById('withdrawAmount');
        const amount = parseFloat(amountField.value);
        if (!amount || amount <= 0) {
            Swal.fire("Invalid amount", "Please enter a valid withdrawal value", "error");
            return;
        }
        const res = await fetch("WithdrawApiServlet", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
           body: new URLSearchParams({ amount })
        });
        const data = await res.json();
        if (data.ok) {
            balance = data.newBalance;
            document.getElementById("balanceDisplay").innerText = "₹ " + balance.toFixed(2);
            Swal.fire("Withdrawal Successful 🏧", `₹${amount.toFixed(2)} withdrawn!`, "success");
        } else Swal.fire("Error", data.error || "Withdrawal failed", "error");
        bootstrap.Modal.getInstance(document.getElementById('withdrawModal')).hide();
        amountField.value = "";
    }

    // 🌙 Dark Mode Toggle
    const themeToggle = document.getElementById('themeToggle');
    if (localStorage.getItem('theme') === 'dark') {
        document.body.classList.add('dark-mode');
        themeToggle.innerHTML = '<i class="fa-solid fa-sun"></i> Light Mode';
    }
    themeToggle.addEventListener('click', () => {
        document.body.classList.toggle('dark-mode');
        const dark = document.body.classList.contains('dark-mode');
        themeToggle.innerHTML = dark
            ? '<i class="fa-solid fa-sun"></i> Light Mode'
            : '<i class="fa-solid fa-moon"></i> Dark Mode';
        localStorage.setItem('theme', dark ? 'dark' : 'light');
    });
</script>
<jsp:include page="chatbot.jsp" />
</body>
</html>
