package com.github.setvisible.messorganizer.settings;

import java.util.EventListener;

public interface UserPreferenceListener extends EventListener {

	/** This event is triggered whenever the source directory has changed. */
	void onSourceDirectoryChanged();

	/** This event is triggered whenever the target directory has changed. */
	void onTargetDirectoryChanged();
}
