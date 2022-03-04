package org.h0110w.somclient.scenes;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.h0110w.somclient.appearance.Theme;
import org.h0110w.somclient.appearance.Themes;
import org.h0110w.somclient.scenes.common.AbstractScene;
import org.h0110w.somclient.scenes.main_scene.MainScene;

import java.util.ArrayList;
import java.util.List;

public class PrimaryStage {
    private final Stage stage;
    private final List<AbstractScene> scenes = new ArrayList<>();
    private LoginScene loginScene;
    private MainScene mainScene;
    private boolean isLoginScene = true;


    public PrimaryStage(Stage stage) {
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        Theme defaultTheme = Themes.DARK_SOLARIZED;
        loginScene = new LoginScene(this, defaultTheme).initialize();
        mainScene = new MainScene(this, defaultTheme).initialize();

        scenes.add(loginScene);
        scenes.add(mainScene);

        changeThemeOnScenes(defaultTheme);
//        applyCSSForEachNodeOnEveryScene("-fx-font-size: 20;");

        stage.setTitle("SomApp");
        stage.setScene(mainScene.getScene());
        stage.setMaximized(true);
        stage.initStyle(StageStyle.UNDECORATED);
    }

    public void changeScene() {
        if (isLoginScene) {
            stage.setScene(mainScene.getScene());
            stage.setMaximized(true);
            isLoginScene = false;
        } else {
            stage.setScene(loginScene.getScene());
            isLoginScene = true;
        }
    }

    public void changeThemeOnScenes(Theme theme) {
        scenes.forEach(scene -> scene.changeTheme(theme));
    }

    public void applyCSSForEachNodeOnEveryScene(String css) {
        scenes.forEach(scene -> scene.applyStyleToNodes(css));
    }

    public Stage getStage() {
        return stage;
    }
}
