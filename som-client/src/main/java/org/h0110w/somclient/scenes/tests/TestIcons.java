package org.h0110w.somclient.scenes.tests;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.h0110w.somclient.appearance.Theme;
import org.h0110w.somclient.appearance.Themes;
import org.kordamp.ikonli.javafx.FontIcon;

public class TestIcons extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Theme darkSolarized = Themes.DARK_SOLARIZED;

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-color: " + darkSolarized.getBackgroundColor1());

        FontIcon icon = new FontIcon("mdi-palette");
        icon.setIconSize(200);

        icon.setIconColor(Paint.valueOf(darkSolarized.getContentColor1()));

//        Button button = new Button("User Account");
//        button.setGraphic(icon);
//        button.setId("account-button");
        

        HBox hBox = new HBox(icon);
        hBox.setAlignment(Pos.CENTER);
        hBox.prefHeightProperty().bind(anchorPane.heightProperty());
        hBox.prefWidthProperty().bind(anchorPane.widthProperty());

        anchorPane.getChildren().add(hBox);
        Scene scene = new Scene(anchorPane, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
