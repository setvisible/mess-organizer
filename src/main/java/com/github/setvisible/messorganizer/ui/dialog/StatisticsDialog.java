package com.github.setvisible.messorganizer.ui.dialog;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.setvisible.messorganizer.core.Software;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The class StatisticsDialog is a dialog that show statistics.
 *
 */
public class StatisticsDialog extends Dialog<ButtonType> {

	private final Logger logger = LoggerFactory.getLogger(StatisticsDialog.class);

	private StatisticsDialogController controller;

	public StatisticsDialog(final Stage stage) {

		try {
			/* Load the FXML Scene */
			final ResourceBundle bundle = ResourceBundle.getBundle("locale.en_US");
			final FXMLLoader loader = new FXMLLoader(getClass().getResource("statisticsdialog.fxml"), bundle);
			final Parent root = loader.load();
			controller = loader.<StatisticsDialogController>getController();
			this.getDialogPane().setContent(root);

			this.setTitle(loader.getResources().getString("statistics"));
			this.initModality(Modality.APPLICATION_MODAL);
			if (stage != null) {
				this.initOwner(stage.getScene().getWindow());
			}
			this.setOnCloseRequest(e -> controller.shutdown());

			/* Add the Buttons */
			this.getDialogPane().getButtonTypes().setAll(ButtonType.CLOSE);

			final Button buttonOk = (Button) this.getDialogPane().lookupButton(ButtonType.CLOSE);
			buttonOk.setText(loader.getResources().getString("close"));

		} catch (final IOException e) {
			logger.error(e.getMessage());
		}

	}

	public void setSoftwareData(final List<Software> softwares) {
		controller.setSoftwareData(softwares);
	}

}
