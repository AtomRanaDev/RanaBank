package com.bankmanagement.web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.*;
@WebServlet("/MiniStatementServlet")

public class MiniStatementServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pin = request.getParameter("pin");

        List<String[]> transactions = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banksystem", "root", "131486");

            PreparedStatement ps = con.prepareStatement("SELECT date, type, amount FROM bank WHERE pin=? ORDER BY id DESC LIMIT 10");
            ps.setString(1, pin);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                transactions.add(new String[]{
                        rs.getString("date"),
                        rs.getString("type"),
                        rs.getString("amount")
                });
            }

            con.close();

            request.setAttribute("transactions", transactions);
            request.setAttribute("pin", pin);
            RequestDispatcher rd = request.getRequestDispatcher("ministatement.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<h3 style='color:red;text-align:center;'>Error fetching statement: " + e.getMessage() + "</h3>");
        }
    }
}
