/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listBook;

import addBook.FXMLAddBookController;
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
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import util.Utile;

/**
 * FXML Controller class
 *
 * @author Sab
 */
public class FXMLBookListController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Book> tableView;
    @FXML
    private TableColumn<Book, String> idCol;
    @FXML
    private TableColumn<Book, String> titleCol;
    @FXML
    private TableColumn<Book, String> authorCol;
    @FXML
    private TableColumn<Book, String> publisherCol;
    @FXML

    private TableColumn<Book, Boolean> availabilityCol;
    ObservableList<Book> list = FXCollections.observableArrayList();
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
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
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("availability"));
    }

    /*
    to load the data from the Database to the Table
     */
    private void loadData() {
        list.clear();
        try {
            conn = DBConnection.getConnection();
            
            pstmt = conn.prepareStatement("SELECT * FROM book");
            resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                String idBk = resultSet.getString("id");
                String titleBk = resultSet.getString("title");
                String authorBk = resultSet.getString("author");
                String publisherBk = resultSet.getString("publisher");
                boolean avail = resultSet.getBoolean("isavail");
                // availabilityCol.setVisible(resultSet.getBoolean("isavail"));
                list.add(new Book(idBk, titleBk, authorBk, publisherBk, avail));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }finally {
            try { if (conn != null) {conn.close();}} catch (SQLException e) {e.printStackTrace(); }
            try { if (pstmt != null) {pstmt.close();}} catch (SQLException e) {e.printStackTrace();}
            try { if (resultSet != null) {resultSet.close();}} catch (SQLException e) {e.printStackTrace();}
        }

        tableView.setItems(list);
    }

    @FXML
    private void handleBookDeleteOption(ActionEvent event) {

        int stmt;
        Book selectedForDelete = tableView.getSelectionModel().getSelectedItem();
        if (selectedForDelete == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select The Book First.", ButtonType.OK);
            alert.setTitle("No Book Selected");
            alert.showAndWait();

        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting Book");
        alert.setContentText("Are you sure want to delete The Book " + selectedForDelete.getTitle() + " ?");
        Optional<ButtonType> answer = alert.showAndWait();
        if (answer.get() == ButtonType.OK) {
            try {
                conn = DBConnection.getConnection();
                pstmt = conn.prepareStatement("delete from book where id = ? ");
                pstmt.setString(1, selectedForDelete.getId());
                stmt = pstmt.executeUpdate();
                conn.commit();
                if (stmt != 0) {
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucess");
                    alert.setContentText("Book " + selectedForDelete.getTitle() + " deleted");
                    alert.show();
                    list.remove(selectedForDelete);
                }
            } catch (SQLException ex) {
                Logger.getLogger(FXMLBookListController.class.getName()).log(Level.SEVERE, null, ex);
                
            }finally {
            try { if (conn != null) {conn.close();}} catch (SQLException e) {e.printStackTrace(); }
            try { if (pstmt != null) {pstmt.close();}} catch (SQLException e) {e.printStackTrace();}
        }
            
        } else {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Deletion Canceled");
            alert1.setContentText("Deletion process Canceled");
            alert1.show();
        }

    }

    @FXML
    private void handleBookEditOption(ActionEvent event) {
        try {
            Book selectedForEdit = tableView.getSelectionModel().getSelectedItem();
            if (selectedForEdit == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select The Book First.", ButtonType.OK);
                alert.setTitle("No Book Selected");
                alert.showAndWait();

            }

            FXMLLoader fXMLLoader = new FXMLLoader(getClass().getResource("/addBook/FXMLAddBook.fxml"));
            Parent rParent = fXMLLoader.load();

            FXMLAddBookController controller = fXMLLoader.getController();
            controller.inflateUI(selectedForEdit);
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Book");
            stage.setScene(new Scene(rParent));
            stage.show();
            Utile.setStageIcon(stage);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    handleBookRefresh(event);
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(FXMLBookListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    refresh the book list table by right clicking 
    and selecting refresh option from the context menu. 
     */
    @FXML
    private void handleBookRefresh(ActionEvent event) {

        loadData();
    }

    /*
    Making Inner Class for the books
     */
    public static class Book {
        // Use SimpleStringProperty as the concrete implementation in The code

        private final SimpleStringProperty id;
        private final SimpleStringProperty title;
        private final SimpleStringProperty author;
        private final SimpleStringProperty publisher;
        private final SimpleBooleanProperty availability;

        /*
   Create Constractur taking the data
         */
        public Book(String id, String title, String author, String publisher, boolean availability) {
            this.id = new SimpleStringProperty(id);
            this.title = new SimpleStringProperty(title);
            this.author = new SimpleStringProperty(author);
            this.publisher = new SimpleStringProperty(publisher);
            this.availability = new SimpleBooleanProperty(availability);
        }

        /*
    create getter for the fields 
         */
        public String getId() {
            return id.get();
        }

        public String getTitle() {
            return title.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public String getPublisher() {
            return publisher.get();
        }

        public Boolean getAvailability() {
            return availability.get();
        }

    }

}
