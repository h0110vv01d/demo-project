package org.h0110w.somclient.scenes.main_scene.working_space_nodes;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import org.h0110w.somclient.appearance.Icons;
import org.h0110w.somclient.scenes.common.AbstractNode;
import org.kordamp.ikonli.javafx.FontIcon;

public class WSPlaceholder extends AbstractNode<HBox, WSPlaceholder> {
    @Override
    public WSPlaceholder initialize() {
        node = new HBox();
        node.setId("placeholder");

        node.setAlignment(Pos.CENTER);

        FontIcon icon = new FontIcon(Icons.EYE);
        icon.setId("eye");

        node.getChildren().add(icon);
        return this;
    }
}
