package org.h0110w.somclient.scenes.main_scene.working_space_nodes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import org.h0110w.somclient.appearance.Theme;
import org.h0110w.somclient.appearance.Themes;
import org.h0110w.somclient.scenes.PrimaryStage;
import org.h0110w.somclient.scenes.common.AbstractNode;

public class WSThemes extends AbstractNode<VBox, WSThemes> {
    private final PrimaryStage primaryStage;

    public WSThemes(PrimaryStage primaryStage) {
        this.primaryStage = primaryStage;
        node = new VBox();
    }

    @Override
    public WSThemes initialize() {
        node.setSpacing(10);
        node.setPadding(new Insets(20, 0, 20, 20));
        node.setAlignment(Pos.TOP_LEFT);

        for (Theme theme : Themes.getThemes()) {
            node.getChildren().add(getThemeSelectButton(theme));
        }
        node.setSpacing(20);
        return this;
    }

    private HBox getThemeSelectButton(Theme theme) {
        HBox selectOption = new HBox();
        HBox colors = new HBox();
        colors.setId("colors");

        theme.getColors().forEach(color -> {
            Rectangle rectangle = new Rectangle();
            rectangle.setWidth(30);
            rectangle.setHeight(30);
            rectangle.setFill(Paint.valueOf(color));
            colors.getChildren().add(rectangle);
        });

        HBox space = new HBox();
        space.setPrefWidth(50);

        Label label = new Label(theme.getName());
        label.setFont(new Font("Tahoma", 20));

        selectOption.getChildren().addAll(colors, space, label);
        selectOption.setOnMouseClicked(mouseEvent ->
                primaryStage.changeThemeOnScenes(theme));
        return selectOption;
    }
}
