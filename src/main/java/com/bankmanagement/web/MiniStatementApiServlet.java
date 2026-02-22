package com.bankmanagement.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/api/ministatement")
public class MiniStatementApiServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse res) {
        res.setContentType("application/json");
        String pin = req.getParameter("pin");
        StringBuilder json = new StringBuilder("{\"ok\":true,\"items\":[");
        boolean first = true;
        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/banksystem","root","131486");
             PreparedStatement ps = con.prepareStatement(
                     "SELECT date,type,amount FROM bank WHERE pin=? ORDER BY id DESC LIMIT 5")) {
            ps.setString(1, pin);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (!first) json.append(',');
                json.append("{\"date\":\"").append(rs.getString(1))
                        .append("\",\"type\":\"").append(rs.getString(2))
                        .append("\",\"amount\":").append(rs.getDouble(3)).append("}");
                first = false;
            }
            json.append("]}");
            PrintWriter out = res.getWriter(); out.print(json.toString());
        } catch (Exception e) {
            try { res.getWriter().print("{\"ok\":false,\"error\":\""+e.getMessage()+"\"}"); } catch (Exception ignored){}
        }
    }
}
