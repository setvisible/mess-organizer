package com.github.setvisible.messorganizer.core;

import java.io.File;
import java.util.List;

import javax.swing.event.EventListenerList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;

public class Model {

	private final ObservableList<Software> softwareData = FXCollections.observableArrayList();

	private final EventListenerList listeners = new EventListenerList();

	// *************************************************************************
	public void clear() {
		softwareData.clear();
		notifyDataChanged();
	}

	public ObservableList<Software> getSoftwareData() {
		return softwareData;
	}

	// *************************************************************************
	/**
	 * Runs an asynchronous analysis.
	 */
	public void runAnalysis(final File sourceDirectory, final File targetDirectory) {
		clear();

		notifyProcessing();

		final SimilarityService service = new SimilarityService();
		service.setSourceDirectory(sourceDirectory);
		service.setTargetDirectory(targetDirectory);

		service.setOnSucceeded((WorkerStateEvent event) -> {
			final List<Software> softwares = service.getValue();
			for (final Software software : softwares) {
				softwareData.add(software);
			}

			notifyProcessFinished();
			notifyDataChanged();

		});
		service.start();
	}

	// *************************************************************************
	public void applyAnalysis(final Software software) throws IOException {
		// TODO
	}

	public void applyAllAnalyses() throws IOException {
		// TODO
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
	private void notifyProcessing() {
		for (final ModelListener listener : getListeners()) {
			listener.onProcessing();
		}
	}

	private void notifyProcessFinished() {
		for (final ModelListener listener : getListeners()) {
			listener.onProcessFinished();
		}
	}

	private void notifyDataChanged() {
		for (final ModelListener listener : getListeners()) {
			listener.onDataChanged();
		}
	}
}
