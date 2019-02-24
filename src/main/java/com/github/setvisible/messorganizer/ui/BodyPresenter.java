package com.github.setvisible.messorganizer.ui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.github.setvisible.messorganizer.MainApplication;
import com.github.setvisible.messorganizer.core.Software;
import com.github.setvisible.messorganizer.settings.UserPreference;
import com.github.setvisible.messorganizer.settings.UserPreferenceListener;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class BodyPresenter implements UserPreferenceListener, Initializable {

	private MainApplication mainApp;
	private UserPreference userPreference;

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

	@Override
	public void initialize(final URL url, final ResourceBundle resourceBundle) {

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

	public void setMainApp(final MainApplication mainApp) {
		this.mainApp = mainApp;
		softwareTable.setItems(mainApp.getSoftwareData());
	}

	/**
	 * Fills all text fields to show details about the software. If the specified
	 * software is null, all text fields are cleared.
	 *
	 * @param software the software or null
	 */
	private void showSoftwareDetails(final Software software) {
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
		handleBrowse(sourceDirectory);
	}

	@FXML
	private void handleBrowseTarget() {
		handleBrowse(targetDirectory);
	}

	private void handleBrowse(TextField textfield) {
		Stage ownerWindow = mainApp.getPrimaryStage();

		String dir = textfield.getText();
		File previous = new File(dir);
		final DirectoryChooser directoryChooser = new DirectoryChooser();
		if (previous.exists()) {
			directoryChooser.setInitialDirectory(previous);
		} else {
			String path = System.getProperty("user.home");
			File customDir = new File(path);
			directoryChooser.setInitialDirectory(customDir);
		}
		directoryChooser.setTitle("Select Directory");

		final File selectedDirectory = directoryChooser.showDialog(ownerWindow);
		if (selectedDirectory != null) {
			textfield.setText(selectedDirectory.getAbsolutePath());
		}
	}

	// ************************************************************************
	// User Preference
	// ************************************************************************
	public void setUserPreference(final UserPreference userPreference) {
		if (this.userPreference != null) {
			this.userPreference.removeListener(this);
		}
		this.userPreference = userPreference;
		if (this.userPreference != null) {
			this.userPreference.addListener(this);

			onSourceDirectoryChanged();
			onTargetDirectoryChanged();
		}
	}

	@Override
	public void onSourceDirectoryChanged() {
		assert userPreference != null;
		final String directory = this.userPreference.getSourceDirectory();
		sourceDirectory.setText(directory);
	}

	@Override
	public void onTargetDirectoryChanged() {
		assert userPreference != null;
		final String directory = this.userPreference.getTargetDirectory();
		targetDirectory.setText(directory);
	}
}