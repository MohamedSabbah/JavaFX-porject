/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toolbar;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import util.Utile;

/**
 * FXML Controller class
 *
 * @author Sab
 */
public class ToolbarController implements Initializable {
    
 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    } 

     /*
    Add event listeners to the button in the tool Box ,
    this allows to open the previously Created 4 modules
    to be accessed right from the Main Window..
    
     */
    @FXML
    private void loadAddBook(ActionEvent event) {
        Utile.loadWindow(getClass().getResource("/addBook/FXMLAddBook.fxml"), "Add New Book", null);
    }

    @FXML
    private void loadAddMemeber(ActionEvent event) {
         Utile.loadWindow(getClass().getResource("/memberAdd/addMember.fxml"), " Add New Memeber", null);
    }

    @FXML
    private void loadBookTable(ActionEvent event) {
         Utile.loadWindow(getClass().getResource("/listBook/FXMLBookList.fxml"), " Book List", null);
    }

    @FXML
    private void loadMemeberTable(ActionEvent event) {
         Utile.loadWindow(getClass().getResource("/memeberList/MemeberList.fxml"), " Memeber List", null);
    }

    @FXML
    private void loadSettings(ActionEvent event) {
        Utile.loadWindow(getClass().getResource("/setting/settings.fxml"), " Settings", null);
    }
}
