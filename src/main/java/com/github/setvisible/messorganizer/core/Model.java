package com.github.setvisible.messorganizer.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.swing.event.EventListenerList;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.platform.FileUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;

public class Model {

	private final static Logger logger = LoggerFactory.getLogger(Model.class);

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

		logger.info("Analyzing...");
		notifyProcessing();

		final SimilarityService service = new SimilarityService();
		service.setSourceDirectory(sourceDirectory);
		service.setTargetDirectory(targetDirectory);

		service.setOnSucceeded((WorkerStateEvent event) -> {
			final List<Software> softwares = service.getValue();
			for (final Software software : softwares) {
				softwareData.add(software);
			}

			logger.info("Analysis done.");
			notifyProcessFinished();
			notifyDataChanged();

		});
		service.start();
	}

	// *************************************************************************
	public void applyAnalysis(final Software software) throws IOException {

		if (software.getDecision() == Decision.REPLACE) {
			final File destination = new File(software.getDestinationPathName());
			moveToTrash(destination);
		}

		if (software.getDecision() == Decision.MOVE || software.getDecision() == Decision.REPLACE) {

			// logger.info("Moving '" + software.getFileName() + "'...");

			final String path = FilenameUtils.getFullPath(software.getDestinationPathName());
			final String name = FilenameUtils.getName(software.getFullFileName());
			final File target = new File(path + name);
			moveToTrash(target);

			final File source = new File(software.getFullFileName());
			move(source, target);


			softwareData.remove(software);

			// logger.info("Moved.");
		}
	}

	public void applyAllAnalyses() throws IOException {
		int i = softwareData.size();
		while (i > 0) {
			i--;
			final Software software = softwareData.get(i);
			applyAnalysis(software);
		}
	}

	private static void move(final File source, final File target) throws IOException {
		moveToTrash(target);
		Files.move(source.toPath(), target.toPath(), StandardCopyOption.ATOMIC_MOVE);
	}

	private static void moveToTrash(final File target) throws IOException {
		if (target.exists() && target.isFile()) {
			final FileUtils fileUtils = FileUtils.getInstance();
			if (fileUtils.hasTrash()) {
				fileUtils.moveToTrash(new File[] { target });
			}
		}
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
