package org.h0110w.somclient.scenes.tests;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.h0110w.somclient.appearance.Icons;

public class PaintIconTest extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-color: #002b36");

        HBox hBox = new HBox();
        hBox.prefWidthProperty().bind(anchorPane.widthProperty());
        hBox.prefHeightProperty().bind(anchorPane.heightProperty());
        hBox.setAlignment(Pos.CENTER);

//        ImageView imageView = getColorAdjusted();
        ImageView imageView = getRepaint();

        hBox.getChildren().add(imageView);

        anchorPane.getChildren().add(hBox);
        Scene scene = new Scene(anchorPane, 300, 250);
        stage.setScene(scene);
        stage.show();
    }

    private ImageView getRepaint() {
//        return new ImageView(changeImageColor(Icons.BURGER, Color.valueOf("#839496")));
        return null;
    }

    public static final WritableImage changeImageColor(Image image, Color newColor)
    {
        if (image == null)
            throw new IllegalArgumentException("Cannot change the color of a null image.");

        WritableImage newImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelWriter writer = newImage.getPixelWriter();
        PixelReader reader = image.getPixelReader();

        for (int readY = 0; readY < image.getHeight(); readY++)
        {
            for (int readX = 0; readX < image.getWidth(); readX++)
            {
                Color color = reader.getColor(readX, readY);
                if (color.getOpacity() > 0)
                {
                    writer.setColor(readX, readY, newColor);
                }
            }
        }

        return newImage;
    }

    public static void main(String[] args) {
        launch();
    }
}
