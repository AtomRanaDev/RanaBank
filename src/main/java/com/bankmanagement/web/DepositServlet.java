package com.bankmanagement.web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/DepositServlet") // ✅ This line is the key fix
public class DepositServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pin = request.getParameter("pin");
        double amount = Double.parseDouble(request.getParameter("amount"));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banksystem", "root", "131486");

            PreparedStatement ps = con.prepareStatement("UPDATE signupthree SET balance = balance + ? WHERE pin = ?");
            ps.setDouble(1, amount);
            ps.setString(2, pin);
            ps.executeUpdate();

            PreparedStatement ps2 = con.prepareStatement("INSERT INTO bank(pin, date, type, amount) VALUES(?,?,?,?)");
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            ps2.setString(1, pin);
            ps2.setString(2, date);
            ps2.setString(3, "Deposit");
            ps2.setString(4, String.valueOf(amount));
            ps2.executeUpdate();

            response.sendRedirect("DashboardServlet?pin=" + pin);
            con.close();

        } catch (Exception e) {
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}
