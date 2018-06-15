/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mainGui.MainController;
import org.apache.commons.codec.digest.DigestUtils;
import setting.Preference;
import util.Utile;

/**
 * FXML Controller class
 *
 * @author Sab
 */
public class LoginController implements Initializable {

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;

    Preference preference;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        preference = Preference.getPreference();
    }

    @FXML
    private void loginButtonAction(ActionEvent event) {
        String uName = username.getText();
        String pass = DigestUtils.shaHex(password.getText());
        
        if(uName.equals(preference.getUserName())&& pass.equals(preference.getPassword())){
           closeStage();
           loadMain();
            
        }else{
           username.getStyleClass().add("wrong-credenial");
           password.getStyleClass().add("wrong-credenial");
        }
            
    }

    @FXML
    private void cancelButtonAction(ActionEvent event) {
        System.exit(0);
    }

    private void closeStage() {
       ((Stage)username.getScene().getWindow()).close();
    }
    
     void loadMain() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/mainGui/Main.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Library Assistant");
            
            stage.setScene(new Scene(parent));
            stage.show();
            Utile.setStageIcon(stage);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
}
