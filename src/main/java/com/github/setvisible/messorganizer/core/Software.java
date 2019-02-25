package com.github.setvisible.messorganizer.core;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.github.setvisible.messorganizer.util.LocalDateAdapter;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for a Software.
 *
 */
public class Software {

	private final StringProperty fileName = new SimpleStringProperty();
	private final StringProperty fullFileName = new SimpleStringProperty();
	private final StringProperty destinationPathName = new SimpleStringProperty();
	private final DoubleProperty similarity = new SimpleDoubleProperty();
	private final ObjectProperty<LocalDate> versionDate = new SimpleObjectProperty<LocalDate>();

	public Software() {
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

	// ****************************************************************************
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
}
