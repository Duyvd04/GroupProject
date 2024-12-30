package com.example.chatapp.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ChatbotService {

    @Value("${huggingface.api.key}")
    private String apiKey;

    public String getResponse(String prompt) {
        try {
            String jsonRequest = "{\"inputs\": \"" + prompt + "\"}";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api-inference.huggingface.co/models/facebook/blenderbot-400M-distill"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Raw response from API: " + response.body()); // Debugging log

            if (response.statusCode() == 200) {
                return parseResponse(response.body());
            } else {
                System.err.println("API Error: " + response.statusCode() + " - " + response.body());
                return "Sorry, I couldn't process your request.";
            }
        } catch (Exception e) {
            System.err.println("Error during API call: " + e.getMessage());
            return "Sorry, I couldn't process your request.";
        }
    }

    private String parseResponse(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            return jsonObject.optString("generated_text", "Sorry, I couldn't understand the response.").trim();
        } catch (Exception e) {
            System.err.println("Error parsing response: " + e.getMessage());
            return "Sorry, I couldn't understand the response.";
        }
    }
}
