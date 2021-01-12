/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author TimeTraveler
 */
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
