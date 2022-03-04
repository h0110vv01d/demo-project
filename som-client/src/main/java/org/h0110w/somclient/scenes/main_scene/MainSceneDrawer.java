package org.h0110w.somclient.scenes.main_scene;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.h0110w.somclient.appearance.Icons;
import org.h0110w.somclient.scenes.common.AbstractNode;
import org.h0110w.somclient.scenes.common.CustomHBoxButton;

public class MainSceneDrawer extends AbstractNode<AnchorPane, MainSceneDrawer> {
    private final MainScene mainScene;
    private final AnchorPane mainPane;
    private final BooleanProperty isSliderHidden;

    protected StackPane leftPane = new StackPane();
    protected CustomHBoxButton homeButton;
    protected CustomHBoxButton usersButton;
    protected StackPane rightPane = new StackPane();
    protected static final int LEFT_PANE_WIDTH = 52;
    protected static final int RIGHT_PANE_WIDTH = 100;
    protected DoubleProperty drawerHeight = new SimpleDoubleProperty();
    private Rectangle clipRectangle;
    private Timeline timelineLeft;
    private Timeline timelineRight;
    private static final int TOP_INSET_FROM_HEADER =10;

    public MainSceneDrawer(MainScene mainScene) {
        this.mainScene = mainScene;
        this.mainPane = mainScene.mainPane;
        this.isSliderHidden = mainScene.isSliderHidden;
        node = new AnchorPane();
    }

    @Override
    public MainSceneDrawer initialize() {
        drawerHeight.bind(mainPane.heightProperty().add(-MainSceneHeader.HEADER_HEIGHT));

        node.translateYProperty().set(MainSceneHeader.HEADER_HEIGHT);

        leftPane = initLeftPane();
        rightPane = initRightPane(); // slider

        setAnimation();

        node.getChildren().addAll(leftPane, rightPane);
        return this;
    }

    private StackPane initLeftPane() {
        leftPane = new StackPane();
        leftPane.setId("mainDrawerLeftPane");

        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(LEFT_PANE_WIDTH);
        rectangle.heightProperty().bind(drawerHeight);
        rectangle.setFill(Color.TRANSPARENT);

        VBox actions = new VBox();
        actions.setPadding(new Insets(TOP_INSET_FROM_HEADER, 0, 10, 0));
        actions.getChildren()
                .addAll(homeButton(),
                        usersButton(),
                        testButton(),
                        themesButton()
                );
        actions.setMaxWidth(LEFT_PANE_WIDTH);

        leftPane.setMinWidth(LEFT_PANE_WIDTH);

        leftPane.getChildren().addAll(rectangle, actions);
        return leftPane;
    }

    private StackPane initRightPane() {
        rightPane = new StackPane();
        rightPane.setId("mainDrawerRightPane");

        Rectangle rightRectangle = new Rectangle();

        rightRectangle.setWidth(RIGHT_PANE_WIDTH);
        rightRectangle.heightProperty().bind(drawerHeight);
        rightRectangle.setFill(Color.TRANSPARENT);

        VBox labels = new VBox();
        labels.setPadding(new Insets(TOP_INSET_FROM_HEADER, 10, 10, 10));
        labels.getChildren()
                .addAll(homeButtonLabel(),
                        usersButtonLabel(),
                        new CustomHBoxButton(new Label("test")).initialize().getNode(),
                        themesLabel()
                );

        rightPane.setPrefWidth(RIGHT_PANE_WIDTH);

        setAnimation();

        rightPane.getChildren().addAll(rightRectangle, labels);
        return rightPane;
    }


    private void setAnimation() {
        createClipRectangle();
        createTimelines();
    }

    private void createClipRectangle() {
        clipRectangle = new Rectangle();
        clipRectangle.setWidth(0);
        clipRectangle.heightProperty().bind(drawerHeight);
        clipRectangle.translateXProperty().set(RIGHT_PANE_WIDTH);

        rightPane.setClip(clipRectangle);
        rightPane.translateXProperty().set(-LEFT_PANE_WIDTH);
    }

