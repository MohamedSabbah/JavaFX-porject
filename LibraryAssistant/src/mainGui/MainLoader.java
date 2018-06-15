/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainGui;

import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.Utile;

/**
 *
 * @author Sab
 */
public class MainLoader extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException, SQLException {
       
        
        Parent root = FXMLLoader.load(getClass().getResource("/login/Login.fxml"));
        Scene scene = new Scene(root);
        
       primaryStage.setTitle("library Assistant Login");
        primaryStage.setScene(scene);
       
        Utile.setStageIcon(primaryStage);
        primaryStage.show();
      
      
      
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
