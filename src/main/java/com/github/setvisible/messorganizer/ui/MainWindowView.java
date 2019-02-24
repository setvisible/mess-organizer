package com.github.setvisible.messorganizer.ui;

import java.util.ResourceBundle;

import com.airhacks.afterburner.views.FXMLView;

public class MainWindowView extends FXMLView {

	public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("locale.en_US");

	public MainWindowView() {
		this.bundle = RESOURCE_BUNDLE;
	}

	public MainWindowView(ResourceBundle bundle) {
		this.bundle = bundle;
	}
}
