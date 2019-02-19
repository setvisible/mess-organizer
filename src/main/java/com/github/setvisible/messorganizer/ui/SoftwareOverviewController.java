package com.github.setvisible.messorganizer.ui;

import com.github.setvisible.messorganizer.MainApplication;
import com.github.setvisible.messorganizer.core.Software;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class SoftwareOverviewController {

	private MainApplication mainApp;

	@FXML
	private TableView<Software> softwareTable;
	@FXML
	private TableColumn<Software, String> softwareNameColumn;
	@FXML
	private TableColumn<Software, String> vendorNameColumn;
	@FXML
	private Button browseSourceDirectory;
	@FXML
	private Button browseTargetDirectory;
	@FXML
	private TextField targetDirectory;
	@FXML
	private TextField sourceDirectory;
	@FXML
	private TextField actionLabel;
	@FXML
	private Label sourceNameLabel;


	public SoftwareOverviewController() {
	}

	@FXML
	private void initialize() {
		// Initialize the software table with the two columns.
		softwareNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstSoftwareProperty());
		vendorNameColumn.setCellValueFactory(cellData -> cellData.getValue().vendorNameProperty());

		// Clear software details.
		showSoftwareDetails(null);

		// Listen for selection changes and show the software details when
		// changed.
		softwareTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showSoftwareDetails(newValue));
	}

	public void setMainApp(MainApplication mainApp) {
		this.mainApp = mainApp;
		softwareTable.setItems(mainApp.getSoftwareData());
	}

	/**
	 * Fills all text fields to show details about the software. If the
	 * specified software is null, all text fields are cleared.
	 *
	 * @param software
	 *            the software or null
	 */
	private void showSoftwareDetails(Software software) {
		if (software != null) {
			// Fill the labels with info from the software object.
			actionLabel.setText(software.getSoftwareName());
		} else {
			// Person is null, remove all the text.
			actionLabel.setText("");
		}
	}

	@FXML
	private void handleBrowseSource() {
	}

	@FXML
	private void handleBrowseTarget() {
	}
}
