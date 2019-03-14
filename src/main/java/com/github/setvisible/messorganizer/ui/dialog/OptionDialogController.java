package com.github.setvisible.messorganizer.ui.dialog;

import java.io.File;

import com.github.setvisible.messorganizer.core.Decision;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

/**
 * The controller of OptionDialog.
 */
public class OptionDialogController {

	private final ToggleGroup group = new ToggleGroup();

	private String destinationPathName;

	@FXML
	private RadioButton radioMove;
	@FXML
	private RadioButton radioReplace;
	@FXML
	private RadioButton radioDontMove;
	@FXML
	private TextField destinationPath;
	@FXML
	private TextField destinationFile;
	@FXML
	private HBox moveHBox;
	@FXML
	private HBox replaceHBox;

	@FXML
	public void initialize() {
		radioMove.setToggleGroup(group);
		radioReplace.setToggleGroup(group);
		radioDontMove.setToggleGroup(group);

		moveHBox.disableProperty().bind(radioMove.selectedProperty().not());
		replaceHBox.disableProperty().bind(radioReplace.selectedProperty().not());

		radioMove.setSelected(true);
	}

	@FXML
	public void browsePath() {
		// TODO
	}

	@FXML
	public void browseFile() {
		// TODO
	}

	public Decision getDecision() {
		if (radioMove.isSelected()) {
			return Decision.MOVE;
		} else if (radioReplace.isSelected()) {
			return Decision.REPLACE;
		} else {
			return Decision.DONT_MOVE;
		}
	}

	public void setDecision(final Decision decision) {
		switch (decision) {
		case MOVE:
			radioMove.setSelected(true);
			break;

		case REPLACE:
			radioReplace.setSelected(true);
			break;

		case DONT_MOVE:
			radioDontMove.setSelected(true);
			break;

		default:
			break;
		}
	}

	public String getDestinationPathName() {
		return destinationPathName;
	}

	public void setDestinationPathName(final String destinationPathName) {
		this.destinationPathName = destinationPathName;

		// TODO

		if (destinationPathName != null) {
			final File file = new File(destinationPathName);
			destinationPath.setText(file.getParent().toString());
			destinationFile.setText(file.toString());
		}
	}
}
