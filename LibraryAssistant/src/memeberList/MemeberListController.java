/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memeberList;

import database.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import memberAdd.AddMemberController;
import util.Utile;

/**
 * FXML Controller class
 *
 * @author Sab
 */
public class MemeberListController implements Initializable {

    @FXML
    private TableView<Memeber> tableView;
    @FXML
    private TableColumn<Memeber, String> idCol;
    @FXML
    private TableColumn<Memeber, String> nameCol;
    @FXML
    private TableColumn<Memeber, String> mobileCol;
    @FXML
    private TableColumn<Memeber, String> emailCol;

    ObservableList<Memeber> list = FXCollections.observableArrayList();
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        loadData();
    }

    /*
     to inintializ the the column  in the Table with the data
     */
    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    /*
    to load the data from the Database to the Table
     */
    private void loadData() {
        list.clear();
        try {
            conn = DBConnection.getConnection();
            
            pstmt = conn.prepareStatement("select * from memeber");
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                String memeberId = resultSet.getString("id");
                String memeberName = resultSet.getString("name");
                String memeberMobile = resultSet.getString("mobile");
                String memeberEmail = resultSet.getString("email");

                list.add(new Memeber(memeberId, memeberName, memeberMobile, memeberEmail));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }finally{
            try { if (conn != null) {conn.close();}} catch (SQLException e) {e.printStackTrace();}
            try { if (pstmt != null) {pstmt.close();}} catch (SQLException e) {e.printStackTrace();}
            try { if (resultSet != null) {resultSet.close();}} catch (SQLException e) {e.printStackTrace();}
        }

        tableView.getItems().setAll(list);
    }

    @FXML
    private void handleDeleteMemeberOption(ActionEvent event) {
        Memeber selectDelelteItem = tableView.getSelectionModel().getSelectedItem();
        if (selectDelelteItem == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select The Memeber First.", ButtonType.OK);
            alert.setTitle("No Memeber Selected");
            alert.showAndWait();
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting Memeber");
        alert.setContentText("Are you sure want to delete The Memeber " + selectDelelteItem.getName() + " ?");
        Optional<ButtonType> answer = alert.showAndWait();

        if (answer.get() == ButtonType.OK) {
            try {
                conn = DBConnection.getConnection();
                pstmt = conn.prepareStatement("delete from memeber where id =?");
                pstmt.setString(1, selectDelelteItem.getId());
                int stmt = pstmt.executeUpdate();
                conn.commit();

                if (stmt != 0) {
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucess");
                    alert.setContentText("Memeber " + selectDelelteItem.getName() + " deleted");
                    alert.show();
                    list.remove(selectDelelteItem);
                    HandleRefreshMemeberOption(event);
                }
            } catch (SQLException ex) {
                Logger.getLogger(MemeberListController.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
             try {if(conn!= null)conn.close();} catch (SQLException e) {e.printStackTrace();}
            try {if(pstmt != null)pstmt.close();} catch (SQLException e) {e.printStackTrace();}
        }

        } else {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Deletion Canceled");
            alert1.setContentText("Deletion process Canceled");
            alert1.show();
        }
    }

    /*
    member edit option..
    Now member info can be edited and the table can be refreshed from context menu.
    */
    
    @FXML
    private void handleEditMemeberOption(ActionEvent event) {
        try {
            Memeber selectEditItem = tableView.getSelectionModel().getSelectedItem();
            if (selectEditItem == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select The Memeber First.", ButtonType.OK);
                alert.setTitle("No Memeber Selected");
                alert.showAndWait();
            }
            
            
            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/memberAdd/addMember.fxml"));
            Parent rParent = fXMLLoader.load();
            AddMemberController controller = fXMLLoader.getController();
            controller.inflateUI(selectEditItem);
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Memeber");
            stage.setScene(new Scene(rParent));
            stage.show();
            Utile.setStageIcon(stage);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    HandleRefreshMemeberOption(event);
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(MemeberListController.class.getName()).log(Level.SEVERE, null, ex);
        }

        

    }
    // Memeber refresh Option....
    @FXML
    private void HandleRefreshMemeberOption(ActionEvent event) {
        loadData();
    }

    /*
    Making Inner Class for The Memebers
     */
    public static class Memeber {
        // Use SimpleStringProperty as the concrete implementation in The code

        private final SimpleStringProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty mobile;
        private final SimpleStringProperty email;

        /*
   Create Constractur taking the data
         */
        public Memeber(String id, String name, String mobile, String email) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.mobile = new SimpleStringProperty(mobile);
            this.email = new SimpleStringProperty(email);

        }

        /*
    create getter for the fields 
         */
        public String getId() {
            return id.get();
        }

        public String getName() {
            return name.get();
        }

        public String getMobile() {
            return mobile.get();
        }

        public String getEmail() {
            return email.get();
        }

    }

}
