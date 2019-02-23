package com.github.setvisible.messorganizer;

import java.io.File;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.github.setvisible.messorganizer.core.Software;
import com.github.setvisible.messorganizer.core.SoftwareListWrapper;
import com.github.setvisible.messorganizer.ui.MainWindowPresenter;
import com.github.setvisible.messorganizer.ui.MainWindowView;
import com.github.setvisible.messorganizer.ui.dialog.StatisticsDialog;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApplication extends Application {

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

		final MainWindowView appView = new MainWindowView();

		final Scene scene = new Scene(appView.getView());
		stage.setTitle("Mess Organizer");

		// Set the application icon.
		stage.getIcons().add(new Image("images/icon_32x32.png"));

		// Set the application style.
		final String uri = getClass().getResource("/DarkTheme.css").toExternalForm();
		scene.getStylesheets().add(uri);

		stage.setScene(scene);
		stage.show();

		this.primaryStage = stage;

		final MainWindowPresenter mainWindow = (MainWindowPresenter) appView.getPresenter();
		mainWindow.setMainApp(this);

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

	public void showStatistics() {
		final StatisticsDialog dialog = new StatisticsDialog(primaryStage);
		dialog.setSoftwareData(softwareData);
		dialog.show();
	}

}
