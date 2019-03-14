package com.github.setvisible.messorganizer.core;

import java.io.File;
import java.util.List;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public final class SimilarityService extends Service<List<Software>> {

	private File sourceDirectory = null;
	private File targetDirectory = null;

	public SimilarityService() {

	}

	@Override
	protected Task<List<Software>> createTask() {
		return new SimilarityAnalysisTask();
	}

	private class SimilarityAnalysisTask extends Task<List<Software>> {
		@Override
		protected List<Software> call() throws Exception {
			return Similarity.findSimilarities(sourceDirectory, targetDirectory);
		}
	}

	public File getSourceDirectory() {
		return sourceDirectory;
	}

	public void setSourceDirectory(final File sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}

	public File getTargetDirectory() {
		return targetDirectory;
	}

	public void setTargetDirectory(final File targetDirectory) {
		this.targetDirectory = targetDirectory;
	}
}
