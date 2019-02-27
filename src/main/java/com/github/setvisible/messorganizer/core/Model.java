package com.github.setvisible.messorganizer.core;

import javax.swing.event.EventListenerList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {

	private final ObservableList<Software> softwareData = FXCollections.observableArrayList();

	private final EventListenerList listeners = new EventListenerList();


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
