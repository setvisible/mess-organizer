package com.github.setvisible.messorganizer.ui.widget;

import java.util.ResourceBundle;

import com.airhacks.afterburner.views.FXMLView;

public class DetailPanelView extends FXMLView {

	public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("locale.en_US");

	public DetailPanelView() {
		this.bundle = RESOURCE_BUNDLE;
	}

	public DetailPanelView(ResourceBundle bundle) {
		this.bundle = bundle;
	}
}
