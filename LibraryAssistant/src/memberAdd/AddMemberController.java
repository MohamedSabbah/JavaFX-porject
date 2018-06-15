/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memberAdd;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import database.DBConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.STYLESHEET_MODENA;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import memeberList.MemeberListController;
import memeberList.MemeberListController.Memeber;

/**
 * FXML Controller class
 *
 * @author Sab
 */
public class AddMemberController implements Initializable {

    @FXML
    private JFXTextField memberName;
    @FXML
    private JFXTextField memberId;
    
    @FXML
    private JFXTextField memberEmail;
    @FXML
    private JFXTextField memberMobile;
    
     private Alert alert;
     private Connection connection = null;
     private PreparedStatement pstatement = null;
     
     boolean isEdit = false;
    @FXML
    private JFXButton saveButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
       
    }    
     
    @FXML
    private void addMember(ActionEvent event) {
         /*
       1) get the input from the user and put it in normal varaibles.
        2) put the varaibles inside boolean varaible and usr Alert if the text fields is empty,
        with using if Statement.
        */
        String idMemeber = memberId.getText() ;
        String nameMemeber = memberName.getText();
        String mobileMemeber = memberMobile.getText();
        String emailMemeber = memberEmail.getText();
        
       boolean flag = idMemeber.isEmpty()||nameMemeber.isEmpty()||mobileMemeber.isEmpty()||emailMemeber.isEmpty();
       if(flag){
           alert = new Alert(Alert.AlertType.ERROR, STYLESHEET_MODENA, ButtonType.OK);
           alert.setContentText("Please enter in all Fields");
          alert.setTitle("Empty info");
          alert.show();
          return;
       }
       
        if (isEdit) {
            Memeber memeber= new Memeber(idMemeber, nameMemeber, mobileMemeber, emailMemeber);

            handleEditOperation(memeber);
            return;
        }
       
        /*
        Get Connection from the database 
        and using insert operation to add the info to the database
        with using PreparedStatement 
        in the End of the operation we close the Connection in Finally
        */
        try {
            connection = DBConnection.getConnection();
            
            pstatement = connection.prepareStatement("insert into memeber values (?,?,?,?)");
            pstatement.setString(1,idMemeber );
            pstatement.setString(2,nameMemeber );
            pstatement.setString(3,mobileMemeber);
            pstatement.setString(4,emailMemeber);
            pstatement.executeUpdate();
            connection.commit();
            
             /*
            another alert to show the user the operation is done
            */
            if (pstatement!= null){
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Saved!");
                alert.setTitle("Sucess");
                alert.show();
            }else{
                   alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("null");
                alert.setTitle("Field");
                alert.show();
            }
             
        } catch (SQLException ex) {
            Logger.getLogger(AddMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
             try {if(connection!= null)connection.close();} catch (SQLException e) {e.printStackTrace();}
            try {if(pstatement != null)pstatement.close();} catch (SQLException e) {e.printStackTrace();}
        }
    }

    @FXML
    private void cancelMember(ActionEvent event) {
        
        
        Stage stage =  (Stage)memberName.getScene().getWindow();
        stage.close();
    }

    public void inflateUI(Memeber memeber) {
      memberId.setText(memeber.getId());
      memberEmail.setText(memeber.getEmail());
      memberName.setText(memeber.getName());
      memberMobile.setText(memeber.getMobile());
      memberId.setEditable(false);
      isEdit = true;
    }
    
     private void handleEditOperation(Memeber memeber) {
        try {

            connection = DBConnection.getConnection();

            pstatement = connection.prepareStatement("update memeber set name =?, mobile =?, email =? where  id = ? ");
            pstatement.setString(1, memeber.getName());
           pstatement.setString(2, memeber.getMobile());
            pstatement.setString(3, memeber.getEmail());
           pstatement.setString(4, memeber.getId());
           pstatement.executeUpdate();
            connection.commit();
            if (pstatement != null) {
                saveButton.setText("Save");
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("The Update is  Done!");
                alert.setTitle("Sucess");
                alert.show();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("null");
                alert.setTitle("Failed");
                alert.show();
            }

        } catch (SQLException ex) {
            Logger.getLogger(MemeberListController.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
             try {if(connection!= null)connection.close();} catch (SQLException e) {e.printStackTrace();}
            try {if(pstatement != null)pstatement.close();} catch (SQLException e) {e.printStackTrace();}
        }
    }
    
}
