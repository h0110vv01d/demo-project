package org.h0110w.somclient.scenes.common;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.h0110w.somclient.appearance.Theme;
import org.kordamp.ikonli.javafx.FontIcon;

public class CustomHBoxButton extends AbstractNode<HBox, CustomHBoxButton> {
    public static final int HEIGHT = 30;
    public static final int WIDTH = 30;
    private final Node buttonLabel;

    public CustomHBoxButton(String iconCode) {
        this.buttonLabel = new FontIcon(iconCode);
    }

    public CustomHBoxButton(Label label) {
        this.buttonLabel = label;
    }

    @Override
    public CustomHBoxButton initialize() {
        node = new HBox();

        node.setPrefSize(HEIGHT, WIDTH);
        node.setAlignment(Pos.CENTER);
        node.getChildren().add(buttonLabel);

        setPressAnimation();

        return this;
    }

    private void setPressAnimation() {
        node.setOnMousePressed(mouseEvent -> node.setStyle("-fx-background-color: " + Theme.CSS_BG2_NAME));
        node.setOnMouseReleased(mouseEvent -> node.setStyle("-fx-background-color: " + Theme.CSS_BG1_NAME));
    }
}
