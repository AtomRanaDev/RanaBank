package com.bankmanagement.web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@WebServlet("/WithdrawServlet")

public class WithdrawServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pin = request.getParameter("pin");
        double amount = Double.parseDouble(request.getParameter("amount"));
        double currentBalance = 0.0;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banksystem", "root", "131486");

            // Step 1: Check Current Balance
            PreparedStatement ps1 = con.prepareStatement("SELECT balance FROM signupthree WHERE pin = ?");
            ps1.setString(1, pin);
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                currentBalance = rs.getDouble("balance");
            }

            // Step 2: Validate and Withdraw
            if (amount <= 0) {
                out.println("<h3 style='color:red;text-align:center;'>Invalid amount entered!</h3>");
            } else if (amount > currentBalance) {
                out.println("<h3 style='color:red;text-align:center;'>Insufficient balance!</h3>");
            } else {
                PreparedStatement ps2 = con.prepareStatement("UPDATE signupthree SET balance = balance - ? WHERE pin = ?");
                ps2.setDouble(1, amount);
                ps2.setString(2, pin);
                ps2.executeUpdate();

                // Step 3: Log the transaction in the 'bank' table
                PreparedStatement ps3 = con.prepareStatement("INSERT INTO bank(pin, date, type, amount) VALUES(?,?,?,?)");
                String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                ps3.setString(1, pin);
                ps3.setString(2, date);
                ps3.setString(3, "Withdraw");
                ps3.setString(4, String.valueOf(amount));
                ps3.executeUpdate();

                // Redirect back to Dashboard
                response.sendRedirect("DashboardServlet?pin=" + pin);
            }

            con.close();
        } catch (Exception e) {
            out.println("<h3 style='color:red;text-align:center;'>Error: " + e.getMessage() + "</h3>");
        }
    }
}
