package org.h0110w.somclient.scenes.main_scene.working_space_nodes;

import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import org.h0110w.somclient.model.User;
import org.h0110w.somclient.model.UserRole;
import org.h0110w.somclient.model.UserStatus;
import org.h0110w.somclient.scenes.common.AbstractNode;

import java.util.List;

public class WSUsers extends AbstractNode<AnchorPane, WSUsers> {
    @Override
    public WSUsers initialize() {
        node = new AnchorPane();
        List<User> users = List.of(new User("asdfa", "admin", UserRole.ADMIN, UserStatus.ACTIVE, true));
        TableView<User> table = new TableView<>(FXCollections.observableList(users));
        node.getChildren().add(table);
        return this;
    }
}
