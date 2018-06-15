/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mainGui.MainController;

/**
 *
 * @author Sab
 */
public class Utile {

    public static final String IMAGE_LOCATION = "/resources/if_bookshelf_1055107.png";

    public static void setStageIcon(Stage stage) {
        stage.getIcons().add(new Image(IMAGE_LOCATION));

    }

    public static void loadWindow(URL Location, String Title, Stage parentStage) {
        try {
            Parent parent = FXMLLoader.load(Location);
            Stage stage = null;

            if (stage != null) {
                stage = parentStage;
            } else {

                stage = new Stage(StageStyle.DECORATED);
            }

            stage.setTitle(Title);
            stage.setScene(new Scene(parent));
            stage.show();
            setStageIcon(stage);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
