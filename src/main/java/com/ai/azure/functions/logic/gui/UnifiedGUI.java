package com.ai.azure.functions.logic.gui;

import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class UnifiedGUI {

	public static void main(String[] args) {

		JFrame frame = new JFrame("AI Azure CRUD GUI");
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(2, 1));

		JButton listFunctionsBtn = new JButton("List Functions");
		JButton listLogicBtn = new JButton("List Logic Apps");

		JTextArea outputArea = new JTextArea();
		outputArea.setEditable(false);

		listFunctionsBtn.addActionListener(e -> {
			String response = sendGet("http://localhost:5000/functions/list");
			outputArea.setText(response);
		});

		listLogicBtn.addActionListener(e -> {
			String response = sendGet("http://localhost:5000/logic/list");
			outputArea.setText(response);
		});

		JPanel panel = new JPanel();
		panel.add(listFunctionsBtn);
		panel.add(listLogicBtn);

		frame.add(panel);
		frame.add(new JScrollPane(outputArea));
		frame.setVisible(true);
	}

	private static String sendGet(String urlStr) {
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuilder content = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine).append("\n");
			}
			in.close();
			return content.toString();
		} catch (Exception e) {
			return "Error: " + e.getMessage();
		}
	}
}
