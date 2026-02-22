package com.bankmanagement.web;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@WebServlet("/ChatServlet")
public class ChatServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setContentType("application/json;charset=UTF-8");

        String userMsg = req.getParameter("message");

        if (userMsg == null || userMsg.isBlank()) {
            res.getWriter().write("{\"reply\":\"Please type something first.\"}");
            return;
        }

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("cardNumber") == null) {
            res.getWriter().write("{\"reply\":\"Please login to use banking features.\"}");
            return;
        }

        String cardNumber = (String) session.getAttribute("cardNumber");
        String lowerMsg = userMsg.toLowerCase();

        // ✅ BALANCE
        if (lowerMsg.contains("balance")) {

            double balance = BankDAO.getBalanceByCard(cardNumber);

            res.getWriter().write("{\"reply\":\"Your current balance is ₹"
                    + balance + "\"}");
            return;
        }

        // ✅ DEPOSIT
        if (lowerMsg.startsWith("deposit")) {

            double amount = extractAmount(lowerMsg);

            if (amount <= 0) {
                res.getWriter().write("{\"reply\":\"Use format: deposit 1000\"}");
                return;
            }

            double currentBalance = BankDAO.getBalanceByCard(cardNumber);
            double newBalance = currentBalance + amount;

            BankDAO.updateBalance(cardNumber, newBalance);
            BankDAO.recordTransaction(cardNumber, "deposit", amount);

            res.getWriter().write("{\"reply\":\"₹" + amount +
                    " deposited successfully. New balance is ₹" + newBalance + "\"}");
            return;
        }

        // ✅ WITHDRAW
        if (lowerMsg.startsWith("withdraw")) {

            double amount = extractAmount(lowerMsg);

            if (amount <= 0) {
                res.getWriter().write("{\"reply\":\"Use format: withdraw 500\"}");
                return;
            }

            double currentBalance = BankDAO.getBalanceByCard(cardNumber);

            if (amount > currentBalance) {
                res.getWriter().write("{\"reply\":\"Insufficient balance.\"}");
                return;
            }

            double newBalance = currentBalance - amount;

            BankDAO.updateBalance(cardNumber, newBalance);
            BankDAO.recordTransaction(cardNumber, "withdraw", amount);

            res.getWriter().write("{\"reply\":\"₹" + amount +
                    " withdrawn successfully. Remaining balance is ₹" + newBalance + "\"}");
            return;
        }

        // ✅ LAST TRANSACTION
        if (lowerMsg.contains("last transaction")) {

            String lastTxn = BankDAO.getLastTransaction(cardNumber);

            res.getWriter().write("{\"reply\":\"Your last transaction was: "
                    + escapeJson(lastTxn) + "\"}");
            return;
        }

        // 🔥 AI Fallback
        try {

            String systemPrompt = "You are RanaBank's professional banking assistant. "
                    + "Answer clearly and politely about banking services only.\nUser: ";

            String fullPrompt = systemPrompt + userMsg;

            URL url = new URL("http://127.0.0.1:11434/api/generate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput = "{"
                    + "\"model\":\"llama3\","
                    + "\"prompt\":\"" + escapeJson(fullPrompt) + "\","
                    + "\"stream\":false"
                    + "}";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));

            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                responseBuilder.append(line);
            }

            br.close();

            String reply = extractOllamaResponse(responseBuilder.toString());

            res.getWriter().write("{\"reply\":\"" + escapeJson(reply) + "\"}");

        } catch (Exception e) {
            res.getWriter().write("{\"reply\":\"AI service unavailable.\"}");
        }
    }

    private double extractAmount(String text) {
        String numbers = text.replaceAll("[^0-9.]", "");
        if (numbers.isEmpty()) return 0;
        return Double.parseDouble(numbers);
    }

    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "");
    }

    private String extractOllamaResponse(String json) {

        int index = json.indexOf("\"response\":\"");

        if (index != -1) {
            int start = index + 12;
            int end = json.indexOf("\",", start);

            if (end > start) {
                return json.substring(start, end)
                        .replace("\\n", "<br>")
                        .replace("\\\"", "\"");
            }
        }

        return "AI could not generate a response.";
    }
}