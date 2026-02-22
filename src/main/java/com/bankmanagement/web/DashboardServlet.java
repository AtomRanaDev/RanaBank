package com.bankmanagement.web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("cardNumber") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String cardNumber = (String) session.getAttribute("cardNumber");

        // ✅ ALWAYS FETCH LIVE BALANCE FROM DB
        double freshBalance = BankDAO.getBalanceByCard(cardNumber);

        // Update session (keeps it synced)
        session.setAttribute("balance", freshBalance);

        // Send fresh data to JSP
        request.setAttribute("name", session.getAttribute("username"));
        request.setAttribute("cardNumber", cardNumber);
        request.setAttribute("balance", freshBalance);

        RequestDispatcher rd = request.getRequestDispatcher("dashboard.jsp");
        rd.forward(request, response);
    }
}