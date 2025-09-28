package com.ai.azure.functions.logic.repository;

import java.util.ArrayList;
import java.util.List;

public class AzureFunctionsRepository {

	public static List<String> getAzureFunctions() {
		List<String> functions = new ArrayList<>();
		functions.add("FunctionApp-WeatherAPI");
		functions.add("FunctionApp-FileProcessor");
		functions.add("FunctionApp-EmailSender");
		return functions;
	}
}
