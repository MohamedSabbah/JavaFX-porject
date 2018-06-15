/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package setting;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Sab
 */
public class SettingsController implements Initializable {

    @FXML
    private JFXTextField nDaysWithoutFine;
    @FXML
    private JFXTextField finePerDay;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    
     private Preference preference;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initDefaultValue();
    }    

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        Preference.getPreference();
        preference.setnDaysWithoutFine(Integer.parseInt(nDaysWithoutFine.getText()));
        preference.setFinePreDay(Float.parseFloat(finePerDay.getText()));
        preference.setUserName(username.getText());
        preference.setPassword(password.getText());
        Preference.whriteTopreferenceFile(preference);
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        ((Stage)nDaysWithoutFine.getScene().getWindow()).close();
        
    }
    
    /*
    write out configuration object (Object of Preference class) in to a file with the help of JSON library. 
    JSON allows to write objects as strings (which is also human readable). 
    */
    
    private  void initDefaultValue(){
         preference = Preference.getPreference();
        nDaysWithoutFine.setText(String.valueOf(preference.getnDaysWithoutFine()));
        finePerDay.setText(String.valueOf(preference.getFinePreDay()));
        username.setText(String.valueOf(preference.getUserName()));
        password.setText(String.valueOf(preference.getPassword()));
        
        
    }
}
