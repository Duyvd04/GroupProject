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

    private final HttpClient client = HttpClient.newHttpClient();

    public String getResponse(String prompt, String botType) {
        try {
            String jsonRequest;
            String apiUrl;
            if ("blenderbot".equalsIgnoreCase(botType)) {
                apiUrl = "https://api-inference.huggingface.co/models/facebook/blenderbot-1B-distill";
                jsonRequest = new JSONObject().put("inputs", prompt).toString();
            } else if ("qa-bot".equalsIgnoreCase(botType)) {
                apiUrl = "https://91ad-34-86-73-123.ngrok-free.app/predict";

                // Trích xuất context và question từ prompt
                String[] parts = prompt.split("Question:");
                if (parts.length < 2) {
                    return "Please provide a context and a question in the format: 'Context: <context> Question: <question>'";
                }
                String context = parts[0].replace("Context:", "").trim();
                String question = parts[1].trim();

                jsonRequest = new JSONObject()
                        .put("question", question)
                        .put("context", context)
                        .toString();
            } else {
                return "Invalid bot type. Use '@Chatbot1' or '@Chatbot2'.";
            }

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseResponse(response.body(), botType);
            } else {
                System.err.println("API Error: " + response.statusCode() + " - " + response.body());
                return "Sorry, I couldn't process your request.";
            }
        } catch (Exception e) {
            System.err.println("Error during API call: " + e.getMessage());
            return "Sorry, I couldn't process your request.";
        }
    }

    private String parseResponse(String response, String botType) {
        try {
            // Kiểm tra kiểu dữ liệu phản hồi
            if (response.startsWith("[")) {
                // Nếu là JSONArray, dùng cho Chatbot1 (blenderbot)
                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                return jsonObject.optString("generated_text", "Sorry, I couldn't understand the response.").trim();
            } else if (response.startsWith("{")) {
                // Nếu là JSONObject, dùng cho Chatbot2 (qa-bot)
                JSONObject jsonObject = new JSONObject(response);
                return jsonObject.optString("answer", "I don't know the answer to that.").trim();
            }
        } catch (Exception e) {
            System.err.println("Error parsing response: " + e.getMessage());
        }
        return "Sorry, I couldn't understand the response.";
    }
}
