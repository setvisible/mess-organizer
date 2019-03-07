package com.github.setvisible.messorganizer.ui.widget;

import java.net.URL;
import java.util.ResourceBundle;

import com.github.setvisible.messorganizer.core.Software;
import com.github.setvisible.messorganizer.util.DateUtil;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class DetailPanelPresenter implements Initializable {


	@FXML
	private Label fileLabel;
	@FXML
	private Label versionLabel;
	@FXML
	private Label sizeLabel;
	@FXML
	private Label creationTimeLabel;
	@FXML
	private Label lastAccessTimeLabel;
	@FXML
	private Label lastModifiedTimeLabel;

	@Override
	public void initialize(final URL url, final ResourceBundle resourceBundle) {
	}

	public void setSoftware(final Software software) {
		if (software != null) {
			// Fill the labels with info from the software object.
			fileLabel.setText(software.getFullFileName());
			versionLabel.setText(software.getVersionIdentifier());
			sizeLabel.setText(software.getFileSize());
			creationTimeLabel.setText(DateUtil.formatFileTime(software.getFileCreationTime()));
			lastAccessTimeLabel.setText(DateUtil.formatFileTime(software.getFileLastAccessTime()));
			lastModifiedTimeLabel.setText(DateUtil.formatFileTime(software.getFileLastModifiedTime()));
		} else {
			// Software is null, remove all the text.
			fileLabel.setText("-");
			versionLabel.setText("-");
			sizeLabel.setText("-");
			creationTimeLabel.setText(DateUtil.formatFileTime(null));
			lastAccessTimeLabel.setText(DateUtil.formatFileTime(null));
			lastModifiedTimeLabel.setText(DateUtil.formatFileTime(null));
		}
	}
}
