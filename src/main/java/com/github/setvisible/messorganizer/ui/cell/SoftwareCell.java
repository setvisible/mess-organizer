package com.github.setvisible.messorganizer.ui.cell;

import java.util.function.Consumer;

import com.github.setvisible.messorganizer.core.Decision;
import com.github.setvisible.messorganizer.core.Software;
import com.github.setvisible.messorganizer.util.DateUtil;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;

/**
 * 
 * <pre>
 *  
 *  MySoft2000.exe
 *      		Destination: D:\Backup\					[   Apply   ]
 *   Icon		similarity: 99.9%						[ Ignore... ]
 *   			version: 1.3  date: 1/2/2019			
 * ---------------------------------------------------------------------
 *  MySoft2000.exe
 *      		Replace: D:\Backup\MySoft2000-v1.2.exe	[   Apply   ]
 *   Icon		similarity: 99.9%						[ Ignore... ]	
 * 				version: 1.3  date: 1/2/2019
 * 
 * </pre>
 */
public class SoftwareCell extends ListCell<Software> {

	private static final String STYLE_TITLE = "-fx-font-size: 1.4em;";
	private static final String STYLE_CONTENT = "-fx-font-size: 1.1em;";

	private final AnchorPane content = new AnchorPane();
	private final GridPane gridPane = new GridPane();
	private final ImageView fileIcon = new ImageView();
	private final Label fileLabel = new Label();
	private final Label destinationLabel = new Label();
	private final Label similarityLabel = new Label();
	private final Label infoLabel = new Label();
	private final Button applyButton = new Button();
	private final Button optionButton = new Button();

	public SoftwareCell() {

		fileIcon.setFitWidth(48);
		fileIcon.setPreserveRatio(true);

		fileLabel.setStyle(STYLE_TITLE);

		destinationLabel.setStyle(STYLE_CONTENT);
		destinationLabel.setWrapText(false);
		destinationLabel.setEllipsisString("...");

		infoLabel.setStyle(STYLE_CONTENT);

		similarityLabel.setStyle(STYLE_CONTENT);
		similarityLabel.setTooltip(new Tooltip("File name similarity"));

		applyButton.setText("Apply");
		applyButton.setMinWidth(100);
		applyButton.setOnAction(e -> apply());

		optionButton.setText("Options...");
		optionButton.setMinWidth(100);
		optionButton.setOnAction(e -> openOptionDialog());

		setNodeLocation(0, 0, fileLabel);
		setNodeLocation(1, 0, fileIcon);
		setNodeLocation(1, 1, destinationLabel);
		setNodeLocation(2, 1, similarityLabel);
		setNodeLocation(3, 1, infoLabel);
		setNodeLocation(0, 2, applyButton);
		setNodeLocation(2, 2, optionButton);

		GridPane.setValignment(fileIcon, VPos.CENTER);
		GridPane.setHalignment(fileIcon, HPos.CENTER);
		GridPane.setRowSpan(fileIcon, GridPane.REMAINING);
		GridPane.setColumnSpan(fileLabel, GridPane.REMAINING);
		GridPane.setRowSpan(applyButton, 2);
		GridPane.setRowSpan(optionButton, 2);
		GridPane.setValignment(applyButton, VPos.BOTTOM);
		GridPane.setValignment(optionButton, VPos.TOP);

		addColumnConstraint(gridPane, Priority.NEVER, 100d);
		addColumnConstraint(gridPane, Priority.SOMETIMES, Region.USE_COMPUTED_SIZE);
		addColumnConstraint(gridPane, Priority.NEVER, Region.USE_COMPUTED_SIZE);

		addRowConstraint(gridPane, Priority.NEVER);
		addRowConstraint(gridPane, Priority.NEVER);
		addRowConstraint(gridPane, Priority.NEVER);
		addRowConstraint(gridPane, Priority.ALWAYS);

		gridPane.setHgap(0d);
		gridPane.setVgap(5d);

		gridPane.getChildren().add(fileIcon);
		gridPane.getChildren().add(fileLabel);
		gridPane.getChildren().add(destinationLabel);
		gridPane.getChildren().add(similarityLabel);
		gridPane.getChildren().add(infoLabel);
		gridPane.getChildren().add(applyButton);
		gridPane.getChildren().add(optionButton);

		AnchorPane.setTopAnchor(gridPane, 5d);
		AnchorPane.setLeftAnchor(gridPane, 0d);
		AnchorPane.setBottomAnchor(gridPane, 5d);
		AnchorPane.setRightAnchor(gridPane, 5d);

		content.getChildren().add(gridPane);
	}

