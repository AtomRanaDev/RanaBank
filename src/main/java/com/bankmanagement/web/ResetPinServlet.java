package com.bankmanagement.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/ResetPinServlet")
public class ResetPinServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html");
        HttpSession session = req.getSession();
        String card = (String) session.getAttribute("card");
        int otp = (int) session.getAttribute("otp");
        int userOtp = Integer.parseInt(req.getParameter("otp"));
        String newPin = req.getParameter("newpin");

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/banksystem", "root", "131486")) {

            if (otp == userOtp) {
                PreparedStatement ps = con.prepareStatement("UPDATE signupthree SET pin=? WHERE card_number=?");
                ps.setString(1, newPin);
                ps.setString(2, card);
                ps.executeUpdate();

                res.getWriter().println("<script src='https://cdn.jsdelivr.net/npm/sweetalert2@11'></script>"
                        + "<script>Swal.fire('PIN Updated!','Your new PIN is set.','success').then(()=>{window.location='index.jsp'})</script>");
            } else {
                res.getWriter().println("<script src='https://cdn.jsdelivr.net/npm/sweetalert2@11'></script>"
                        + "<script>Swal.fire('Invalid OTP!','Please try again.','error').then(()=>{window.location='resetpin.jsp'})</script>");
            }

        } catch (Exception e) {
            res.getWriter().println("<p style='color:red'>Error: " + e.getMessage() + "</p>");
        }
    }
}
