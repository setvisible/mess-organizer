package com.github.setvisible.messorganizer.core;

public enum Decision {

	MOVE("Move"), //
	REPLACE("Replace"), //
	DONT_MOVE("Don't Move");

	private final String value;

	private Decision(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
