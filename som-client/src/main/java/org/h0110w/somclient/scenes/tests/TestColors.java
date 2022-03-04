package org.h0110w.somclient.scenes.tests;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class TestColors extends Application {
    //$base03:    #002b36;
    //$base02:    #073642;
    //$base01:    #586e75;
    //$base00:    #657b83;
    //$base0:     #839496;
    //$base1:     #93a1a1;
    //$base2:     #eee8d5;
    //$base3:     #fdf6e3;
    //$yellow:    #b58900;
    //$orange:    #cb4b16;
    //$red:       #dc322f;
    //$magenta:   #d33682;
    //$violet:    #6c71c4;
    //$blue:      #268bd2;
    //$cyan:      #2aa198;
    //$green:     #859900;
    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane anchorPane = new AnchorPane();
        VBox vBox = new VBox();
        vBox.prefHeightProperty().bind(anchorPane.heightProperty());
        vBox.prefWidthProperty().bind(anchorPane.widthProperty());

        List<String> colors = Arrays.asList("#002b36", "#073642",
                "#586e75",
                "#657b83",
                "#839496",
                "#8a8a8a",
                "#93a1a1",
                "#93a1a1");
        for (String color : colors) {
            vBox.getChildren().add(getColor(color));
        }

        anchorPane.getChildren().add(vBox);
        Scene scene = new Scene(anchorPane, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private Node getColor(String color) {
        HBox hBox = new HBox();
        hBox.setStyle("-fx-background-color: " + color);
        hBox.setPrefSize(100, 600);
        return hBox;
    }

    public static void main(String[] args) {
        launch();
    }
}
