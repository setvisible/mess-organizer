package com.github.setvisible.messorganizer.ui;

import java.util.ResourceBundle;

import com.airhacks.afterburner.views.FXMLView;

public class BodyView extends FXMLView {

	public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("locale.en_US");

	public BodyView() {
		this.bundle = RESOURCE_BUNDLE;
	}

	public BodyView(ResourceBundle bundle) {
		this.bundle = bundle;
	}
}
