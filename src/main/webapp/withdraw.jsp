<!DOCTYPE html>
<html>
<head>
    <title>Withdraw Money</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(145deg, #f1f1f1, #e0e0e0);
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }
        .card {
            padding: 30px;
            border-radius: 20px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            background-color: white;
        }
    </style>
</head>
<body>
    <div class="card text-center">
        <h3>Withdraw Funds</h3>
        <form action="WithdrawServlet" method="post">
            <input type="hidden" name="pin" value="${param.pin}">
            <input type="number" name="amount" class="form-control mt-3" placeholder="Enter Amount" required>
            <button type="submit" class="btn btn-warning mt-3 w-100">Withdraw</button>
        </form>
        <a href="DashboardServlet?pin=${param.pin}" class="btn btn-outline-dark mt-3 w-100">Back</a>
    </div>
    <jsp:include page="chatbot.jsp" />
</body>
</html>
