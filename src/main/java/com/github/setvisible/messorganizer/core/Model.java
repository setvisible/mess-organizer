package com.github.setvisible.messorganizer.core;

import java.io.File;
import java.util.List;

import javax.swing.event.EventListenerList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {

	private final ObservableList<Software> softwareData = FXCollections.observableArrayList();

	private final EventListenerList listeners = new EventListenerList();

	// *************************************************************************
	public void clear() {
		softwareData.clear();
		notifyDataChanged();
	}

	public void analyze(final File sourceDirectory, final File targetDirectory) {
		clear();

		final Service service = new Service();
		service.setSourceDirectory(sourceDirectory);
		service.setTargetDirectory(targetDirectory);
		final List<Software> softwares = service.analyze();

		for (final Software software : softwares) {
			softwareData.add(software);
		}

		notifyDataChanged();
	}

	public void applyAnalysis() {

	}

	public ObservableList<Software> getSoftwareData() {
		return softwareData;
	}

	// *************************************************************************
	// Listeners
	// *************************************************************************
	public void addListener(final ModelListener listener) {
		listeners.add(ModelListener.class, listener);
	}

	public void removeListener(final ModelListener listener) {
		listeners.remove(ModelListener.class, listener);
	}

	public ModelListener[] getListeners() {
		return listeners.getListeners(ModelListener.class);
	}

	// *************************************************************************
	// Listeners Notifications
	// *************************************************************************
	private void notifyDataChanged() {
		for (final ModelListener listener : getListeners()) {
			listener.onDataChanged();
		}
	}
}
