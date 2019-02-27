package com.github.setvisible.messorganizer.ui.dialog;

import java.io.File;
import java.util.prefs.Preferences;

import com.github.setvisible.messorganizer.settings.UserPreference;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * The controller of PreferenceDialog.
 */
public class PreferenceDialogController {

	@FXML
	private TabPane tabPane;
	@FXML
	private Button browseSourceDirectory;
	@FXML
	private Button browseTargetDirectory;
	@FXML
	private TextField targetDirectory;
	@FXML
	private TextField sourceDirectory;
	@FXML
	private Button buttonRestoreDefaults;

	private Stage stage;

	@FXML
	public void initialize() {
		this.readSettings();
	}

	public void shutdown() {
		this.writeSettings();
	}

	public void setPrimaryStage(final Stage stage) {
		this.stage = stage;
	}

	public void setUserPreference(final UserPreference userPreference) {
		/* Restore Defaults */
		buttonRestoreDefaults.setOnAction(e -> {
			userPreference.clearUserPreference();
			updateWidgets(userPreference);
		});

		updateWidgets(userPreference);
	}

	public void updateWidgets(final UserPreference userPreference) {
		/* Tab General */
		setupDirectories(userPreference);
	}

	private void setupDirectories(final UserPreference userPreference) {
		sourceDirectory.setText(userPreference.getSourceDirectory());
		targetDirectory.setText(userPreference.getTargetDirectory());

		sourceDirectory.textProperty().addListener(e -> {
			final String directory = sourceDirectory.getText();
			userPreference.setSourceDirectory(directory);
		});
		targetDirectory.textProperty().addListener(e -> {
			final String directory = targetDirectory.getText();
			userPreference.setTargetDirectory(directory);
		});
	}

	@FXML
	private void handleBrowseSource() {
		handleBrowse(sourceDirectory);
	}

	@FXML
	private void handleBrowseTarget() {
		handleBrowse(targetDirectory);
	}

	private void handleBrowse(final TextField textfield) {
		assert stage != null;

		final String dir = textfield.getText();
		final File previous = new File(dir);
		final DirectoryChooser directoryChooser = new DirectoryChooser();
		if (previous.exists()) {
			directoryChooser.setInitialDirectory(previous);
		} else {
			// File userDirectory = FileUtils.getUserDirectory();
			final String path = System.getProperty("user.home");
			final File customDir = new File(path);
			directoryChooser.setInitialDirectory(customDir);
		}
		directoryChooser.setTitle("Select Directory");

		final File selectedDirectory = directoryChooser.showDialog(stage);
		if (selectedDirectory != null) {
			textfield.setText(selectedDirectory.getAbsolutePath());
		}
	}

	// ************************************************************************
	// Dialog Settings
	// ************************************************************************
	private void readSettings() {
		final Preferences prefs = Preferences.userNodeForPackage(this.getClass());
		final int index = prefs.getInt("index", 0);
		tabPane.getSelectionModel().select(index);
	}

	private void writeSettings() {
		final Preferences prefs = Preferences.userNodeForPackage(this.getClass());
		final int index = tabPane.getSelectionModel().getSelectedIndex();
		prefs.putInt("index", index);
	}
}
