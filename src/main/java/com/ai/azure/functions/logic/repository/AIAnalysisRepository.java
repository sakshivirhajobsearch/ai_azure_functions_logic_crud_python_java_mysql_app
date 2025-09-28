package com.ai.azure.functions.logic.repository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AIAnalysisRepository {

	public static String analyzeTextWithAI(String text) {
		try {
			URL url = new URL("http://localhost:5001/api/ai/analyze");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; utf-8");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);

			String jsonInputString = "{\"text\": \"" + text.replace("\"", "\\\"") + "\"}";

			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = jsonInputString.getBytes("utf-8");
				os.write(input, 0, input.length);
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			StringBuilder response = new StringBuilder();
			String responseLine;

			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}

			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"error\": \"Failed to call AI API\"}";
		}
	}
}
