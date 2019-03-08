package com.github.setvisible.messorganizer.core;

import org.apache.commons.io.FilenameUtils;

public final class Similarity {

	private Similarity() {
	}

	public static double fileNameSimilarity(String s1, String s2) {

		String ext1 = FilenameUtils.getExtension(s1);
		String ext2 = FilenameUtils.getExtension(s2);
		if (!ext1.equals(ext2)) {
			return 0.0;
		}
		String baseName1 = simplify(FilenameUtils.getBaseName(s1));
		String baseName2 = simplify(FilenameUtils.getBaseName(s2));
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
	public static double similarity(String s1, String s2) {

		// TODO could use org.apache.commons.text.similarity ?

		String longer = s1;
		String shorter = s2;
		if (s1.length() < s2.length()) { // longer should always have greater length
			longer = s2;
			shorter = s1;
		}

		int longerLength = longer.length();
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

		int[] costs = new int[s2.length() + 1];
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
