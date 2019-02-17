package com.github.setvisible.messorganizer.core;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.github.setvisible.messorganizer.util.LocalDateAdapter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for a Software.
 *
 */
public class Software {
	private static final String SimpleObjectProperty = null;
	private final StringProperty softwareName;
	private final StringProperty vendorName;
	private final ObjectProperty<LocalDate> versionDate;

	/**
	 * Default constructor.
	 */
	public Software() {
		this(null, null);
	}

	/**
	 * Constructor with some initial data.
	 * 
	 * @param softwareName
	 * @param vendorName
	 */
	public Software(String softwareName, String vendorName) {
		this.softwareName = new SimpleStringProperty(softwareName);
		this.vendorName = new SimpleStringProperty(vendorName);
		this.versionDate = new SimpleObjectProperty<LocalDate>(LocalDate.of(1999, 2, 21));
	}

	public String getSoftwareName() {
		return softwareName.get();
	}

	public void setSoftwareName(String softwareName) {
		this.softwareName.set(softwareName);
	}

	public StringProperty firstSoftwareProperty() {
		return softwareName;
	}

	public String getVendorName() {
		return vendorName.get();
	}

	public void setVendorName(String vendorName) {
		this.vendorName.set(vendorName);
	}

	public StringProperty vendorNameProperty() {
		return vendorName;
	}

	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public LocalDate getVersionDate() {
		return versionDate.get();
	}

	public void setVersionDate(LocalDate versionDate) {
		this.versionDate.set(versionDate);
	}

	public ObjectProperty<LocalDate> versionDateProperty() {
		return versionDate;
	}
}