    private void createTimelines() {
        AnchorPane workingSpace = mainScene.workingSpace.getNode();

        timelineLeft = new Timeline();
        timelineRight = new Timeline();

        Duration animationDuration = Duration.millis(100);

        timelineRight.setCycleCount(1);
        timelineRight.setAutoReverse(true);
        final KeyValue kvRight1 = new KeyValue(clipRectangle.widthProperty(), RIGHT_PANE_WIDTH);
        final KeyValue kvRight2 = new KeyValue(clipRectangle.translateXProperty(), 0);
        final KeyValue kvRight3 = new KeyValue(rightPane.translateXProperty(), LEFT_PANE_WIDTH);
        final KeyValue kvRight4 = new KeyValue(workingSpace.translateXProperty(),
                LEFT_PANE_WIDTH + RIGHT_PANE_WIDTH);
        final KeyFrame kfRight = new KeyFrame(animationDuration,
                kvRight1, kvRight2, kvRight3, kvRight4);
        timelineRight.getKeyFrames().add(kfRight);

        timelineLeft.setCycleCount(1);
        timelineLeft.setAutoReverse(true);
        final KeyValue kvLeft1 = new KeyValue(clipRectangle.widthProperty(), 0);
        final KeyValue kvLeft2 = new KeyValue(clipRectangle.translateXProperty(), RIGHT_PANE_WIDTH);
        final KeyValue kvLeft3 = new KeyValue(rightPane.translateXProperty(), -LEFT_PANE_WIDTH);
        final KeyValue kvLeft4 = new KeyValue(workingSpace.translateXProperty(), LEFT_PANE_WIDTH);
        final KeyFrame kfLeft = new KeyFrame(animationDuration,
                kvLeft1, kvLeft2, kvLeft3, kvLeft4);
        timelineLeft.getKeyFrames().add(kfLeft);
    }

    private Node homeButton() {
        homeButton = new CustomHBoxButton(Icons.HOME).initialize();

        homeButton.getNode().setOnMouseClicked(mainScene.workingSpace.getEventHandlerForHome());

        return homeButton.getNode();
    }

    private Node homeButtonLabel() {
        CustomHBoxButton homeButtonLabel = getButtonLabel("Home");

        homeButtonLabel.getNode().setOnMouseClicked(mainScene.workingSpace.getEventHandlerForHome());

        return homeButtonLabel.getNode();
    }

    private Node usersButton() {
        usersButton = new CustomHBoxButton(Icons.USERS).initialize();

        usersButton.getNode().setOnMouseClicked(mainScene.workingSpace.getEventHandlerForUsers());

        return usersButton.getNode();
    }

    private Node usersButtonLabel() {
        CustomHBoxButton homeButtonLabel = getButtonLabel("Users");

        homeButtonLabel.getNode().setOnMouseClicked(mainScene.workingSpace.getEventHandlerForUsers());

        return homeButtonLabel.getNode();
    }

    private CustomHBoxButton getButtonLabel(String labelText) {
        Label label = new Label();
        label.setPrefHeight(30);
        label.setText(labelText);
        label.setFont(new Font("Tahoma", 20));

        CustomHBoxButton button = new CustomHBoxButton(label).initialize();
        button.getNode().setAlignment(Pos.CENTER_LEFT);

        return button;
    }

    private Node testButton() {
        CustomHBoxButton button = new CustomHBoxButton(new Label("test")).initialize();
        button.getNode().setOnMouseClicked(event -> {
            System.out.println("test button pressed");
            System.out.println(mainPane.heightProperty().get());
            System.out.println(drawerHeight.get());
        });
        return button.getNode();
    }

    private Node themesButton() {
        CustomHBoxButton themeButton = new CustomHBoxButton(Icons.PALETTE).initialize();
        themeButton.getNode().setOnMouseClicked(mainScene.workingSpace.getEventHandlerForThemes());
        return getBottomDrawerVBox(themeButton.getNode());
    }

    private Node themesLabel() {
        CustomHBoxButton themesLabel = getButtonLabel("Themes");

        themesLabel.getNode().setOnMouseClicked(mainScene.workingSpace.getEventHandlerForThemes());

        return getBottomDrawerVBox(themesLabel.getNode());
    }

    private VBox getBottomDrawerVBox(Node node) {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0, 0, 10, 0));

        vBox.prefHeightProperty().bind(drawerHeight.add(-TOP_INSET_FROM_HEADER-3*CustomHBoxButton.HEIGHT));

        vBox.setAlignment(Pos.BOTTOM_CENTER);

        vBox.getChildren().add(node);
        return vBox;
    }

    public EventHandler<MouseEvent> getEventHandlerForBurger() {
        return mouseEvent -> {
            final boolean isAnimationRunning = timelineLeft.getStatus().equals(Animation.Status.RUNNING) ||
                    timelineRight.getStatus().equals(Animation.Status.RUNNING);
            if (isAnimationRunning) {
                return;
            } else if (isSliderHidden.get()) {
                timelineRight.play();
                isSliderHidden.set(false);
            } else {
                timelineLeft.play();
                isSliderHidden.set(true);
            }
        };

    }
}
