package com.github.setvisible.messorganizer.ui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.github.setvisible.messorganizer.core.Model;
import com.github.setvisible.messorganizer.core.ModelListener;
import com.github.setvisible.messorganizer.settings.UserPreference;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * Main view of the application.
 */
public class MainWindowPresenter implements ModelListener, Initializable {

	private Model model;

	@FXML
	private BodyPresenter bodyController;

	@Override
	public void initialize(final URL url, final ResourceBundle resourceBundle) {
	}

	public void setModel(final Model model) {
		if (this.model != null) {
			this.model.removeListener(this);
		}
		this.model = model;
		if (this.model != null) {
			this.model.addListener(this);
		}

		this.bodyController.setModel(model);

	}

	// ***********************************************************************
	public void setUserPreference(final UserPreference userPreference) {
		this.bodyController.setUserPreference(userPreference);
	}

	// ************************************************************************
	// Actions
	// ************************************************************************
	private Consumer<?> callbackNewFile;
	private Consumer<?> callbackOpen;
	private Consumer<?> callbackSave;
	private Consumer<?> callbackSaveAs;
	private Consumer<?> callbackExit;
	private Consumer<?> callbackShowStatistics;
	private Consumer<?> callbackShowUserPreferences;
	private Consumer<?> callbackAbout;

	public void setNewFileAction(Consumer<?> callbackNewFile) {
		this.callbackNewFile = callbackNewFile;
	}

	public void setOpenAction(Consumer<?> callbackOpen) {
		this.callbackOpen = callbackOpen;
	}

	public void setSaveAction(Consumer<?> callbackSave) {
		this.callbackSave = callbackSave;
	}

	public void setSaveAsAction(Consumer<?> callbackSaveAs) {
		this.callbackSaveAs = callbackSaveAs;
	}

	public void setExitAction(Consumer<?> callbackExit) {
		this.callbackExit = callbackExit;
	}

	public void setOnShowStatisticsAction(Consumer<?> callbackShowStatistics) {
		this.callbackShowStatistics = callbackShowStatistics;
	}

	public void setOnShowUserPreferencesAction(Consumer<?> callbackShowUserPreferences) {
		this.callbackShowUserPreferences = callbackShowUserPreferences;
		this.bodyController.setOnShowUserPreferences(callbackShowUserPreferences);
	}

	public void setOnAboutAction(Consumer<?> callbackAbout) {
		this.callbackAbout = callbackAbout;
	}

	// ************************************************************************
	// Events
	// ************************************************************************
	@FXML
	private void newFile() {
		callbackNewFile.accept(null);
	}

	@FXML
	private void open() {
		callbackOpen.accept(null);
	}

	@FXML
	private void save() {
		callbackSave.accept(null);
	}

	@FXML
	private void saveAs() {
		callbackSaveAs.accept(null);
	}

	@FXML
	private void exit() {
		callbackExit.accept(null);
	}

	@FXML
	private void showStatistics() {
		callbackShowStatistics.accept(null);
	}

	@FXML
	public void showUserPreferences() {
		callbackShowUserPreferences.accept(null);
	}

	@FXML
	private void about() {
		callbackAbout.accept(null);
	}

	// ************************************************************************
	// Model Listeners
	// ************************************************************************
	@Override
	public void onDataChanged() {

	}
}