package com.bankmanagement.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.sql.*;

@WebServlet("/ForgotPinServlet")
public class ForgotPinServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html");
        String card = req.getParameter("card_number");
        Random rand = new Random();
        int otp = 100000 + rand.nextInt(900000);

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/banksystem", "root", "131486")) {

            PreparedStatement ps = con.prepareStatement("SELECT * FROM signupthree WHERE card_number=?");
            ps.setString(1, card);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                HttpSession session = req.getSession();
                session.setAttribute("otp", otp);
                session.setAttribute("card", card);

                PrintWriter out = res.getWriter();
                out.println("<script src='https://cdn.jsdelivr.net/npm/sweetalert2@11'></script>");
                out.println("<script>");
                out.println("Swal.fire({title:'OTP Sent ✅', text:'Mock OTP: " + otp + "', icon:'info', confirmButtonText:'Continue'}).then(()=>{ window.location='resetpin.jsp'; });");
                out.println("</script>");
            } else {
                res.getWriter().println("<script src='https://cdn.jsdelivr.net/npm/sweetalert2@11'></script><script>Swal.fire('Invalid Card','Card number not found','error').then(()=>{window.location='forgotpin.jsp'})</script>");
            }

        } catch (Exception e) {
            res.getWriter().println("<p style='color:red'>Error: " + e.getMessage() + "</p>");
        }
    }
}
