/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package addBook;

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
import listBook.FXMLBookListController.Book;

/**
 * FXML Controller class
 *
 * @author Sab
 */
public class FXMLAddBookController implements Initializable {

    @FXML
    private JFXTextField title;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField author;
    @FXML
    private JFXTextField publisher;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;

    private boolean isEditMode = Boolean.FALSE;

    /**
     * Initializes the controller class.
     */
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    Alert alert;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // TODO
    }

    /**
     * Create Method for adding book to the database with using Save Button.
     *
     * @param event
     */
    @FXML
    private void addBook(ActionEvent event) {

        /*
        get the input from the user then put it in noraml varaibles
         */
        String bookID = id.getText();
        String bookTitle = title.getText();
        String bookAuthor = author.getText();
        String bookPublisher = publisher.getText();

        /*
        using the alert if the text fields is Empty and user tried to save it .
         */
        if (bookID.isEmpty() || bookTitle.isEmpty() || bookAuthor.isEmpty() || bookPublisher.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR, STYLESHEET_MODENA, ButtonType.OK);
            alert.setContentText("Please enter in all Fields");
            alert.setTitle("Empty info");
            alert.show();
            return;
        }

        if (isEditMode) {
            Book book = new Book(bookID, bookTitle, bookAuthor, bookPublisher, true);

            handleEditOperation(book);
            return;
        }

        /*
        Get Connection from the database 
        and using insert operation to add the info to the database
        with using PreparedStatement 
        in the End of the operation we close the Connection in Finally
         */
        try {
            conn = DBConnection.getConnection();
           
            pstmt = conn.prepareStatement("insert into book values (?,?,?,?,?)");
            pstmt.setString(1, bookID);
            pstmt.setString(2, bookTitle);
            pstmt.setString(3, bookAuthor);
            pstmt.setString(4, bookPublisher);
            pstmt.setBoolean(5, true);
            pstmt.executeUpdate();
            conn.commit();

            /*
            another alert to show the user the operation is done
             */
            if (pstmt != null) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Saved!");
                alert.setTitle("Sucess");
                alert.show();

                id.clear();
                title.clear();
                author.clear();
                publisher.clear();

            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("null");
                alert.setTitle("Failed");
                alert.show();
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try { if (conn != null) {conn.close();}} catch (SQLException e) {e.printStackTrace(); }
            try { if (pstmt != null) {pstmt.close();}} catch (SQLException e) {e.printStackTrace();}
        }

    }

    /*
    this is for cancel button to clear the info what the user added befoer he save it 
     */
    @FXML
    private void cancel(ActionEvent event) {
        id.clear();
        title.clear();
        author.clear();
        publisher.clear();
    }

    public void inflateUI(Book book) {
        title.setText(book.getTitle());
        id.setText(book.getId());
        author.setText(book.getAuthor());
        publisher.setText(book.getPublisher());
        saveButton.setText("Edit");
        id.setEditable(false);
        isEditMode = true;

    }

    private void handleEditOperation(Book book) {
        try {

            conn = DBConnection.getConnection();

            pstmt = conn.prepareStatement("update book set title =?, author =?, publisher=? where  id = ? ");
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getPublisher());
            pstmt.setString(4, book.getId());
            pstmt.executeUpdate();
            conn.commit();
            if (pstmt != null) {
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
            Logger.getLogger(FXMLAddBookController.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try { if (conn != null) {conn.close();}} catch (SQLException e) {e.printStackTrace(); }
            try { if (pstmt != null) {pstmt.close();}} catch (SQLException e) {e.printStackTrace();}
        }
        
    }

}
