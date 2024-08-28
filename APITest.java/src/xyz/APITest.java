package xyz;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class APITest {
    public static void main(String[] args) {
        try {
            // Specify the URL of the API
            URL url = new URL("https://bfhldevapigw.healthrx.co.in/automation-campus/create/user");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            // Set the request method to POST
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("roll-number", "1"); // Add custom header as per cURL
            conn.setDoOutput(true);

            // JSON input string
            String jsonInputString = "{\"firstName\":\"Test\",\"lastName\":\"Test\",\"phoneNumber\":9999999999,\"emailId\":\"test.test@test.com\"}";

            // Send the JSON input to the API
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Read the response from the API
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            try (Scanner scanner = new Scanner(conn.getInputStream(), "utf-8")) {
                String responseBody = scanner.useDelimiter("\\A").next();
                System.out.println("Response Body: " + responseBody);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
