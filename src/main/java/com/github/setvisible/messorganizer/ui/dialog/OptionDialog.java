package com.github.setvisible.messorganizer.ui.dialog;

import java.io.IOException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.setvisible.messorganizer.core.Model;
import com.github.setvisible.messorganizer.core.Software;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The class OptionDialog is a dialog to for the option.
 *
 */
public class OptionDialog extends Dialog<ButtonType> {

	private static final Logger logger = LoggerFactory.getLogger(OptionDialog.class);

	private OptionDialogController controller;
	private Model model;
	private Software software;

	public OptionDialog(final Stage stage) {
		assert stage != null;

		/* Load the FXML Scene */
		try {
			final ResourceBundle bundle = ResourceBundle.getBundle("locale.en_US");
			final FXMLLoader loader = new FXMLLoader(getClass().getResource("optiondialog.fxml"), bundle);
			final Parent root = loader.load();
			controller = loader.<OptionDialogController>getController();
			this.getDialogPane().setContent(root);

			this.setTitle(loader.getResources().getString("options"));
			this.initModality(Modality.APPLICATION_MODAL);
			this.initOwner(stage.getScene().getWindow());
			// this.setOnCloseRequest(e -> controller.shutdown());

			/* Add the Buttons */
			this.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
			
			final Button buttonOk = (Button) this.getDialogPane().lookupButton(ButtonType.OK);
			buttonOk.setText("Ok");
			buttonOk.setDefaultButton(false);
			buttonOk.setOnAction(e -> accept());

			final Button buttonClose = (Button) this.getDialogPane().lookupButton(ButtonType.CANCEL);
			buttonClose.setText(loader.getResources().getString("cancel"));
			buttonClose.setDefaultButton(false);

		
//			final Optional<ButtonType> result = this.showAndWait();
//			if (result.isPresent() && result.get() == ButtonType.YES) {				
//				software.setDecision(controller.getDecision());
//			}
			
		} catch (final IOException ex) {
			logger.error(ex.getMessage());
		}
	}

	public void setItem(final Software software) {
		this.software = software;
		controller.setDecision(software.getDecision());
		controller.setDestinationPathName(software.getDestinationPathName());
	}

	private void accept() {
		if (software != null) {
			software.setDecision(controller.getDecision());
			software.setDestinationPathName(controller.getDestinationPathName());
		}
	}
}
