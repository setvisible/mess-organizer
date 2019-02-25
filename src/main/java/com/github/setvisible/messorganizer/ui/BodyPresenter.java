package com.github.setvisible.messorganizer.ui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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

public class BodyPresenter implements UserPreferenceListener, Initializable {

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
		softwareNameColumn.setCellValueFactory(cellData -> cellData.getValue().fileNameProperty());
		vendorNameColumn.setCellValueFactory(cellData -> cellData.getValue().fullFileNameProperty());

		// Clear software details.
		showSoftwareDetails(null);

		// Listen for selection changes and show the software details when
		// changed.
		softwareTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showSoftwareDetails(newValue));

	}

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
			actionLabel.setText(software.getFileName());
		} else {
			// Person is null, remove all the text.
			actionLabel.setText("");
		}
	}

	// ************************************************************************
	// Actions
	// ************************************************************************
	private Consumer<?> callbackShowUserPreferences;

	public void setOnShowUserPreferences(Consumer<?> callbackShowUserPreferences) {
		this.callbackShowUserPreferences = callbackShowUserPreferences;
	}

	@FXML
	public void showUserPreferences() {
		callbackShowUserPreferences.accept(null);
	}

	// ************************************************************************
	// Model Listeners
	// ************************************************************************
	@Override
	public void onDataChanged() {
		softwareTable.getItems().clear();
		softwareTable.setItems(this.model.getSoftwareData());
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