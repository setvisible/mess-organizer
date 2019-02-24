package com.github.setvisible.messorganizer;

import java.io.File;
import java.util.Optional;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.setvisible.messorganizer.core.Software;
import com.github.setvisible.messorganizer.core.SoftwareListWrapper;
import com.github.setvisible.messorganizer.settings.UserPreference;
import com.github.setvisible.messorganizer.ui.MainWindowPresenter;
import com.github.setvisible.messorganizer.ui.MainWindowView;
import com.github.setvisible.messorganizer.ui.dialog.StatisticsDialog;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApplication extends Application {

	private final Logger logger = LoggerFactory.getLogger(MainApplication.class);

	private final UserPreference userPreference = new UserPreference();

	private Stage primaryStage;

	/**
	 * The data as an observable list of Software.
	 */
	private ObservableList<Software> softwareData = FXCollections.observableArrayList();

	/**
	 * Constructor
	 */
	public MainApplication() {
		// Add some sample data
		softwareData.add(new Software("Hans", "Muster"));
		softwareData.add(new Software("Ruth", "Mueller"));
		softwareData.add(new Software("Heinz", "Kurz"));
		softwareData.add(new Software("Cornelia", "Meier"));
		softwareData.add(new Software("Werner", "Meyer"));
		softwareData.add(new Software("Lydia", "Kunz"));
		softwareData.add(new Software("Anna", "Best"));
		softwareData.add(new Software("Stefan", "Meier"));
		softwareData.add(new Software("Martin", "Mueller"));
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

		// Set the application icon.
		primaryStage.getIcons().add(new Image("images/icon_32x32.png"));

		// Set the application style.
		// final String uri = getClass().getResource("/DarkTheme.css").toExternalForm();
		// scene.getStylesheets().add(uri);

		primaryStage.setScene(scene);
		primaryStage.show();


		final MainWindowPresenter mainWindow = (MainWindowPresenter) mainWindowView.getPresenter();
		mainWindow.setMainApp(this);
		mainWindow.setUserPreference(userPreference);

		// Try to load last opened software file.
		final File file = getSoftwareFilePath();
		if (file != null) {
			loadSoftwareDataFromFile(file);
		}
	}

	/**
	 * Returns the main stage.
	 * 
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Returns the data as an observable list of Softwares.
	 * 
	 * @return
	 */
	public ObservableList<Software> getSoftwareData() {
		return softwareData;
	}

	/**
	 * Returns the preference file, i.e. the file that was last opened. The
	 * preference is read from the OS specific registry. If no such preference
	 * can be found, null is returned.
	 * 
	 * @return
	 */
	public File getSoftwareFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApplication.class);
		String filePath = prefs.get("filePath", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

	/**
	 * Sets the file path of the currently loaded file. The path is persisted in
	 * the OS specific registry.
	 * 
	 * @param file
	 *            the file or null to remove the path
	 */
	public void setSoftwareFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(MainApplication.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());

			// Update the stage title.
			primaryStage.setTitle("Mess Organizer - " + file.getName());
		} else {
			prefs.remove("filePath");

			// Update the stage title.
			primaryStage.setTitle("Mess Organizer");
		}
	}

	/**
	 * Loads software data from the specified file. The current software data
	 * will be replaced.
	 * 
	 * @param file
	 */
	public void loadSoftwareDataFromFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(SoftwareListWrapper.class);
			Unmarshaller um = context.createUnmarshaller();

			// Reading XML from the file and unmarshalling.
			SoftwareListWrapper wrapper = (SoftwareListWrapper) um.unmarshal(file);

			softwareData.clear();
			softwareData.addAll(wrapper.getSoftwares());

			// Save the file path to the registry.
			setSoftwareFilePath(file);

		} catch (Exception e) { // catches ANY exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n" + file.getPath());

			alert.showAndWait();
		}
	}

	/**
	 * Saves the current software data to the specified file.
	 * 
	 * @param file
	 */
	public void saveSoftwareDataToFile(File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(SoftwareListWrapper.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Wrapping our software data.
			SoftwareListWrapper wrapper = new SoftwareListWrapper();
			wrapper.setSoftwares(softwareData);

			// Marshalling and saving XML to the file.
			m.marshal(wrapper, file);

			// Save the file path to the registry.
			setSoftwareFilePath(file);
		} catch (Exception e) { // catches ANY exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + file.getPath());

			alert.showAndWait();
		}
	}

	public void exit() {
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

	public void showStatistics() {
		final StatisticsDialog dialog = new StatisticsDialog(primaryStage);
		dialog.setSoftwareData(softwareData);
		dialog.show();
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
