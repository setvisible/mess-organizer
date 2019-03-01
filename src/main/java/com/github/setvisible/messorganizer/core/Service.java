package com.github.setvisible.messorganizer.core;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;


public final class Service {

	private final Logger logger = LoggerFactory.getLogger(Service.class);

	private File sourceDirectory = null;
	private File targetDirectory = null;

	public List<Software> analyze() {

		final List<Software> softwares = new ArrayList<>();

		if (sourceDirectory == null || targetDirectory == null) {
			return softwares;
		}
		if (!sourceDirectory.exists() || !sourceDirectory.isDirectory()) {
			return softwares;
		}

		final List<Path> sourcePaths = new ArrayList<>();
		final List<Path> targetPaths = new ArrayList<>();

		try (Stream<Path> paths = Files.walk(Paths.get(sourceDirectory.getPath()))) {
			paths.filter(Files::isRegularFile).forEach(path -> sourcePaths.add(path));

		} catch (final IOException ex) {
			logger.error(ex.getMessage());
		}

		try (Stream<Path> paths = Files.walk(Paths.get(targetDirectory.getPath()))) {
			paths.filter(Files::isRegularFile).forEach(path -> targetPaths.add(path));

		} catch (final IOException ex) {
			logger.error(ex.getMessage());
		}

		for (final Path path : sourcePaths) {
			final Software software = new Software();
			software.setFileName(path.getFileName().toString());
			software.setFullFileName(path.toString());

			final long size = FileUtils.sizeOf(path.toFile());
			software.setFileSize(FileUtils.byteCountToDisplaySize(size));

			final ImageIcon icon = (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(path.toFile());
			final BufferedImage image = (BufferedImage) icon.getImage();
			final Image fxIcon = SwingFXUtils.toFXImage(image, null);
			software.setFileIcon(fxIcon);

			try {
				final BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
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

			for (final Path targetPath : targetPaths) {

				final String targetFileName = targetPath.getFileName().toString();
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
