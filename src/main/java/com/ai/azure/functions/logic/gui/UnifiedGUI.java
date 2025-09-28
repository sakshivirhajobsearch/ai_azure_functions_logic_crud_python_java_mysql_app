package com.ai.azure.functions.logic.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.ai.azure.functions.logic.repository.AzureFunctionsRepo;
import com.ai.azure.functions.logic.repository.LogicAppsRepo;

public class UnifiedGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTable functionTable;
	private JTable logicAppTable;
	private DefaultTableModel functionModel;
	private DefaultTableModel logicAppModel;

	private JButton addFunctionBtn, updateFunctionBtn, deleteFunctionBtn;
	private JButton addLogicBtn, updateLogicBtn, deleteLogicBtn;
	private JButton refreshBtn;

	private AzureFunctionsRepo functionRepo = new AzureFunctionsRepo();
	private LogicAppsRepo logicRepo = new LogicAppsRepo();

	public UnifiedGUI() {
		setTitle("AI + Azure Functions & Logic Apps CRUD");
		setSize(1000, 700);
		setLayout(new BorderLayout());

		// Split panel for tables
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(300);

		// Functions table
		functionModel = new DefaultTableModel(new String[] { "ID", "Name", "URL" }, 0);
		functionTable = new JTable(functionModel);
		splitPane.setTopComponent(new JScrollPane(functionTable));

		// Logic Apps table
		logicAppModel = new DefaultTableModel(new String[] { "ID", "Name", "URL" }, 0);
		logicAppTable = new JTable(logicAppModel);
		splitPane.setBottomComponent(new JScrollPane(logicAppTable));

		add(splitPane, BorderLayout.CENTER);

		// Buttons panel
		JPanel buttonPanel = new JPanel(new GridLayout(2, 1));

		// Function buttons
		JPanel funcButtons = new JPanel();
		addFunctionBtn = new JButton("Add Function");
		updateFunctionBtn = new JButton("Update Function");
		deleteFunctionBtn = new JButton("Delete Function");
		funcButtons.add(addFunctionBtn);
		funcButtons.add(updateFunctionBtn);
		funcButtons.add(deleteFunctionBtn);

		// Logic Apps buttons
		JPanel logicButtons = new JPanel();
		addLogicBtn = new JButton("Add Logic App");
		updateLogicBtn = new JButton("Update Logic App");
		deleteLogicBtn = new JButton("Delete Logic App");
		logicButtons.add(addLogicBtn);
		logicButtons.add(updateLogicBtn);
		logicButtons.add(deleteLogicBtn);

		buttonPanel.add(funcButtons);
		buttonPanel.add(logicButtons);
		add(buttonPanel, BorderLayout.SOUTH);

		// Refresh button at top
		refreshBtn = new JButton("Refresh Data");
		add(refreshBtn, BorderLayout.NORTH);

		// Load data initially
		loadData();

		// Button actions
		refreshBtn.addActionListener(e -> loadData());

		addFunctionBtn.addActionListener(e -> addFunction());
		updateFunctionBtn.addActionListener(e -> updateFunction());
		deleteFunctionBtn.addActionListener(e -> deleteFunction());

		addLogicBtn.addActionListener(e -> addLogicApp());
		updateLogicBtn.addActionListener(e -> updateLogicApp());
		deleteLogicBtn.addActionListener(e -> deleteLogicApp());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void loadData() {
		try {
			// Functions
			functionModel.setRowCount(0);
			List<Map<String, String>> functions = functionRepo.getAllFunctions();
			for (Map<String, String> f : functions) {
				functionModel.addRow(new Object[] { f.get("id"), f.get("name"), f.get("url") });
			}

			// Logic Apps
			logicAppModel.setRowCount(0);
			List<Map<String, String>> apps = logicRepo.getAllLogicApps();
			for (Map<String, String> a : apps) {
				logicAppModel.addRow(new Object[] { a.get("id"), a.get("name"), a.get("url") });
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	// ---------------- Function CRUD ----------------

	private void addFunction() {
		String name = JOptionPane.showInputDialog("Enter Function Name:");
		String url = JOptionPane.showInputDialog("Enter Function URL:");
		if (name != null && url != null) {
			try {
				functionRepo.addFunction(name, url);
				loadData();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Error adding function: " + e.getMessage());
			}
		}
	}

	private void updateFunction() {
		int row = functionTable.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Select a function to update.");
			return;
		}
		int id = Integer.parseInt(functionModel.getValueAt(row, 0).toString());
		String currentName = functionModel.getValueAt(row, 1).toString();
		String currentUrl = functionModel.getValueAt(row, 2).toString();

		String newName = JOptionPane.showInputDialog("Enter new Function Name:", currentName);
		String newUrl = JOptionPane.showInputDialog("Enter new Function URL:", currentUrl);
		if (newName != null && newUrl != null) {
			try {
				functionRepo.updateFunction(id, newName, newUrl);
				loadData();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Error updating function: " + e.getMessage());
			}
		}
	}

	private void deleteFunction() {
		int row = functionTable.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Select a function to delete.");
			return;
		}
		int id = Integer.parseInt(functionModel.getValueAt(row, 0).toString());
		int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this function?");
		if (confirm == JOptionPane.YES_OPTION) {
			try {
				functionRepo.deleteFunction(id);
				loadData();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Error deleting function: " + e.getMessage());
			}
		}
	}

	// ---------------- Logic Apps CRUD ----------------

	private void addLogicApp() {
		String name = JOptionPane.showInputDialog("Enter Logic App Name:");
		String url = JOptionPane.showInputDialog("Enter Logic App URL:");
		if (name != null && url != null) {
			try {
				logicRepo.addLogicApp(name, url);
				loadData();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Error adding logic app: " + e.getMessage());
			}
		}
	}

	private void updateLogicApp() {
		int row = logicAppTable.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Select a logic app to update.");
			return;
		}
		int id = Integer.parseInt(logicAppModel.getValueAt(row, 0).toString());
		String currentName = logicAppModel.getValueAt(row, 1).toString();
		String currentUrl = logicAppModel.getValueAt(row, 2).toString();

		String newName = JOptionPane.showInputDialog("Enter new Logic App Name:", currentName);
		String newUrl = JOptionPane.showInputDialog("Enter new Logic App URL:", currentUrl);
		if (newName != null && newUrl != null) {
			try {
				logicRepo.updateLogicApp(id, newName, newUrl);
				loadData();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Error updating logic app: " + e.getMessage());
			}
		}
	}

	private void deleteLogicApp() {
		int row = logicAppTable.getSelectedRow();
		if (row == -1) {
			JOptionPane.showMessageDialog(this, "Select a logic app to delete.");
			return;
		}
		int id = Integer.parseInt(logicAppModel.getValueAt(row, 0).toString());
		int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this logic app?");
		if (confirm == JOptionPane.YES_OPTION) {
			try {
				logicRepo.deleteLogicApp(id);
				loadData();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Error deleting logic app: " + e.getMessage());
			}
		}
	}

	// ---------------- Main ----------------

	public static void main(String[] args) {
		SwingUtilities.invokeLater(UnifiedGUI::new);
	}
}
