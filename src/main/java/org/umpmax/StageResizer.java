package org.umpmax;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class StageResizer {
    double xOffset = 0;
    double yOffset = 0;

    StageResizer(Stage stage, Scene sc) {
        sc.setOnMouseDragged((e) -> {
            if (!stage.isFullScreen()) {
                stage.setX(e.getScreenX() + xOffset);
                stage.setY(e.getScreenY() + yOffset);
            }
        });
        sc.setOnMousePressed((e) -> {
            xOffset = stage.getX() - e.getScreenX();
            yOffset = stage.getY() - e.getScreenY();
        });
    }
}
