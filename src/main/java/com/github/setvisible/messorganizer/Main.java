package com.github.setvisible.messorganizer;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		MainApplication app = new MainApplication();
		app.start(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
