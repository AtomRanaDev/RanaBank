package com.bankmanagement.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/ResetPinServlet")
public class ResetPinServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        res.setContentType("text/html");

        String enteredOtp = req.getParameter("otp");
        String newPin = req.getParameter("newpin");

        HttpSession session = req.getSession();

        Integer sessionOtp = (Integer) session.getAttribute("otp");
        String card = (String) session.getAttribute("card");

        PrintWriter out = res.getWriter();

        if (sessionOtp == null || card == null) {

            out.println("<script>alert('Session expired');window.location='forgotpin.jsp'</script>");
            return;

        }

        if (!enteredOtp.equals(String.valueOf(sessionOtp))) {

            out.println("<script>alert('Invalid OTP');window.location='resetpin.jsp'</script>");
            return;

        }

        try (Connection con = BankDAO.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE signupthree SET pin=? WHERE card_number=?"
            );

            ps.setString(1, newPin);
            ps.setString(2, card);

            ps.executeUpdate();

            session.removeAttribute("otp");
            session.removeAttribute("card");

            out.println("<script>");
            out.println("alert('PIN updated successfully');");
            out.println("window.location='index.jsp';");
            out.println("</script>");

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<script>alert('Error updating PIN');window.location='forgotpin.jsp'</script>");

        }
    }
}