package com.github.setvisible.messorganizer.ui;

import java.io.File;

import com.github.setvisible.messorganizer.MainApplication;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;


public class RootLayoutController {

    private MainApplication mainApp;

    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleNew() {
        mainApp.getSoftwareData().clear();
		mainApp.setSoftwareFilePath(null);
    }

    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
			mainApp.loadSoftwareDataFromFile(file);
        }
    }

    @FXML
    private void handleSave() {
		File softwareFile = mainApp.getSoftwareFilePath();
		if (softwareFile != null) {
			mainApp.saveSoftwareDataToFile(softwareFile);
        } else {
            handleSaveAs();
        }
    }

    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
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
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Mess Organizer");
        alert.setHeaderText("About");
        alert.setContentText("Author: Sebastien Vavassori\nWebsite: https://github.com/setvisible");

        alert.showAndWait();
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
    
    @FXML
    private void handleShowStatistics() {
      mainApp.showStatistics();
    }
}