package org.h0110w.somclient.scenes.main_scene;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import org.h0110w.somclient.appearance.Theme;
import org.h0110w.somclient.scenes.common.AbstractNode;
import org.h0110w.somclient.scenes.common.AbstractScene;
import org.h0110w.somclient.scenes.PrimaryStage;

import java.util.Arrays;

public class MainScene extends AbstractScene<MainScene> {
    protected AnchorPane mainPane;
    protected MainSceneHeader header;
    protected MainSceneDrawer drawer;
    protected MainSceneWorkingSpace workingSpace;

    protected BooleanProperty isSliderHidden = new SimpleBooleanProperty(true);

    public MainScene(PrimaryStage primaryStage, Theme theme) {
        super(primaryStage, theme);
    }


    @Override
    public MainScene initialize() {
        mainPane = initMainPane();
        header = new MainSceneHeader(this);
        workingSpace = new MainSceneWorkingSpace(this);
        drawer = new MainSceneDrawer(this);

        initNodes();

        mainPane.getChildren().addAll(
                header.getNode(),
                drawer.getNode(),
                workingSpace.getNode()
        );

        scene = new Scene(mainPane, 800, 600);
        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            }
        });
        return this;
    }

    private void initNodes() {
        for (AbstractNode node : Arrays.asList(header, drawer, workingSpace)) {
            node.initialize();
        }
    }

    private AnchorPane initMainPane() {
        mainPane = new AnchorPane();
        return mainPane;
    }

}
