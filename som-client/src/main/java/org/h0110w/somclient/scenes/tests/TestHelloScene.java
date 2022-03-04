package org.h0110w.somclient.scenes.tests;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.h0110w.somclient.appearance.Icons;

public class TestHelloScene extends Application {
    private HBox button;
    private Label label = new Label("default");
    private boolean isDef = true;

    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane anchorPane = new AnchorPane();
//        anchorPane.setStyle("-fx-background-color: #222244");

        VBox vBox = new VBox();
//        vBox.setStyle("-fx-background-color: #000044");
        vBox.setAlignment(Pos.CENTER);
        vBox.setPrefSize(600, 400);

        button = new HBox();
//        button.setStyle("-fx-background-color: #073642");
        button.setAlignment(Pos.CENTER);


        ColorAdjust colorAdjust = new ColorAdjust();
        Color targetColor = Color.GREEN;
        double hue = map((targetColor.getHue() + 180) % 360, 0, 360, -1, 1);
        colorAdjust.setHue(hue);

// use saturation as it is
        double saturation = targetColor.getSaturation();
        colorAdjust.setSaturation(saturation);

// we use WHITE in the masked ball creation => inverse brightness
        double brightness = map(targetColor.getBrightness(), 0, 1, -1, 0);
        colorAdjust.setBrightness(brightness);

        //TODO
        //###
        //### need to get some image
        //###
        ImageView imageView = new ImageView(); // here
        imageView.setEffect(colorAdjust);
        imageView.setCache(true);
        imageView.setCacheHint(CacheHint.SPEED);

        button.getChildren().add(imageView);

        button.setMinHeight(100);
        button.setMaxHeight(100);
        button.setMinWidth(100);
        button.setMaxWidth(100);
        button.setOnMousePressed(getPressed());
        button.setOnMouseClicked(getOnMouseClicked());
        button.setOnMouseReleased(getReleased());

        vBox.getChildren().addAll(button, label());
        anchorPane.getChildren().add(vBox);

        Scene scene = new Scene(anchorPane, 800, 600);
//        scene.getRoot().getStylesheets().add(Themes.DARK_SOLARIZED);
        stage.setScene(scene);
        stage.show();
    }

    private Node label() {
        HBox hBox = new HBox();
//        hBox.setStyle("-fx-background-color: #000055");
        hBox.setAlignment(Pos.CENTER);
        hBox.setMaxWidth(300);
        hBox.setPrefSize(300, 100);
        label.setFont(new Font("Tahoma", 20));
        hBox.getChildren().add(label);
        return hBox;
    }

    public static double map(double value, double start, double stop, double targetStart, double targetStop) {
        return targetStart + (targetStop - targetStart) * ((value - start) / (stop - start));
    }

    private EventHandler<? super MouseEvent> getReleased() {
        return (EventHandler<MouseEvent>) mouseEvent -> {
            button.setStyle("-fx-background-color: #002b36");
        };
    }

    private EventHandler<? super MouseEvent> getPressed() {
        return (EventHandler<MouseEvent>) mouseEvent -> {
            button.setStyle("-fx-background-color: #073642");
        };
    }

    private EventHandler<? super MouseEvent> getOnMouseClicked() {
        return (EventHandler<MouseEvent>) mouseEvent -> {
            if (isDef) {
                label.setText("not default");
                isDef = false;
            } else {
                label.setText("default");
                isDef = true;
            }
        };
    }

    public static void main(String[] args) {
        launch();
    }
}
