package com.bankmanagement.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/WithdrawApiServlet")
public class WithdrawApiServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("cardNumber") == null) {
            response.getWriter().write("{\"ok\":false}");
            return;
        }

        String cardNumber = (String) session.getAttribute("cardNumber");
        double amount = Double.parseDouble(request.getParameter("amount"));

        double currentBalance = BankDAO.getBalanceByCard(cardNumber);

        if (amount > currentBalance) {
            response.getWriter().write(
                    "{\"ok\":false,\"error\":\"Insufficient Balance\"}"
            );
            return;
        }

        double newBalance = currentBalance - amount;

        BankDAO.updateBalance(cardNumber, newBalance);
        BankDAO.recordTransaction(cardNumber, "Withdraw", amount);

        session.setAttribute("balance", newBalance);

        response.getWriter().write(
                "{\"ok\":true,\"newBalance\":" + newBalance + "}"
        );
    }
}