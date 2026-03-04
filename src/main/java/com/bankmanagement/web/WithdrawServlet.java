package com.bankmanagement.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/WithdrawServlet")
public class WithdrawServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("cardNumber") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String cardNumber = (String) session.getAttribute("cardNumber");
        String pin = (String) session.getAttribute("pin");
        double amount = Double.parseDouble(request.getParameter("amount"));

        try (Connection con = BankDAO.getConnection()) {

            // Check balance
            PreparedStatement check = con.prepareStatement(
                    "SELECT balance FROM signupthree WHERE card_number = ?");
            check.setString(1, cardNumber);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");

                if (currentBalance < amount) {
                    response.getWriter().println("Insufficient Balance");
                    return;
                }
            }

            // Deduct balance
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE signupthree SET balance = balance - ? WHERE card_number = ?");
            ps.setDouble(1, amount);
            ps.setString(2, cardNumber);
            ps.executeUpdate();

            // Insert transaction
            PreparedStatement ps2 = con.prepareStatement(
                    "INSERT INTO bank(pin, date, type, amount) VALUES(?,?,?,?)");

            ps2.setString(1, pin);
            ps2.setString(2, java.time.LocalDateTime.now().toString());
            ps2.setString(3, "Withdraw");
            ps2.setString(4, String.valueOf(amount));
            ps2.executeUpdate();

            response.sendRedirect("DashboardServlet");

        } catch (Exception e) {
            response.getWriter().println("Withdraw Error: " + e.getMessage());
        }
    }
}