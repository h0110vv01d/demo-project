package org.h0110w.somclient.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import org.h0110w.somclient.appearance.Theme;
import org.h0110w.somclient.exception.ServiceException;
import org.h0110w.somclient.scenes.common.AbstractScene;
import org.h0110w.somclient.service.ServiceFacade;

import java.io.IOException;
import java.net.ConnectException;

@Slf4j
public class LoginScene extends AbstractScene<LoginScene> {
    private final Label loginLabel = new Label("Login");
    private final TextField loginField = new TextField();

    private final Label passLabel = new Label("Password");
    private final PasswordField passwordField = new PasswordField();

    private Button loginButton;

    final Label statusMessage = new Label("");

    protected LoginScene(PrimaryStage primaryStage, Theme theme) {
        super(primaryStage, theme);
    }


    @Override
    public LoginScene initialize() {
        defineNodes();
        GridPane mainGrid = createGrid();
        scene = new Scene(mainGrid, 500, 350);
        return this;
    }

    private void defineNodes() {
        nodes.add(loginLabel);
        nodes.add(loginField);
        nodes.add(loginButton());
        nodes.add(passLabel);
        nodes.add(passwordField);
    }

    private GridPane createGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("SomApplication");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
        HBox hTitle = new HBox(10);
        hTitle.setAlignment(Pos.CENTER);
        hTitle.getChildren().add(scenetitle);
        grid.add(hTitle, 0, 0, 3, 1);

        grid.add(loginLabel, 0, 1);
        grid.add(loginField, 1, 1);

        grid.add(passLabel, 0, 2);
        grid.add(passwordField, 1, 2);

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(loginButton());
        grid.add(hbBtn, 1, 4);

        grid.add(statusMessage, 1, 6);

        loginField.setText("som_admin");
        passwordField.setText("123qwe");
        return grid;
    }


    private Button loginButton() {
        if (loginButton == null) {
            loginButton = new Button("Login");
            loginButton.setOnAction(actionEvent -> requestToken());
        }
        nodes.add(loginButton);
        return loginButton;
    }

    private boolean areFieldsValid() {
        return !loginField.getText().isEmpty() &&
                !passwordField.getText().isEmpty();
    }

    private void requestToken() {
        if (!areFieldsValid()) {
            statusMessage.setText("Invalid credentials");
            return;
        }

        final boolean isAuthenticated = tryToAuth();
        if (isAuthenticated) {
            primaryStage.changeScene();
        }
    }

    private boolean tryToAuth() {
        boolean isSuccess = false;
        String message = "error";
        try {
            ServiceFacade.authService.authenticate(loginField.getText(),
                    passwordField.getText());
            message = "Success";
            isSuccess = true;
        } catch (ConnectException e) {
            log.error(e.getMessage());
            message = "no connection to host";
        } catch (IOException e) {
            log.error("token IOException");
        } catch (ServiceException exception) {
            log.error("token 401:" + exception.getMessage());
            message = exception.getMessage();
        }
        statusMessage.setText(message);
        return isSuccess;
    }
}
