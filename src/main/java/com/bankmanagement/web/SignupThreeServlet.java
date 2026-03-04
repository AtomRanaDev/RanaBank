package com.bankmanagement.web;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class SignupThreeServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String accountType = request.getParameter("accountType");
        String facility = request.getParameter("facility");

        long card = (long)(Math.random() * 9000000000000000L) + 1000000000000000L;
        String cardNumber = String.valueOf(card);

        int pinNum = (int)(Math.random() * 9000) + 1000;
        String pin = String.valueOf(pinNum);

        try {

            Connection con = BankDAO.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO signupthree(account_Type,card_number,pin,facility,balance) VALUES(?,?,?,?,?)");

            ps.setString(1, accountType);
            ps.setString(2, cardNumber);
            ps.setString(3, pin);
            ps.setString(4, facility);
            ps.setDouble(5, 0);

            ps.executeUpdate();

            con.close();

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            out.println("<h2 style='color:green;'>Account Created Successfully</h2>");
            out.println("<p><b>Card Number:</b> " + cardNumber + "</p>");
            out.println("<p><b>PIN:</b> " + pin + "</p>");
            out.println("<a href='login.jsp'>Go to Login</a>");

        } catch (Exception e) {

            e.printStackTrace();
            response.getWriter().println("Database Error: " + e.getMessage());

        }
    }
}