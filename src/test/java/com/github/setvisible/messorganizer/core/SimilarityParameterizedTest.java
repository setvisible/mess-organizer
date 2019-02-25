package com.github.setvisible.messorganizer.core;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class SimilarityParameterizedTest {

	@Parameterized.Parameters(name = "{index}: Test similarity between {0} and {1}, result: {2}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] { { "", "", 1.000 }, //
				{ "1234567890", "1", 0.100 }, //
				{ "1234567890", "123", 0.300 }, //
				{ "1234567890", "1234567", 0.700 }, //
				{ "1234567890", "1234567890", 1.000 }, //
				{ "1234567890", "1234567980", 0.800 }, //
				{ "47/2010", "472010", 0.857 }, //
				{ "47/2010", "472011", 0.714 }, //
				{ "47/2010", "AB.CDEF", 0.000 }, //
				{ "47/2010", "4B.CDEFG", 0.125 }, //
				{ "47/2010", "AB.CDEFG", 0.000 }, //
				{ "The quick fox jumped", "The fox jumped", 0.700 }, //
				{ "The quick fox jumped", "The fox", 0.350 }, //
				{ "kitten", "sitting", 0.571 } //
		});
	}

	private final String s1;
	private final String s2;
	private final double expected;

	public SimilarityParameterizedTest(String s1, String s2, double expected) {
		this.s1 = s1;
		this.s2 = s2;
		this.expected = expected;
	}

	@Test
	public void testLocateResults() {
		Assert.assertEquals(expected, Similarity.similarity(s1, s2), 0.001);
	}
}
