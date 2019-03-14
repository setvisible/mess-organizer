package com.github.setvisible.messorganizer.util;

import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Helper functions for handling dates.
 */
public class DateUtil {


	// Ex: "Fri Feb 22 23:28:31 2019 +0100"
	private static final String DEFAULT_TIME_PATTERN = "EEE MMM d hh:mm:ss yyyy Z";
	private static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";
	private static final String DEFAULT_DATE = "-/-/--";

	/** The date formatters. */
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(DEFAULT_TIME_PATTERN, Locale.US);
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);
	// TODO private static final DateTimeFormatter DATE_TIME_FORMATTER =
	// DateTimeFormatter.ISO_LOCAL_DATE ;


	/**
	 * Returns the given date as a well formatted String. The above defined
	 * {@link DateUtil#DEFAULT_DATE_PATTERN} is used.
	 *
	 * @param localDate the date to be returned as a string
	 * @return formatted string
	 */
	public static String format(final LocalDate localDate) {
		if (localDate != null) {
			return localDate.format(DATE_TIME_FORMATTER);
		}
		return DEFAULT_DATE;
	}

	public static String formatFileTime(final FileTime time) {
		if (time != null) {
			return DATE_FORMAT.format(time.toMillis()).toUpperCase();
		}
		return DEFAULT_DATE;
	}

	/**
	 * Converts a String in the format of the defined {@link DateUtil#DATE_PATTERN}
	 * to a {@link LocalDate} object.
	 *
	 * Returns null if the String could not be converted.
	 *
	 * @param dateString the date as String
	 * @return the date object or null if it could not be converted
	 */
	public static LocalDate parse(String dateString) {
		try {
			return DATE_TIME_FORMATTER.parse(dateString, LocalDate::from);
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	/**
	 * Checks the String whether it is a valid date.
	 *
	 * @param dateString
	 * @return true if the String is a valid date
	 */
	public static boolean validDate(String dateString) {
		// Try to parse the String.
		return DateUtil.parse(dateString) != null;
	}
}
