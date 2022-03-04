package org.h0110w.somclient;

import javafx.application.Application;
import javafx.stage.Stage;
import org.h0110w.somclient.scenes.PrimaryStage;

import java.io.IOException;

public class SomClientApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        PrimaryStage primaryStage = new PrimaryStage(stage);
        primaryStage.getStage().show();
    }

    public static void main(String[] args) {
        launch();
    }
}