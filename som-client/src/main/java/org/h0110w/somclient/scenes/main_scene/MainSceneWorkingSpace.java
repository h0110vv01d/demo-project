package org.h0110w.somclient.scenes.main_scene;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.h0110w.somclient.scenes.common.AbstractNode;
import org.h0110w.somclient.scenes.main_scene.working_space_nodes.WSPlaceholder;
import org.h0110w.somclient.scenes.main_scene.working_space_nodes.WSThemes;
import org.h0110w.somclient.scenes.main_scene.working_space_nodes.WSUsers;

public class MainSceneWorkingSpace extends AbstractNode<AnchorPane, MainSceneWorkingSpace> {
    private final MainScene mainScene;
    private final AnchorPane mainPane;
    private final DoubleProperty boxWidth = new SimpleDoubleProperty();
    private final DoubleProperty boxHeight = new SimpleDoubleProperty();

    private final WSPlaceholder placeholder;
    private final WSThemes themes;
    private final WSUsers users = new WSUsers().initialize();

    public MainSceneWorkingSpace(MainScene mainScene) {
        this.mainScene = mainScene;
        this.mainPane = mainScene.mainPane;
        this.placeholder = new WSPlaceholder().initialize();
        this.themes = new WSThemes(mainScene.primaryStage).initialize();
        this.node = new AnchorPane();
    }

    @Override
    public MainSceneWorkingSpace initialize() {
        node.setId("workingSpace");
//        node.setPadding(new Insets(2, 0, 0, 0));
        node.setStyle("-fx-background-style: black");
        setBindings();

        node.getChildren().add(placeholder.getNode());
        return this;
    }


    public void setBindings() {
        // X
        node.translateXProperty().set(MainSceneDrawer.LEFT_PANE_WIDTH);

        boxWidth.bind(mainPane.widthProperty());
        boxWidth.add(mainScene.drawer.rightPane.translateXProperty());
        boxWidth.add(-MainSceneDrawer.RIGHT_PANE_WIDTH);
        node.prefWidthProperty().bind(boxWidth);

        // Y
        node.translateYProperty().set(MainSceneHeader.HEADER_HEIGHT);

        boxHeight.bind(mainPane.heightProperty());
        boxHeight.add(-MainSceneHeader.HEADER_HEIGHT);
        node.prefHeightProperty().bind(boxHeight);

        placeholder.getNode().prefHeightProperty().bind(boxHeight);
        placeholder.getNode().prefWidthProperty().bind(boxWidth);
    }

    public EventHandler<MouseEvent> getEventHandlerForHome() {
        return mouseEvent -> replaceNode(placeholder);
    }

    public EventHandler<MouseEvent> getEventHandlerForUsers() {
        return mouseEvent -> replaceNode(users);
    }

    public EventHandler<MouseEvent> getEventHandlerForThemes() {
        return mouseEvent -> replaceNode(themes);
    }

    private <T extends AbstractNode<? extends Node, T>> void replaceNode(T abstractNode) {
        if (node.getChildren().get(0).equals(abstractNode.getNode())){
            return;
        }
        node.getChildren().remove(0);
        node.getChildren().add(abstractNode.getNode());
    }
}