	@Override
	protected void updateItem(final Software software, final boolean empty) {
		super.updateItem(software, empty);

		setText(null);

		if (empty || software == null) {
			setGraphic(null);
			setContentDisplay(ContentDisplay.LEFT);

		} else {
			fileIcon.setImage(software.getFileIcon());
			fileLabel.setText(software.getFileName());
			similarityLabel.setText(formatSimilarityLabel(software));
			infoLabel.setText(formatInfoLable(software));

			fileNameProperty.bind(software.fileNameProperty());
			hasDestinationProperty.bind(software.decisionProperty().isNotEqualTo(Decision.DONT_MOVE));

			destinationLabel.textProperty().bind(bindDestinationLabel(software));

			applyButton.disableProperty().bind(software.decisionProperty().isEqualTo(Decision.DONT_MOVE));

			setGraphic(content);
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		}
	}

	private static StringExpression bindDestinationLabel(final Software software) {
		final String destination = software.getDestinationPathName() != null ? software.getDestinationPathName() : "";
		return Bindings //
				.when(software.decisionProperty().isEqualTo(Decision.MOVE))
				.then(String.format("Move to: %s", destination)) //
				.otherwise(Bindings //
						.when(software.decisionProperty().isEqualTo(Decision.REPLACE)) //
						.then(String.format("Replace: %s", destination)) //
						.otherwise("No action"));
	}

	// ************************************************************************
	// Actions
	// ************************************************************************
	private Consumer<Software> callbackOpenOptionDialog;
	private Consumer<Software> callbackApply;

	public void setOnApplyAction(Consumer<Software> callbackApply) {
		this.callbackApply = callbackApply;
	}

	public void setOnOpenOptionDialogAction(Consumer<Software> callbackOpenOptionDialog) {
		this.callbackOpenOptionDialog = callbackOpenOptionDialog;
	}

	private void apply() {
		callbackApply.accept(getItem());
	}

	private void openOptionDialog() {
		callbackOpenOptionDialog.accept(getItem());
	}

	// ************************************************************************
	// GUI
	// ************************************************************************
	final StringProperty destinationTitleProperty = new SimpleStringProperty();
	final StringProperty fileNameProperty = new SimpleStringProperty();
	final BooleanProperty hasDestinationProperty = new SimpleBooleanProperty();

	public StringProperty fileNameProperty() {
		return fileNameProperty;
	}

	public BooleanProperty hasDestinationProperty() {
		return hasDestinationProperty;
	}

	// ************************************************************************
	// Helpers
	// ************************************************************************
	private static void addColumnConstraint(final GridPane gridPane, final Priority hgrow, final double minWidth) {
		gridPane.getColumnConstraints().add(//
				new ColumnConstraints(minWidth, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, hgrow, HPos.LEFT,
						true));
	}

	private static void addRowConstraint(final GridPane gridPane, final Priority vgrow) {
		gridPane.getRowConstraints().add(//
				new RowConstraints(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, vgrow,
						VPos.CENTER, true));
	}

	private static void setNodeLocation(final int rowIndex, final int columnIndex, final Node child) {
		GridPane.setConstraints(child, columnIndex, rowIndex);
	}

	private static String formatSimilarityLabel(final Software software) {
		assert software != null;
		switch (software.getDecision()) {
		case MOVE:
		case REPLACE:
			final double similarityInPercent = 100.0 * software.getSimilarity();
			return String.format("Similarity: %.1f%%", similarityInPercent);

		case DONT_MOVE:
		default:
			return "-";
		}
	}

	private static String formatInfoLable(final Software software) {
		assert software != null;
		switch (software.getDecision()) {
		case MOVE:
		case REPLACE:
			final String date = DateUtil.format(software.getVersionDate());
			return String.format("Version: %s  Date: %s", software.getVersionIdentifier(), date);

		case DONT_MOVE:
		default:
			return "-";
		}
	}
}
