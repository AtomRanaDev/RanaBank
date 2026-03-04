package com.bankmanagement.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BankDAO {


    /* Railway automatically injects these environment variables */

    private static final String URL = System.getenv("MYSQL_URL");
    private static final String USER = System.getenv("MYSQLUSER");
    private static final String PASSWORD = System.getenv("MYSQLPASSWORD");

    public static Connection getConnection() throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /* Get Balance */

    public static double getBalanceByCard(String cardNumber) {

        double balance = 0;

        try (Connection con = getConnection()) {

            String query =
                    "SELECT balance FROM signupthree WHERE card_number=?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, cardNumber);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                balance = rs.getDouble("balance");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return balance;
    }

    /* Update Balance */

    public static boolean updateBalance(String cardNumber,double newBalance){

        try (Connection con = getConnection()) {

            String query =
                    "UPDATE signupthree SET balance=? WHERE card_number=?";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setDouble(1,newBalance);
            ps.setString(2,cardNumber);

            return ps.executeUpdate() > 0;

        } catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    /* Record Transaction */

    public static void recordTransaction(String cardNumber,
                                         String type,
                                         double amount){

        try (Connection con = getConnection()) {

            String query =
                    "INSERT INTO bank(card_number,type,amount) VALUES(?,?,?)";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1,cardNumber);
            ps.setString(2,type);
            ps.setDouble(3,amount);

            ps.executeUpdate();

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /* Last Transaction */

    public static String getLastTransaction(String cardNumber){

        try (Connection con = getConnection()) {

            String query =
                    "SELECT type,amount,transaction_time FROM bank " +
                            "WHERE card_number=? ORDER BY transaction_time DESC LIMIT 1";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1,cardNumber);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                return rs.getString("type").toUpperCase()
                        +" ₹"+rs.getDouble("amount")
                        +" on "+rs.getTimestamp("transaction_time");
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        return "No transactions found.";
    }


}
