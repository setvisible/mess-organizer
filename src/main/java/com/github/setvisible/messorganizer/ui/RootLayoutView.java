package com.github.setvisible.messorganizer.ui;

import java.util.ResourceBundle;

import com.airhacks.afterburner.views.FXMLView;

public class RootLayoutView extends FXMLView {

	public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("locale.en_US");

	public RootLayoutView() {
		this.bundle = RESOURCE_BUNDLE;
	}

	public RootLayoutView(ResourceBundle bundle) {
		this.bundle = bundle;
	}
}
