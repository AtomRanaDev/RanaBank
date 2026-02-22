package com.bankmanagement.web;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class SignupThreeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String accountType = request.getParameter("accountType");
        String cardNumber = request.getParameter("cardNumber");
        String pin = request.getParameter("pin");
        String facility = request.getParameter("facility");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banksystem", "root", "131486");
            PreparedStatement ps = con.prepareStatement("INSERT INTO signupthree (account_Type, card_number, pin, facility) VALUES (?,?,?,?)");
            ps.setString(1, accountType);
            ps.setString(2, cardNumber);
            ps.setString(3, pin);
            ps.setString(4, facility);

            ps.executeUpdate();
            con.close();

            response.sendRedirect("login.jsp");
        } catch (Exception e) {
            response.getWriter().println("Database Error: " + e.getMessage());
        }
    }
}
