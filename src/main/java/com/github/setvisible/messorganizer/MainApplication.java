package com.github.setvisible.messorganizer;

import java.io.File;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.setvisible.messorganizer.core.Model;
import com.github.setvisible.messorganizer.core.Software;
import com.github.setvisible.messorganizer.io.Parser;
import com.github.setvisible.messorganizer.settings.UserPreference;
import com.github.setvisible.messorganizer.ui.MainWindowPresenter;
import com.github.setvisible.messorganizer.ui.MainWindowView;
import com.github.setvisible.messorganizer.ui.dialog.PreferenceDialog;
import com.github.setvisible.messorganizer.ui.dialog.StatisticsDialog;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApplication extends Application {

	private static final FileChooser.ExtensionFilter FILE_EXTENSION //
			= new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");

	private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);

	private final Model model = new Model();
	private final UserPreference userPreference = new UserPreference();

	private Stage primaryStage;

	/**
	 * Constructor
	 */
	public MainApplication() {
	}

	@Override
	public void start(Stage stage) {
		this.primaryStage = stage;

		primaryStage.setOnCloseRequest(event -> {
			event.consume();
			this.exit();
		});
		primaryStage.setOnShowing(event -> {
			this.userPreference.readUserPreference();
			this.readSettings();
		});

		final MainWindowView mainWindowView = new MainWindowView();

		final Scene scene = new Scene(mainWindowView.getView());
		primaryStage.setTitle("Mess Organizer");
		primaryStage.getIcons().add(new Image("images/icon_32x32.png"));

		// Set the application style.
		// final String uri = getClass().getResource("/DarkTheme.css").toExternalForm();
		// scene.getStylesheets().add(uri);

		primaryStage.setScene(scene);
		primaryStage.show();

		final MainWindowPresenter mainWindow = (MainWindowPresenter) mainWindowView.getPresenter();

		mainWindow.setModel(model);
		mainWindow.setUserPreference(userPreference);

		mainWindow.setNewFileAction(e -> newFile());
		mainWindow.setOpenAction(e -> open());
		mainWindow.setSaveAsAction(e -> saveAs());
		mainWindow.setSaveAction(e -> save());
		mainWindow.setExitAction(e -> exit());
		mainWindow.setOnApplyAllAction(e -> applyAll());
		mainWindow.setOnApplyAction(e -> apply(e));
		mainWindow.setOnShowStatisticsAction(e -> showStatistics());
		mainWindow.setOnShowUserPreferencesAction(e -> showUserPreferences());
		mainWindow.setOnAboutAction(e -> about());

		// Try to load last opened software file if the app has crashed.
		// final File file = getSoftwareFilePath();
		// if (file != null) {
		// loadSoftwareDataFromFile(file);
		// }
	}

	// ************************************************************************
	/**
	 * Returns the preference file, i.e. the file that was last opened. The
	 * preference is read from the OS specific registry. If no such preference can
	 * be found, null is returned.
	 * 
	 * @return
	 */
	public File getCurrentFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApplication.class);
		String filePath = prefs.get("filePath", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

	/**
	 * Sets the file path of the currently loaded file. The path is persisted in the
	 * OS specific registry.
	 * 
	 * @param file the file or null to remove the path
	 */
	public void setCurrentFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(MainApplication.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());
			primaryStage.setTitle("Mess Organizer - " + file.getName());
		} else {
			prefs.remove("filePath");
			primaryStage.setTitle("Mess Organizer");
		}
	}

	// ************************************************************************
	private void newFile() {
		model.getSoftwareData().clear();
		setCurrentFilePath(null);
	}

	private void open() {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(FILE_EXTENSION);
		final File file = fileChooser.showOpenDialog(primaryStage);
		if (file != null) {
			try {
				Parser.loadDataFromFile(file, model);
				setCurrentFilePath(file);

			} catch (Exception e) { // catches ANY exception
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Could not load data");
				alert.setContentText("Could not load data from file:\n" + file.getPath());

				alert.showAndWait();
			}
		}
	}

	private void save() {
		final File file = getCurrentFilePath();
		if (file != null) {
			try {
				Parser.saveDataToFile(file, model);

			} catch (Exception e) { // catches ANY exception
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Could not save data");
				alert.setContentText("Could not save data to file:\n" + file.getPath());

				alert.showAndWait();
			}
		} else {
			saveAs();
		}
	}

	private void saveAs() {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(FILE_EXTENSION);
		File file = fileChooser.showSaveDialog(primaryStage);

		if (file != null) {
			// Make sure it has the correct extension
			if (!file.getPath().endsWith(".xml")) {
				file = new File(file.getPath() + ".xml");
			}
			setCurrentFilePath(file);
			save();
		}
	}

	private void exit() {
		final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Mess Organizer");
		alert.setHeaderText("Do you really want to exit?");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(primaryStage);
		alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

		final Button buttonExit = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
		buttonExit.setText("Yes");
		buttonExit.setDefaultButton(false);

		final Button buttonCancel = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
		buttonCancel.setText("No");

		final Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.YES) {
			this.userPreference.writeUserPreference();
			this.writeSettings();
			Platform.exit();
		}
	}

	// ************************************************************************
	private void applyAll() {
	}

	private void apply(final Software software) {
	}

	private void showStatistics() {
		final StatisticsDialog dialog = new StatisticsDialog(primaryStage);
		dialog.setSoftwareData(model.getSoftwareData());
		dialog.show();
	}

	private void showUserPreferences() {
		final PreferenceDialog dialog = new PreferenceDialog(primaryStage);
		dialog.setUserPreference(userPreference);
		dialog.show();
	}

	private void about() {
		final Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(ResourceBundle.getBundle("locale.en_US").getString("about"));
		alert.setHeaderText("Mess Organizer");
		alert.setContentText(ResourceBundle.getBundle("locale.en_US").getString("about.author") //
				+ ":\t Sebastien Vavassori" + "\n\n" //
				+ ResourceBundle.getBundle("locale.en_US").getString("about.website") //
				+ ":\t https://github.com/setvisible");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(primaryStage);
		alert.getButtonTypes().setAll(ButtonType.OK);
		alert.show();
	}

	// ************************************************************************
	// Settings
	// ************************************************************************
	private void clearSettings() {
		final Preferences prefs = Preferences.userNodeForPackage(this.getClass()).node("window");
		try {
			prefs.clear();
		} catch (final BackingStoreException e) {
			logger.error(e.getMessage());
		}
	}

	private void readSettings() {
		final Preferences prefs = Preferences.userNodeForPackage(this.getClass()).node("window");

		primaryStage.setX(prefs.getDouble("location_x", 100));
		primaryStage.setY(prefs.getDouble("location_y", 100));
		primaryStage.setWidth(prefs.getDouble("width", 1024));
		primaryStage.setHeight(prefs.getDouble("height", 786));
		primaryStage.setMaximized(prefs.getBoolean("is_maximized", false));
	}

	private void writeSettings() {
		final Preferences prefs = Preferences.userNodeForPackage(this.getClass()).node("window");

		prefs.putBoolean("is_maximized", primaryStage.isMaximized());

		final boolean wasMaximized = primaryStage.isMaximized();
		primaryStage.setMaximized(false);

		prefs.putDouble("location_x", primaryStage.getX());
		prefs.putDouble("location_y", primaryStage.getY());
		prefs.putDouble("width", primaryStage.getWidth());
		prefs.putDouble("height", primaryStage.getHeight());

		primaryStage.setMaximized(wasMaximized);
	}
}
