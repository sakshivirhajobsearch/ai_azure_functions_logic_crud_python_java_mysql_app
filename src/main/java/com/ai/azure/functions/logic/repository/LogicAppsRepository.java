package com.ai.azure.functions.logic.repository;

import java.util.ArrayList;
import java.util.List;

public class LogicAppsRepository {

	public static List<String> getLogicApps() {
		List<String> logicApps = new ArrayList<>();
		logicApps.add("LogicApp-AlertManager");
		logicApps.add("LogicApp-DatabaseBackup");
		logicApps.add("LogicApp-TicketNotifier");
		return logicApps;
	}
}
