package com.github.setvisible.messorganizer.ui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.github.setvisible.messorganizer.MainApplication;
import com.github.setvisible.messorganizer.settings.UserPreference;
import com.github.setvisible.messorganizer.ui.dialog.PreferenceDialog;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

/**
 * Main view of the application.
 */
public class MainWindowPresenter implements Initializable {

	private MainApplication mainApp;
	private UserPreference userPreference;

	@FXML
	private BodyPresenter bodyController;


	@Override
	public void initialize(final URL url, final ResourceBundle resourceBundle) {

	}

	// ****************************************************************************
	public void setMainApp(final MainApplication mainApp) {
		this.mainApp = mainApp;
		this.bodyController.setMainApp(mainApp);
	}

	public void setUserPreference(final UserPreference userPreference) {
		this.userPreference = userPreference;
		this.bodyController.setUserPreference(userPreference);
	}

	// ****************************************************************************
	@FXML
	private void handleNew() {
		mainApp.getSoftwareData().clear();
		mainApp.setSoftwareFilePath(null);
	}

	@FXML
	private void handleOpen() {
		final FileChooser fileChooser = new FileChooser();

		// Set extension filter
		final FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		final File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

		if (file != null) {
			mainApp.loadSoftwareDataFromFile(file);
		}
	}

	@FXML
	private void handleSave() {
		final File softwareFile = mainApp.getSoftwareFilePath();
		if (softwareFile != null) {
			mainApp.saveSoftwareDataToFile(softwareFile);
		} else {
			handleSaveAs();
		}
	}

	@FXML
	private void handleSaveAs() {
		final FileChooser fileChooser = new FileChooser();

		// Set extension filter
		final FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

		if (file != null) {
			// Make sure it has the correct extension
			if (!file.getPath().endsWith(".xml")) {
				file = new File(file.getPath() + ".xml");
			}
			mainApp.saveSoftwareDataToFile(file);
		}
	}

	@FXML
	private void handleAbout() {
		final Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Mess Organizer");
		alert.setHeaderText(ResourceBundle.getBundle("locale.en_US").getString("about"));
		final String content = ResourceBundle.getBundle("locale.en_US").getString("about.author") //
				+ ":\t Sebastien Vavassori" + "\n\n" //
				+ ResourceBundle.getBundle("locale.en_US").getString("about.website") //
				+ ":\t https://github.com/setvisible";
		alert.setContentText(content);
		alert.showAndWait();
	}

	@FXML
	private void handleExit() {
		mainApp.exit();
	}

	// ****************************************************************************
	@FXML
	private void handleShowStatistics() {
		mainApp.showStatistics();
	}

	@FXML
	public void showUserPreferences() {
		final Stage primaryStage = mainApp.getPrimaryStage();
		final PreferenceDialog dialog = new PreferenceDialog(primaryStage);
		dialog.setUserPreference(userPreference);
		dialog.show();
	}
}