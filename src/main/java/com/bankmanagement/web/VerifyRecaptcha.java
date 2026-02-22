package com.bankmanagement.web;

import java.io.*;
import java.net.*;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class VerifyRecaptcha {

    // ⚠️ Replace this key with your own valid reCAPTCHA secret key
    public static final String SECRET_KEY = "6LcBcQgsAAAAALYtInEkOAWVaMFp-_RQcX_7Vzhc";
    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public static boolean verify(String gRecaptchaResponse) {
        if (gRecaptchaResponse == null || gRecaptchaResponse.isEmpty()) {
            return false;
        }

        try {
            // Create URL connection
            URL url = new URL(VERIFY_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            // Send POST parameters
            String postParams = "secret=" + URLEncoder.encode(SECRET_KEY, "UTF-8")
                    + "&response=" + URLEncoder.encode(gRecaptchaResponse, "UTF-8");

            try (OutputStream out = con.getOutputStream()) {
                out.write(postParams.getBytes());
            }

            // Read JSON response
            try (InputStream is = con.getInputStream();
                 JsonReader reader = Json.createReader(is)) {

                JsonObject json = reader.readObject();
                return json.getBoolean("success", false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
