package org.h0110w.somclient.appearance;

import javafx.scene.image.*;
import javafx.scene.paint.Color;

public class IconPainter {
    private IconPainter() {

    }

    public static ImageView getRepaint(Image image, String color) {
        return new ImageView(changeImageColor(image, Color.valueOf(color)));
    }

    public static WritableImage changeImageColor(Image image, Color newColor) {
        if (image == null)
            throw new IllegalArgumentException("Cannot change the color of a null image.");

        WritableImage newImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelWriter writer = newImage.getPixelWriter();
        PixelReader reader = image.getPixelReader();

        for (int readY = 0; readY < image.getHeight(); readY++) {
            for (int readX = 0; readX < image.getWidth(); readX++) {
                Color color = reader.getColor(readX, readY);
                if (color.getOpacity() > 0) {
                    writer.setColor(readX, readY, newColor);
                }
            }
        }

        return newImage;
    }
}
