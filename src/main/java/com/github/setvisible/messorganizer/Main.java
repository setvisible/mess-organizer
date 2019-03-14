package com.github.setvisible.messorganizer;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;

public final class Main {

	private final static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(final String[] args) {

		BasicConfigurator.configure();

		try {
			Application.launch(MainApplication.class, args);

		} catch (final Exception ex) {
			logger.error("an exception was thrown", ex);
		}
	}
}
