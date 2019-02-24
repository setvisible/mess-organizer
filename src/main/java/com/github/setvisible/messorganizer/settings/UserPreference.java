package com.github.setvisible.messorganizer.settings;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.event.EventListenerList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserPreference {

	private static final String STR_NODE_NAME = "user";

	private static final Logger logger = LoggerFactory.getLogger(UserPreference.class);

	private final EventListenerList listeners = new EventListenerList();

	private String sourceDirectory = "";
	private String targetDirectory = "";

	// ************************************************************************
	// Persistence
	// ************************************************************************
	public void clearUserPreference() {
		final Preferences prefs = Preferences.userNodeForPackage(this.getClass()).node(STR_NODE_NAME);

		try {
			prefs.clear();
			this.readUserPreference();

		} catch (final BackingStoreException ex) {
			logger.error(ex.getMessage());
		}
	}

	public void readUserPreference() {
		final Preferences prefs = Preferences.userNodeForPackage(this.getClass()).node(STR_NODE_NAME);

		setSourceDirectory(prefs.get("source_directory", ""));
		setTargetDirectory(prefs.get("target_directory", ""));
	}

	public void writeUserPreference() {
		assert sourceDirectory != null;
		assert targetDirectory != null;
		final Preferences prefs = Preferences.userNodeForPackage(this.getClass()).node(STR_NODE_NAME);

		prefs.put("source_directory", sourceDirectory);
		prefs.put("target_directory", targetDirectory);
	}

	// ************************************************************************
	// Getters/Setters
	// ************************************************************************
	public String getSourceDirectory() {
		return sourceDirectory;
	}

	public void setSourceDirectory(final String sourceDirectory) {
		if (this.sourceDirectory != sourceDirectory) {
			this.sourceDirectory = sourceDirectory;
			this.notifySourceDirectoryChanged();
		}
	}

	// ************************************************************************
	public String getTargetDirectory() {
		return targetDirectory;
	}

	public void setTargetDirectory(final String targetDirectory) {
		if (this.targetDirectory != targetDirectory) {
			this.targetDirectory = targetDirectory;
			this.notifyTargetDirectoryChanged();
		}
	}

	// *************************************************************************
	// Listeners
	// *************************************************************************
	public void addListener(final UserPreferenceListener listener) {
		listeners.add(UserPreferenceListener.class, listener);
	}

	public void removeListener(final UserPreferenceListener listener) {
		listeners.remove(UserPreferenceListener.class, listener);
	}

	public UserPreferenceListener[] getListeners() {
		return listeners.getListeners(UserPreferenceListener.class);
	}

	// *************************************************************************
	// Listeners Notifications
	// *************************************************************************
	private void notifySourceDirectoryChanged() {
		for (final UserPreferenceListener listener : getListeners()) {
			listener.onSourceDirectoryChanged();
		}
	}

	private void notifyTargetDirectoryChanged() {
		for (final UserPreferenceListener listener : getListeners()) {
			listener.onTargetDirectoryChanged();
		}
	}
}
