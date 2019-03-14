package com.github.setvisible.messorganizer.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public final class Similarity {

	private final static Logger logger = LoggerFactory.getLogger(Similarity.class);

	private Similarity() {
	}

	public static List<Software> findSimilarities(final File sourceDirectory, final File targetDirectory) {

		final List<Software> softwares = new ArrayList<>();

		if (sourceDirectory == null || targetDirectory == null) {
			return softwares;
		}
		if (!sourceDirectory.exists() || !sourceDirectory.isDirectory()) {
			return softwares;
		}

		final String[] extensions = new String[] { "exe", "zip" };

		final Collection<File> sourcePaths = FileUtils.listFiles(sourceDirectory, extensions, true);
		final Collection<File> targetPaths = FileUtils.listFiles(targetDirectory, extensions, true);

		for (final File path : sourcePaths) {
			final Software software = new Software();

			software.setFileName(FilenameUtils.getName(path.getAbsolutePath()));
			software.setFullFileName(path.getAbsolutePath());

			final long size = FileUtils.sizeOf(path);
			software.setFileSize(FileUtils.byteCountToDisplaySize(size));

			final ImageIcon icon = (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(path);
			final BufferedImage image = (BufferedImage) icon.getImage();
			final Image fxIcon = SwingFXUtils.toFXImage(image, null);
			software.setFileIcon(fxIcon);

			try {
				final BasicFileAttributes attr = Files.readAttributes(path.toPath(), BasicFileAttributes.class);
				software.setFileCreationTime(attr.creationTime());
				software.setFileLastAccessTime(attr.lastAccessTime());
				software.setFileLastModifiedTime(attr.lastModifiedTime());
			} catch (final IOException ex) {
				logger.error(ex.getMessage());
			}

			software.setVersionIdentifier("x.x.x");
			software.setVersionDate(LocalDate.of(1999, 2, 21));

			softwares.add(software);
		}

		for (final Software software : softwares) {

			for (final File targetPath : targetPaths) {

				final String targetFileName = FilenameUtils.getName(targetPath.getAbsolutePath());
				final String filename = software.getFileName();

				final double similary = Similarity.fileNameSimilarity(filename, targetFileName);

				if (similary > 0.9 && similary > software.getSimilarity()) {
					software.setDecision(Decision.REPLACE);
					software.setDestinationPathName(targetPath.toString());
					software.setSimilarity(similary);
				}
			}

		}

		return softwares;
	}

	protected static double fileNameSimilarity(final String s1, final String s2) {

		final String ext1 = FilenameUtils.getExtension(s1);
		final String ext2 = FilenameUtils.getExtension(s2);
		if (!ext1.equals(ext2)) {
			return 0.0;
		}
		final String baseName1 = simplify(FilenameUtils.getBaseName(s1));
		final String baseName2 = simplify(FilenameUtils.getBaseName(s2));
		return similarity(baseName1, baseName2);
	}

	private static String simplify(String baseName) {
		baseName = baseName.toLowerCase();
		baseName = baseName.replaceAll("version", "v");
		baseName = baseName.replaceAll("[0-9]", "X");
		return baseName;
	}

	/**
	 * Calculates the similarity (a number within 0 and 1) between two strings.
	 */
	protected static double similarity(final String s1, final String s2) {

		// TODO could use org.apache.commons.text.similarity ?

		String longer = s1;
		String shorter = s2;
		if (s1.length() < s2.length()) { // longer should always have greater length
			longer = s2;
			shorter = s1;
		}

		final int longerLength = longer.length();
		if (longerLength == 0) {
			return 1.0;
			/* both strings are zero length */
		}

		// If you have Apache Commons Text, you can use it to calculate the edit
		// distance:
		// LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
		// return (longerLength - levenshteinDistance.apply(longer, shorter)) / (double)
		// longerLength;
		return (longerLength - editDistance(longer, shorter)) / (double) longerLength;
	}

	// Example implementation of the Levenshtein Edit Distance
	// See http://rosettacode.org/wiki/Levenshtein_distance#Java
	private static int editDistance(String s1, String s2) {
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();

		final int[] costs = new int[s2.length() + 1];
		for (int i = 0; i <= s1.length(); i++) {
			int lastValue = i;
			for (int j = 0; j <= s2.length(); j++) {
				if (i == 0)
					costs[j] = j;
				else {
					if (j > 0) {
						int newValue = costs[j - 1];
						if (s1.charAt(i - 1) != s2.charAt(j - 1))
							newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
						costs[j - 1] = lastValue;
						lastValue = newValue;
					}
				}
			}
			if (i > 0)
				costs[s2.length()] = lastValue;
		}
		return costs[s2.length()];
	}
}
