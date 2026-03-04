package com.bankmanagement.web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/MiniStatementServlet")
public class MiniStatementServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("pin") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String cardNumber = (String) session.getAttribute("cardNumber");

        List<String[]> transactions = new ArrayList<>();

        try (Connection con = BankDAO.getConnection()) {

            String query = "SELECT transaction_time, type, amount FROM bank WHERE card_number = ? ORDER BY transaction_time DESC LIMIT 10";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, cardNumber);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                transactions.add(new String[]{
                        rs.getTimestamp("transaction_time").toString(),
                        rs.getString("type"),
                        rs.getString("amount")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("transactions", transactions);
        RequestDispatcher rd = request.getRequestDispatcher("ministatement.jsp");
        rd.forward(request, response);
    }
}