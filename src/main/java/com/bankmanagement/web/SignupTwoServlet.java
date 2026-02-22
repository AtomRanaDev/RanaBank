package com.bankmanagement.web;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;

public class SignupTwoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String religion = request.getParameter("religion");
        String category = request.getParameter("category");
        String income = request.getParameter("income");
        String education = request.getParameter("education");
        String occupation = request.getParameter("occupation");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banksystem", "root", "131486");
            PreparedStatement ps = con.prepareStatement("INSERT INTO signuptwo (religion, category, income, education, occupation) VALUES (?,?,?,?,?)");
            ps.setString(1, religion);
            ps.setString(2, category);
            ps.setString(3, income);
            ps.setString(4, education);
            ps.setString(5, occupation);

            ps.executeUpdate();
            con.close();

            response.sendRedirect("signupThree.jsp");
        } catch (Exception e) {
            response.getWriter().println("Database Error: " + e.getMessage());
        }
    }
}
