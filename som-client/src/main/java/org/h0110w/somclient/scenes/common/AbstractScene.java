package org.h0110w.somclient.scenes.common;

import javafx.scene.Node;
import javafx.scene.Scene;
import org.h0110w.somclient.appearance.Theme;
import org.h0110w.somclient.scenes.PrimaryStage;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractScene<T> {
    public final PrimaryStage primaryStage;
    protected Scene scene;
    protected List<Node> nodes = new ArrayList<>();
    protected Theme theme;

    protected AbstractScene(PrimaryStage primaryStage, Theme theme) {
        this.primaryStage = primaryStage;
        this.theme = theme;
    }

    protected abstract T initialize();

    public void changeTheme(Theme theme) {
        scene.getRoot().getStylesheets().remove(this.theme.getCSSPath());
        this.theme = theme;
        scene.getRoot().getStylesheets().add(theme.getCSSPath());
    }

    public void applyStyleToNodes(String css) {
        for (Node node : nodes) {
            node.setStyle(css);
        }
    }

    public Scene getScene() {
        return scene;
    }

    public Theme getTheme() {
        return theme;
    }
}
