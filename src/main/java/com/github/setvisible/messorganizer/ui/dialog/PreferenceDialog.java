package com.github.setvisible.messorganizer.ui.dialog;

import java.io.IOException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.setvisible.messorganizer.settings.UserPreference;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The class PreferenceDialog is a dialog to for the user preferences.
 *
 */
public class PreferenceDialog extends Dialog<ButtonType> {

	private static final Logger logger = LoggerFactory.getLogger(PreferenceDialog.class);

	private PreferenceDialogController controller;

	public PreferenceDialog(final Stage stage) {
		assert stage != null;

		/* Load the FXML Scene */
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("locale.en_US");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("preferencedialog.fxml"), bundle);
			Parent root = loader.load();
			controller = loader.<PreferenceDialogController>getController();
			controller.setPrimaryStage(stage);
			this.getDialogPane().setContent(root);

			this.setTitle(loader.getResources().getString("settings"));
			this.initModality(Modality.APPLICATION_MODAL);
			this.initOwner(stage.getScene().getWindow());
			this.setOnCloseRequest(e -> controller.shutdown());

			/* Add the Buttons */
			this.getDialogPane().getButtonTypes().setAll(ButtonType.CLOSE);

			final Button buttonOk = (Button) this.getDialogPane().lookupButton(ButtonType.CLOSE);
			buttonOk.setText(loader.getResources().getString("close"));

		} catch (IOException ex) {
			logger.error(ex.getMessage());
		}
	}

	public void setUserPreference(UserPreference userPreference) {
		controller.setUserPreference(userPreference);
	}
}
