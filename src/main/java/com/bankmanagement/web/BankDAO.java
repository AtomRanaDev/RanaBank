package com.bankmanagement.web;

import java.sql.*;

public class BankDAO {


    // Railway MySQL connection
    private static final String URL =
            "jdbc:mysql://crossover.proxy.rlwy.net:14457/railway?sslMode=REQUIRED&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = "VzsMjIDQpuHAhoSgXjyFbxgWqOyzkgNX";

    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
    }

    // Get Balance
    public static double getBalanceByCard(String cardNumber) {

        double balance = 0;

        String query =
                "SELECT balance FROM signupthree WHERE card_number=?";

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(query)){

            ps.setString(1, cardNumber);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                balance = rs.getDouble("balance");
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return balance;
    }

    // Update Balance
    public static boolean updateBalance(String cardNumber,double newBalance){

        String query =
                "UPDATE signupthree SET balance=? WHERE card_number=?";

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(query)){

            ps.setDouble(1,newBalance);
            ps.setString(2,cardNumber);

            return ps.executeUpdate()>0;

        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    // Record Transaction
    public static void recordTransaction(String cardNumber,String type,double amount){

        String query =
                "INSERT INTO bank(card_number,type,amount) VALUES(?,?,?)";

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(query)){

            ps.setString(1,cardNumber);
            ps.setString(2,type);
            ps.setDouble(3,amount);

            ps.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // Last Transaction
    public static String getLastTransaction(String cardNumber){

        String query =
                "SELECT type,amount,transaction_time FROM bank " +
                        "WHERE card_number=? ORDER BY transaction_time DESC LIMIT 1";

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(query)){

            ps.setString(1,cardNumber);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                return rs.getString("type").toUpperCase()
                        + " ₹" + rs.getDouble("amount")
                        + " on " + rs.getTimestamp("transaction_time");
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return "No transactions found.";
    }


}
