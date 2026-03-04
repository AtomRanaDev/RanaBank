package com.bankmanagement.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.Random;
import java.util.Properties;

import jakarta.mail.*;
import jakarta.mail.internet.*;

@WebServlet("/ForgotPinServlet")
public class ForgotPinServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

        String card = req.getParameter("card_number");

        Random rand = new Random();
        int otp = 100000 + rand.nextInt(900000);

        try (Connection con = BankDAO.getConnection()) {

            PreparedStatement ps = con.prepareStatement(
                    "SELECT s.email FROM signup s JOIN signupthree st ON s.form_no = st.form_no WHERE st.card_number=?"
            );

            ps.setString(1, card);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String email = rs.getString("email");

                HttpSession session = req.getSession();
                session.setAttribute("otp", otp);
                session.setAttribute("card", card);

                System.out.println("OTP for reset: " + otp);

                res.sendRedirect("resetpin.jsp");

            } else {

                res.sendRedirect("forgotpin.jsp");

            }

        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("forgotpin.jsp");
        }
    }

    private void sendOTPEmail(String toEmail, int otp) throws Exception {

        String fromEmail = "atomranadev@gmail.com";
        String password = "tjjskybtfxxztdbn";

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, password);
                    }
                });

        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

        message.setSubject("RanaBank OTP Verification");
        message.setText("Your OTP for PIN reset is: " + otp);

        Transport.send(message);
    }
}