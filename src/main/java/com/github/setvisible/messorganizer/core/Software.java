package com.github.setvisible.messorganizer.core;

import java.nio.file.attribute.FileTime;
import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.github.setvisible.messorganizer.util.LocalDateAdapter;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

/**
 * Model class for a Software.
 *
 */
public class Software {

	private final StringProperty fileName = new SimpleStringProperty();
	private final StringProperty fullFileName = new SimpleStringProperty();
	private final StringProperty fileSize = new SimpleStringProperty();

	private final ObjectProperty<Image> fileIcon = new SimpleObjectProperty<Image>();

	private final ObjectProperty<FileTime> fileCreationTime = new SimpleObjectProperty<FileTime>();
	private final ObjectProperty<FileTime> fileLastAccessTime = new SimpleObjectProperty<FileTime>();
	private final ObjectProperty<FileTime> fileLastModifiedTime = new SimpleObjectProperty<FileTime>();
	private final StringProperty versionIdentifier = new SimpleStringProperty();
	private final ObjectProperty<LocalDate> versionDate = new SimpleObjectProperty<LocalDate>();

	private final ObjectProperty<Decision> decision = new SimpleObjectProperty<Decision>();
	private final StringProperty destinationPathName = new SimpleStringProperty();
	private final DoubleProperty similarity = new SimpleDoubleProperty();

	public Software() {
		decision.set(Decision.DONT_MOVE);
	}

	// ****************************************************************************
	public String getFileName() {
		return fileName.get();
	}

	public void setFileName(final String fileName) {
		this.fileName.set(fileName);
	}

	public StringProperty fileNameProperty() {
		return fileName;
	}

	// ****************************************************************************
	public String getFullFileName() {
		return fullFileName.get();
	}

	public void setFullFileName(final String fullFileName) {
		this.fullFileName.set(fullFileName);
	}

	public StringProperty fullFileNameProperty() {
		return fullFileName;
	}

	// ****************************************************************************
	/**
	 * Gets a string containing the human-readable size of the file.
	 * <p>
	 * Example: "15 MB", "521 KB"
	 * 
	 * @return String
	 */
	public String getFileSize() {
		return fileSize.get();
	}

	public void setFileSize(final String fileSize) {
		this.fileSize.set(fileSize);
	}

	public StringProperty fileSizeProperty() {
		return fileSize;
	}

	// ****************************************************************************
	public Image getFileIcon() {
		return fileIcon.get();
	}

	public void setFileIcon(final Image fileIcon) {
		this.fileIcon.set(fileIcon);
	}

	public ObjectProperty<Image> fileIconProperty() {
		return fileIcon;
	}

	// ****************************************************************************
	/**
	 * Gets the creation time of the file.
	 * 
	 * @return FileTime
	 */
	public FileTime getFileCreationTime() {
		return fileCreationTime.get();
	}

	public void setFileCreationTime(final FileTime fileCreationTime) {
		this.fileCreationTime.set(fileCreationTime);
	}

	public ObjectProperty<FileTime> creationTimeProperty() {
		return fileCreationTime;
	}

	// ****************************************************************************
	/**
	 * Gets the last access time of the file.
	 * 
	 * @return FileTime
	 */
	public FileTime getFileLastAccessTime() {
		return fileLastAccessTime.get();
	}

	public void setFileLastAccessTime(final FileTime fileLastAccessTime) {
		this.fileLastAccessTime.set(fileLastAccessTime);
	}

	public ObjectProperty<FileTime> fileLastAccessTimeProperty() {
		return fileLastAccessTime;
	}

	// ****************************************************************************
	/**
	 * Gets the last modified time of the file.
	 * 
	 * @return FileTime
	 */
	public FileTime getFileLastModifiedTime() {
		return fileLastModifiedTime.get();
	}

	public void setFileLastModifiedTime(final FileTime fileLastModifiedTime) {
		this.fileLastModifiedTime.set(fileLastModifiedTime);
	}

	public ObjectProperty<FileTime> fileLastModifiedTimeProperty() {
		return fileLastModifiedTime;
	}


	// ****************************************************************************
	/**
	 * Gets the unique identifier of the version (example: "1.3.985-rc2")
	 * 
	 * @return String
	 */
	public String getVersionIdentifier() {
		return versionIdentifier.get();
	}

	public void setVersionIdentifier(final String versionIdentifier) {
		this.versionIdentifier.set(versionIdentifier);
	}

	public StringProperty versionIdentifierProperty() {
		return versionIdentifier;
	}

	// ****************************************************************************
	/**
	 * Gets the date of the version, if applicable.
	 * <p>
	 * It's different from the creation or access or modification time of the file.
	 * 
	 * @return LocalDate
	 */
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public LocalDate getVersionDate() {
		return versionDate.get();
	}

	public void setVersionDate(final LocalDate versionDate) {
		this.versionDate.set(versionDate);
	}

	public ObjectProperty<LocalDate> versionDateProperty() {
		return versionDate;
	}

	// ****************************************************************************
	public Decision getDecision() {
		return decision.get();
	}

	public void setDecision(final Decision decision) {
		this.decision.set(decision);
	}

	public ObjectProperty<Decision> decisionProperty() {
		return decision;
	}

	// ****************************************************************************
	public String getDestinationPathName() {
		return destinationPathName.get();
	}

	public void setDestinationPathName(final String destinationPathName) {
		this.destinationPathName.set(destinationPathName);
	}

	public StringProperty destinationPathNameProperty() {
		return destinationPathName;
	}

	// ****************************************************************************
	public Double getSimilarity() {
		return similarity.get();
	}

	public void setSimilarity(final double similarity) {
		this.similarity.set(similarity);
	}

	public DoubleProperty similarityProperty() {
		return similarity;
	}
}
