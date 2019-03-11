package com.github.setvisible.messorganizer.ui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.setvisible.messorganizer.core.Model;
import com.github.setvisible.messorganizer.core.ModelListener;
import com.github.setvisible.messorganizer.core.Software;
import com.github.setvisible.messorganizer.settings.UserPreference;
import com.github.setvisible.messorganizer.settings.UserPreferenceListener;
import com.github.setvisible.messorganizer.ui.cell.SoftwareCell;
import com.github.setvisible.messorganizer.ui.widget.DetailPanelPresenter;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;

public class BodyPresenter implements ModelListener, UserPreferenceListener, Initializable {

	private static final Logger logger = LoggerFactory.getLogger(BodyPresenter.class);

	private Model model;
	private UserPreference userPreference;

	@FXML
	private TextField targetDirectory;
	@FXML
	private TextField sourceDirectory;
	@FXML
	private ListView<Software> listView;
	@FXML
	private DetailPanelPresenter detailPanelController;
	@FXML
	private Button resetButton;
	@FXML
	private Button analyzeButton;
	@FXML
	private Button applyAllButton;

	@Override
	public void initialize(final URL url, final ResourceBundle resourceBundle) {

		listView.setCellFactory(lv -> {
			final SoftwareCell cell = new SoftwareCell();
			cell.setOnApplyAction(callbackApply);
			cell.setOnOpenOptionDialogAction(callbackOpenOptionDialog);

			final ContextMenu contextMenu = new ContextMenu();

			final MenuItem copyItem = new MenuItem();
			copyItem.textProperty().bind(Bindings.format("Copy \"%s\" details", cell.fileNameProperty()));
			copyItem.setOnAction(e -> copyDetails(cell.getItem()));
			contextMenu.getItems().add(copyItem);

			contextMenu.getItems().add(new SeparatorMenuItem());

			final MenuItem openSourcetem = new MenuItem();
			openSourcetem.setText("Open file directory...");
			openSourcetem.setOnAction(e -> openSourceDirectory(cell.getItem()));
			contextMenu.getItems().add(openSourcetem);

			final MenuItem openTargetItem = new MenuItem();
			openTargetItem.setText("Open destination...");
			openTargetItem.setOnAction(e -> openTargetDirectory(cell.getItem()));
			openTargetItem.disableProperty().bind(cell.hasDestinationProperty().not());
			contextMenu.getItems().add(openTargetItem);

			contextMenu.getItems().add(new SeparatorMenuItem());

			final MenuItem applyItem = new MenuItem();
			applyItem.setText("Apply");
			applyItem.setOnAction(e -> apply(cell.getItem()));
			contextMenu.getItems().add(applyItem);

			contextMenu.getItems().add(new SeparatorMenuItem());

			final MenuItem openOptionDialogItem = new MenuItem();
			openOptionDialogItem.setText("Options...");
			openOptionDialogItem.setOnAction(e -> openOptionDialog(cell.getItem()));
			contextMenu.getItems().add(openOptionDialogItem);

			cell.setContextMenu(contextMenu);
			return cell;
		});

		// Listen for selection changes and show the software details when
		// changed.
		listView.getSelectionModel().selectedItemProperty()
				.addListener((obs, old, current) -> showSoftwareDetails(current));

		onDataChanged();
	}

	public void setModel(final Model model) {
		if (this.model != null) {
			this.model.removeListener(this);
			listView.getItems().clear();
		}
		this.model = model;
		if (this.model != null) {
			this.model.addListener(this);
			listView.setItems(this.model.getSoftwareData());
		}
	}

	/**
	 * Fills all text fields to show details about the software. If the specified
	 * software is null, all text fields are cleared.
	 *
	 * @param software the software or null
	 */
	private void showSoftwareDetails(final Software software) {
		detailPanelController.setSoftware(software);
	}

	private static void copyDetails(final Software software) {
		// TODO
	}

	private static void openSourceDirectory(final Software software) {
		if (software != null) {
			try {
				final File file = new File(software.getFullFileName());
				if (file.exists()) {
					Desktop.getDesktop().open(file.getParentFile());
				}
			} catch (final IOException e) {
				logger.warn(e.getMessage());
			}
		}
	}

	private static void openTargetDirectory(final Software software) {
		if (software != null && software.getDestinationPathName() != null) {
			try {
				final File file = new File(software.getDestinationPathName());
				if (file.exists()) {
					Desktop.getDesktop().open(file.getParentFile());
				}
			} catch (final IOException e) {
				logger.warn(e.getMessage());
			}
		}
	}

	// ************************************************************************
	// Actions
	// ************************************************************************
	private Consumer<?> callbackApplyAll;
	private Consumer<Software> callbackApply;
	private Consumer<Software> callbackOpenOptionDialog;
	private Consumer<?> callbackShowUserPreferences;

	public void setOnApplyAllAction(Consumer<?> callbackApplyAll) {
		this.callbackApplyAll = callbackApplyAll;
	}

	public void setOnApplyAction(Consumer<Software> callbackApply) {
		this.callbackApply = callbackApply;
	}

	public void setOnOpenOptionDialogAction(Consumer<Software> callbackOpenOptionDialog) {
		this.callbackOpenOptionDialog = callbackOpenOptionDialog;
	}

	public void setOnShowUserPreferences(final Consumer<?> callbackShowUserPreferences) {
		this.callbackShowUserPreferences = callbackShowUserPreferences;
	}

	@FXML
	public void reset() {
		if (this.model != null) {
			this.model.clear();
		}
	}

	@FXML
	public void analyze() {
		assert model != null;
		assert userPreference != null;

		final File sourcePath = new File(userPreference.getSourceDirectory());
		final File targetPath = new File(userPreference.getTargetDirectory());

		model.runAnalysis(sourcePath, targetPath);
	}

	@FXML
	public void applyAll() {
		callbackApplyAll.accept(null);
	}

	public void apply(final Software software) {
		callbackApply.accept(software);
	}

	public void openOptionDialog(final Software software) {
		callbackOpenOptionDialog.accept(software);
	}

	@FXML
	public void showUserPreferences() {
		callbackShowUserPreferences.accept(null);
	}

	public Software getSelectedItem() {
		return listView.getSelectionModel().getSelectedItem();
	}

	// ************************************************************************
	// Model Listeners
	// ************************************************************************
	@Override
	public void onDataChanged() {
		final boolean isEmpty = (this.model != null) ? this.model.getSoftwareData().isEmpty() : true;
		resetButton.setDisable(isEmpty);
		analyzeButton.setDisable(!isEmpty);
		applyAllButton.setDisable(isEmpty);

		// Clear software details.
		showSoftwareDetails(null);
	}

	// ************************************************************************
	// User Preference
	// ************************************************************************
	public void setUserPreference(final UserPreference userPreference) {
		if (this.userPreference != null) {
			this.userPreference.removeListener(this);
		}
		this.userPreference = userPreference;
		if (this.userPreference != null) {
			this.userPreference.addListener(this);

			onSourceDirectoryChanged();
			onTargetDirectoryChanged();
		}
	}

	@Override
	public void onSourceDirectoryChanged() {
		assert userPreference != null;
		final String directory = this.userPreference.getSourceDirectory();
		sourceDirectory.setText(directory);
	}

	@Override
	public void onTargetDirectoryChanged() {
		assert userPreference != null;
		final String directory = this.userPreference.getTargetDirectory();
		targetDirectory.setText(directory);
	}
}