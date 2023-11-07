package com.converter.Converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class CurrencyConverter {
    private static final String API_URL = "http://api.exchangerate.host/convert";

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String sourceCurrency;
        String targetCurrency;

        System.out.println("Welcome to the Currency Converter!");
        System.out.println("Enter the source currency code: ");
        sourceCurrency = reader.readLine();

        System.out.println("Enter the target currency code: ");
        targetCurrency = reader.readLine();

        double amount;
        try {
            System.out.println("Enter the amount to convert: ");
            amount = Double.parseDouble(reader.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered.");
            return;
        }

        double conversionRate = getConversionRate(sourceCurrency, targetCurrency, amount);
        if (conversionRate > 0) {
            double result = amount * conversionRate;
            System.out.println("Result: " + amount + " " + sourceCurrency + " = " + result + " " + targetCurrency);
        } else {
            System.out.println("Unable to fetch exchange rates for the provided currencies.");
        }
    }

    private static double getConversionRate(String sourceCurrency, String targetCurrency, double amount) {
        try {
            String apiKey = "9d2b4c13560f5085528d01c881b34cf0";  
            String apiUrl = API_URL + "?access_key=" + apiKey + "&from=" + sourceCurrency + "&to=" + targetCurrency + "&amount=" + amount + "&format=1";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonResponse = new JSONObject(response.toString());

                if (jsonResponse.has("result")) {
                    double conversionRate = jsonResponse.getDouble("result");
                    return conversionRate;
                } else {
                    System.out.println("Conversion rate not found in the response.");
                }
            } else {
                System.out.println("Failed to retrieve exchange rates. HTTP response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            System.out.println("Error parsing the JSON response: " + e.getMessage());
        }

        return -1; // Return a negative value to indicate an error
    }
}
