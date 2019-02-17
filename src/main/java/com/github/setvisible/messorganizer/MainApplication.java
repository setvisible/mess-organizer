package com.github.setvisible.messorganizer;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.github.setvisible.messorganizer.core.Software;
import com.github.setvisible.messorganizer.core.SoftwareListWrapper;
import com.github.setvisible.messorganizer.ui.RootLayoutController;
import com.github.setvisible.messorganizer.ui.SoftwareOverviewController;
import com.github.setvisible.messorganizer.ui.VersionDateStatisticsController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApplication {

	private Stage primaryStage;
	private BorderPane rootLayout;

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

	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Mess Organizer");

		// Set the application icon.
		this.primaryStage.getIcons().add(new Image("images/icon_32x32.png"));

		initRootLayout();

		showSoftwareOverview();
	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApplication.class.getResource("ui/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);

			// Give the controller access to the main app.
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Try to load last opened software file.
		File file = getSoftwareFilePath();
		if (file != null) {
			loadSoftwareDataFromFile(file);
		}
	}

	/**
	 * Shows the software overview inside the root layout.
	 */
	public void showSoftwareOverview() {
		try {
			// Load software overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApplication.class.getResource("ui/SoftwareOverview.fxml"));
			AnchorPane softwareOverview = (AnchorPane) loader.load();

			// Set software overview into the center of root layout.
			rootLayout.setCenter(softwareOverview);

			// Give the controller access to the main app.
			SoftwareOverviewController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
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

	/**
	 * Opens a dialog to show birthday statistics.
	 */
	public void showStatistics() {
		try {
			// Load the fxml file and create a new stage for the popup.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApplication.class.getResource("ui/VersionDateStatistics.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Version Date Statistics");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the softwares into the controller.
			VersionDateStatisticsController controller = loader.getController();
			controller.setSoftwareData(softwareData);

			dialogStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
