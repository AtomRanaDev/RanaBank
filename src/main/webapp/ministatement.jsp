<!DOCTYPE html>
<html>
<head>
    <title>Mini Statement</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(145deg, #f8f8f8, #e8e8e8);
            font-family: 'Poppins', sans-serif;
        }
        .container {
            margin-top: 50px;
            max-width: 800px;
        }
        .card {
            border-radius: 16px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            background-color: white;
        }
        th {
            background-color: #212529;
            color: white;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="card p-4">
            <h2 class="text-center mb-4">Recent Transactions</h2>
            <table class="table table-hover text-center">
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Type</th>
                        <th>Amount</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        java.util.List<String[]> transactions = (java.util.List<String[]>) request.getAttribute("transactions");
                        if (transactions != null && !transactions.isEmpty()) {
                            for (String[] row : transactions) {
                    %>
                    <tr>
                        <td><%= row[0] %></td>
                        <td><%= row[1] %></td>
                        <td><%= row[2] %></td>
                    </tr>
                    <%
                            }
                        } else {
                    %>
                    <tr><td colspan="3">No transactions found.</td></tr>
                    <% } %>
                </tbody>
            </table>
            <div class="text-center mt-3">
                <a href="DashboardServlet?pin=<%= request.getAttribute("pin") %>" class="btn btn-dark">Back to Dashboard</a>
            </div>
        </div>
    </div>
    <jsp:include page="chatbot.jsp" />
</body>
</html>
