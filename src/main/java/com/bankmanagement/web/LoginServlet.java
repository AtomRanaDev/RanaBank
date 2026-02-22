package com.bankmanagement.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String acc = req.getParameter("account");
        String pin = req.getParameter("pin");

        // ✅ reCAPTCHA verification (optional fallback)
        String gRecaptchaResponse = req.getParameter("g-recaptcha-response");
        boolean verified = true;
        try {
            verified = VerifyRecaptcha.verify(gRecaptchaResponse);
        } catch (Exception e) {
            verified = true; // skip if offline
        }

        if (!verified) {
            req.setAttribute("error", "Please verify you're not a robot!");
            req.getRequestDispatcher("index.jsp").forward(req, res);
            return;
        }

        try (Connection con = BankDAO.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT s.name, st.form_no, st.card_number, st.balance " +
                            "FROM signup s JOIN signupthree st ON s.form_no = st.form_no " +
                            "WHERE st.card_number=? AND st.pin=?"
            );
            ps.setString(1, acc);
            ps.setString(2, pin);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String formNo = rs.getString("form_no");
                String cardNumber = rs.getString("card_number");
                double balance = rs.getDouble("balance");

                // ✅ Create session
                HttpSession session = req.getSession();
                session.setAttribute("username", name);
                session.setAttribute("formNo", formNo);
                session.setAttribute("cardNumber", cardNumber);
                session.setAttribute("balance", balance);
                session.setAttribute("pin", pin);

                // Redirect to dashboard
                res.sendRedirect("DashboardServlet");
            } else {
                req.setAttribute("error", "Invalid Account or PIN!");
                req.getRequestDispatcher("index.jsp").forward(req, res);
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.getWriter().println("<p style='color:red;'>Database Error: " + e.getMessage() + "</p>");
        }
    }
}
