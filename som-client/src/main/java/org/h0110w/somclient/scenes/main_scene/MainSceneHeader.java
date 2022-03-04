package org.h0110w.somclient.scenes.main_scene;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.h0110w.somclient.appearance.Icons;
import org.h0110w.somclient.scenes.common.AbstractNode;
import org.h0110w.somclient.scenes.common.CustomHBoxButton;

public class MainSceneHeader extends AbstractNode<HBox, MainSceneHeader> {
    private final MainScene mainScene;
    private final AnchorPane mainPane;

    private CustomHBoxButton burgerButton;
    protected static final int HEADER_HEIGHT = 52;
    private double yOffset;
    private double xOffset;

    public MainSceneHeader(MainScene mainScene) {
        this.mainScene = mainScene;
        this.mainPane = mainScene.mainPane;
        this.node = new HBox();
    }

    @Override
    public MainSceneHeader initialize() {
        node.setId("mainHeader");
        node.setPrefHeight(HEADER_HEIGHT);
        node.prefWidthProperty().bind(mainPane.widthProperty());
        node.setPadding(new Insets(11, 15, 11, 9));

        setOnDrag();

        node.getChildren().addAll(burgerButton(), windowMenu());
        return this;
    }

    private void setOnDrag() {
        node.setOnMousePressed(event -> {
            xOffset = mainScene.primaryStage.getStage().getX() - event.getScreenX();
            yOffset = mainScene.primaryStage.getStage().getY() - event.getScreenY();
        });
        node.setOnMouseDragged(event -> {
            mainScene.primaryStage.getStage().setX(event.getScreenX() + xOffset);
            mainScene.primaryStage.getStage().setY(event.getScreenY() + yOffset);
        });
    }

    private Node windowMenu() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.prefWidthProperty().bind(node.widthProperty());
        hBox.getChildren().addAll(minimize(), maximize(), close());
        return hBox;
    }

    private Node burgerButton() {
        burgerButton = new CustomHBoxButton(Icons.BURGER).initialize();

        burgerButton.getNode().setOnMouseClicked(mainScene.drawer.getEventHandlerForBurger());
        return burgerButton.getNode();
    }

    private Node minimize() {
        CustomHBoxButton button = new CustomHBoxButton(Icons.MINIMIZE).initialize();

        button.getNode().setOnMouseClicked(mouseEvent -> mainScene.primaryStage.getStage().setIconified(true));

        return button.getNode();
    }

    private Node maximize() {
        CustomHBoxButton button = new CustomHBoxButton(Icons.MAXIMIZE).initialize();

        button.getNode().setOnMouseClicked(mouseEvent -> {
            Stage stage = mainScene.primaryStage.getStage();
            stage.setMaximized(!stage.isMaximized());
        });

        return button.getNode();
    }

    private Node close() {
        CustomHBoxButton button = new CustomHBoxButton(Icons.CLOSE).initialize();

        button.getNode().setOnMouseClicked(mouseEvent -> Platform.exit());

        return button.getNode();
    }
}
