package com.bankmanagement.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.Random;

@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html;charset=UTF-8");

        try (Connection con = BankDAO.getConnection()) {

            // ✅ Collect user input
            String formNo = String.valueOf(System.currentTimeMillis());
            String name = req.getParameter("name");
            String father = req.getParameter("father_name");
            String dob = req.getParameter("dob");
            String gender = req.getParameter("gender");
            String email = req.getParameter("email");
            String address = req.getParameter("address");
            String city = req.getParameter("city");
            String pincode = req.getParameter("pincode");
            String state = req.getParameter("state");
            String religion = req.getParameter("religion");
            String category = req.getParameter("category");
            String education = req.getParameter("education");

            // ✅ Generate unique card number and PIN
            Random rand = new Random();
            long cardNumber = 1000000000000000L + (long) (rand.nextDouble() * 9000000000000000L);
            int pin = 1000 + rand.nextInt(9000);

            // ✅ Step 1: Insert into signup
            String query1 = "INSERT INTO signup (form_no, name, father_name, DOB, gender, email, marital_status, address, city, pincode, state) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(query1)) {
                ps.setString(1, formNo);
                ps.setString(2, name);
                ps.setString(3, father);
                ps.setString(4, dob);
                ps.setString(5, gender);
                ps.setString(6, email);
                ps.setString(7, "Single");
                ps.setString(8, address);
                ps.setString(9, city);
                ps.setString(10, pincode);
                ps.setString(11, state);
                ps.executeUpdate();
            }

            // ✅ Step 2: Insert into signuptwo
            String query2 = "INSERT INTO signuptwo (form_no, religion, category, income, education, occupation, pan, aadhar, seniorcitizen, existing_account) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(query2)) {
                ps.setString(1, formNo);
                ps.setString(2, religion);
                ps.setString(3, category);
                ps.setString(4, "0");
                ps.setString(5, education);
                ps.setString(6, "Student");
                ps.setString(7, "NA");
                ps.setString(8, "NA");
                ps.setString(9, "No");
                ps.setString(10, "No");
                ps.executeUpdate();
            }

            // ✅ Step 3: Insert into signupthree
            String query3 = "INSERT INTO signupthree (form_no, account_type, card_number, pin, facility, balance) VALUES (?, ?, ?, ?, ?, 0)";
            try (PreparedStatement ps = con.prepareStatement(query3)) {
                ps.setString(1, formNo);
                ps.setString(2, "Savings");
                ps.setString(3, String.valueOf(cardNumber));
                ps.setString(4, String.valueOf(pin));
                ps.setString(5, "ATM, Internet Banking");
                ps.executeUpdate();
            }

            // ✅ Success message
            res.getWriter().println("<html><body style='font-family:Poppins;text-align:center;'>"
                    + "<h2 style='color:green;'>Account Created Successfully ✅</h2>"
                    + "<p><b>Card Number:</b> " + cardNumber + "</p>"
                    + "<p><b>PIN:</b> " + pin + "</p>"
                    + "<a href='index.jsp'>Go to Login</a>"
                    + "</body></html>");

        } catch (Exception e) {
            res.getWriter().println("<p style='color:red;text-align:center;'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
    }
}
