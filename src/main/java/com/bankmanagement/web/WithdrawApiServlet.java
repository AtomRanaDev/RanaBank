package com.bankmanagement.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/api/withdraw")
public class WithdrawApiServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        String pin = req.getParameter("pin");
        String amountStr = req.getParameter("amount");
        double amount;

        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            out.print("{\"ok\":false, \"error\":\"Invalid amount format\"}");
            return;
        }

        if (amount <= 0) {
            out.print("{\"ok\":false, \"error\":\"Withdrawal amount must be greater than 0\"}");
            return;
        }

        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to database
            try (Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/banksystem", "root", "131486")) {

                double currentBalance = 0.0;

                // 1️⃣ Get current balance
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT balance FROM signupthree WHERE pin = ?")) {
                    ps.setString(1, pin);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        currentBalance = rs.getDouble("balance");
                    } else {
                        out.print("{\"ok\":false, \"error\":\"Account not found\"}");
                        return;
                    }
                }

                // 2️⃣ Check sufficient balance
                if (amount > currentBalance) {
                    out.print("{\"ok\":false, \"error\":\"Insufficient balance\"}");
                    return;
                }

                // 3️⃣ Update balance
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE signupthree SET balance = balance - ? WHERE pin = ?")) {
                    ps.setDouble(1, amount);
                    ps.setString(2, pin);
                    ps.executeUpdate();
                }

                // 4️⃣ Log the transaction
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO bank(pin, date, type, amount) VALUES(?, NOW(), 'Withdraw', ?)")) {
                    ps.setString(1, pin);
                    ps.setDouble(2, amount);
                    ps.executeUpdate();
                }

                // 5️⃣ Get new balance
                double newBalance = currentBalance - amount;

                // ✅ Success response
                out.print("{\"ok\":true, \"newBalance\":" + newBalance + "}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"ok\":false, \"error\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }
}
