package com.github.setvisible.messorganizer.core;

import java.util.EventListener;

public interface ModelListener extends EventListener {

	/** This event is triggered whenever the model is computing data. */
	void onProcessing();

	/** This event is triggered whenever the model has finished computing data. */
	void onProcessFinished();

	/** This event is triggered whenever the model data has changed. */
	void onDataChanged();
}
