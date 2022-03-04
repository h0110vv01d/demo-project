package org.h0110w.somclient.scenes.tests;

import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.h0110w.somclient.appearance.Theme;
import org.h0110w.somclient.scenes.common.AbstractScene;
import org.h0110w.somclient.scenes.PrimaryStage;
import org.h0110w.somclient.service.ServiceFacade;

public class TestSceneWithApi extends AbstractScene {


    protected TestSceneWithApi(PrimaryStage primaryStage, Theme theme) {
        super(primaryStage, theme);
    }

    @Override
    public TestSceneWithApi initialize() {
        VBox vBox = new VBox();
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.prefHeightProperty().bind(vBox.heightProperty());
        Label label = new Label("label");

        Button button1 = new Button("secured ");
        button1.setOnAction(actionEvent ->
                label.setText(ServiceFacade.helloService.getSecuredHello().toString()));

        Button button2 = new Button("public mess");
        button2.setOnAction(actionEvent ->
                label.setText(ServiceFacade.helloService.getPublicMessage().toString()));

        Button button3 = new Button("user mess ");
        button3.setOnAction(actionEvent ->
                label.setText(ServiceFacade.helloService.getUserMessage().toString()));

        Button button4 = new Button("log out");
        button4.setOnAction(actionEvent -> {
            ServiceFacade.authService.logout();
            primaryStage.changeScene();
        });
        GridPane gridPane = new GridPane();
        gridPane.addRow(0, button1);
        gridPane.addRow(1, button2);
        gridPane.addRow(2, button3);
        gridPane.addRow(3, button4);

//        ButtonBar buttonBar = new ButtonBar();
//        buttonBar.getButtons().addAll(button1, button2, button3);


        AnchorPane pane1 = new AnchorPane();
        pane1.getChildren().add(gridPane);

        pane1.prefHeightProperty().bind(splitPane.heightProperty());

        AnchorPane pane2 = new AnchorPane();
        pane2.getChildren().add(label);

        splitPane.getItems().addAll(pane1, pane2);

        vBox.getChildren().add(splitPane);

        scene = new Scene(vBox, 320, 240);
        return this;
    }
}
