/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainGui;

import alertMaker.AlertMaker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import database.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import util.Utile;

/**
 * FXML Controller class
 *
 * @author Sab
 */
public class MainController implements Initializable {

    @FXML
    private HBox bookInfo;
    @FXML
    private HBox memeberInfo;
    @FXML
    private TextField bookIDInput;
    @FXML
    private Text bookName;
    @FXML
    private Text bookAuthor;
    @FXML
    private Text bookStatus;
    @FXML
    private TextField memeberIDInput;
    @FXML
    private Text memeberName;
    @FXML
    private Text memeberContact;

    @FXML
    private JFXButton issueBtn;
    @FXML
    private JFXTextField book_ID;
    @FXML
    private StackPane rootPane;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private Text memberNameHolder;
    @FXML
    private Text memberEmailHolder;
    @FXML
    private Text memberContactHolder;
    @FXML
    private Text bookNameHolder;
    @FXML
    private Text bookAuthorHolder;
    @FXML
    private Text bookPublisherHolder;
    @FXML
    private Text issueDateHolder;
    @FXML
    private Text numberDaysHolder;
    @FXML
    private Text fineInfoHolder;
    @FXML
    private BorderPane borderRoot;
    @FXML
    private JFXButton renewButton;
    @FXML
    private JFXButton submissionButton;
    @FXML
    private HBox submissionData;
    @FXML
    private Tab renewTab;
    @FXML
    private JFXTabPane mainTabPane;
    @FXML
    private StackPane BookInfoContainer;
    @FXML
    private StackPane memeberInfoContainer;

    private ListView<String> issueDataList;
    boolean isreadyForSubmission = false;

    private static final String BOOK_NOT_AVAILABLE = "Not Available";
    private static final String NO_SUCH_BOOK_AVAILABLE = "No Such Book Available";
    private static final String NO_SUCH_MEMBER_AVAILABLE = "No Such Member Available";
    private static final String BOOK_AVAILABLE = "Available";

    private PreparedStatement Pstatement = null;
    private Connection conn = null;
    private ResultSet resultSet = null;
    private PieChart bookChart, memeberChart;

    int pstmt = 0;
    int pstmt1 = 0;
    @FXML
    private Tab bookIssueTab;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /*
        Giving depth to The HBOX components JFXDepthManager.
         */
        JFXDepthManager.setDepth(bookInfo, 1);
        JFXDepthManager.setDepth(memeberInfo, 1);

        initDrawer();
        initGraphs();

    }

    /*
      Option to select the book from the main window itself,
    when eneter the book ID, it is searching in the database.
     */
    @FXML
    private void loadBookInfo(ActionEvent event) {
        clearBookCache();
        enableDisableGraph(false);
        String id = bookIDInput.getText();
        try {
            conn = DBConnection.getConnection();

            Pstatement = conn.prepareStatement("select title,author,isavail from book where id = ? ");
            Pstatement.setString(1, id);
            resultSet = Pstatement.executeQuery();
            boolean flag = false;
            while (resultSet.next()) {
                String bName = resultSet.getString("title");
                String bAuthor = resultSet.getString("author");
                boolean bStatus = resultSet.getBoolean("isavail");

                String status = (bStatus) ? "Available" : "Not Available";
                bookName.setText(bName);
                bookAuthor.setText(bAuthor);
                bookStatus.setText(status);
                flag = true;

            }
            if (!flag) {
                bookName.setText("The Book Not Available");

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (Pstatement != null) {
                    Pstatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    After entering member id, member data is loaded from the database and displayed.
     */
    @FXML
    private void loadMemberInfo(ActionEvent event) {

        clearMemeberCache();
        enableDisableGraph(false);
        String memeberID = memeberIDInput.getText();
        try {
            conn = DBConnection.getConnection();
            Pstatement = conn.prepareStatement("select name, mobile from memeber where id = ?");
            Pstatement.setString(1, memeberID);
            resultSet = Pstatement.executeQuery();
            boolean flag = false;
            while (resultSet.next()) {
                String mName = resultSet.getString("name");
                String mMobile = resultSet.getString("mobile");

                memeberName.setText(mName);
                memeberContact.setText(mMobile);

                flag = true;
            }
            if (!flag) {
                memeberName.setText("Not Available");

            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (Pstatement != null) {
                    Pstatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    void clearBookCache() {
        bookName.setText("");
        bookAuthor.setText("");
        bookStatus.setText("");
    }

    void clearMemeberCache() {
        memeberName.setText("");
        memeberContact.setText("");
    }

    /*
    Implementing book issue opration and loaded both member and book data for book issue.
     */
    @FXML
    private void loadIssueOperation(ActionEvent event) {

        if (checkForIssueValidity()) {
            JFXButton btn = new JFXButton("Okay!");
            AlertMaker.showMaterialDialog(rootPane, borderRoot, Arrays.asList(btn), "Invalid Input", null);
            return;
        }
        if (bookStatus.getText().equals(BOOK_NOT_AVAILABLE)) {
            JFXButton btn = new JFXButton("Okay!");
            JFXButton viewDetails = new JFXButton("View Details");
            viewDetails.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                String bookToBeLoaded = bookIDInput.getText();
                book_ID.setText(bookToBeLoaded);
                book_ID.fireEvent(new ActionEvent());
                mainTabPane.getSelectionModel().select(renewTab);
            });
            AlertMaker.showMaterialDialog(rootPane, borderRoot, Arrays.asList(btn, viewDetails), "Already issued book", "This book is already issued. Cant process issue request");
            return;
        }

        String bookID = bookIDInput.getText();
        String memeberID = memeberIDInput.getText();

        JFXButton yesButton = new JFXButton("YES");
        yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent eventx) -> {
            try {
                conn = DBConnection.getConnection();

                Pstatement = conn.prepareStatement("insert into issue (book_id,memeber_id) values (?,?)");
                Pstatement.setString(1, bookID);
                Pstatement.setString(2, memeberID);
                pstmt1 = Pstatement.executeUpdate();

                Pstatement = conn.prepareStatement("update book set isavail = false where id = ?");
                Pstatement.setString(1, bookID);
                pstmt = Pstatement.executeUpdate();
                conn.commit();

                if (pstmt != 0 && pstmt1 != 0) {
                    JFXButton button = new JFXButton("Done!");
                    AlertMaker.showMaterialDialog(rootPane, borderRoot, Arrays.asList(button), "Book Issue Complete", null);

                } else {
                    JFXButton button = new JFXButton("Okay.I'll Check");
                    AlertMaker.showMaterialDialog(rootPane, borderRoot, Arrays.asList(button), "Issue Operation Failed", null);

                }
            } catch (SQLException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (Pstatement != null) {
                        Pstatement.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            clearIssueEntries();
        });
        JFXButton noButton = new JFXButton("NO");
        noButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
            JFXButton button = new JFXButton("That's Okay");
            AlertMaker.showMaterialDialog(rootPane, borderRoot, Arrays.asList(button), "Issue Cancelled", null);
            clearIssueEntries();
        });
        AlertMaker.showMaterialDialog(rootPane, borderRoot, Arrays.asList(yesButton, noButton), "Confirm Issue", "Are you sure want to issue the book " + bookName.getText() + " to " + memeberName.getText() + " ?");

    }

    /**
     * load the issued book information when the member return the book for
     * submission or renewal. For this data is accessed from all 3 tables. Once
     * the Admin enters the issued book id, load the issued member id from the
     * ISSUE table along with other issue params. The book id and member id is
     * then used to retrieve members books information. All the retrieved data
     * are listed in the list view.
     */
    @FXML
    private void loadBookInfo2(ActionEvent event) {
        clearEntries();
        ObservableList<String> issueData = FXCollections.observableArrayList();
        isreadyForSubmission = false;
        String bID = book_ID.getText();

        try {
            conn = DBConnection.getConnection();
            Pstatement = conn.prepareStatement("select * from issue where book_id = ?");
            Pstatement.setString(1, bID);
            resultSet = Pstatement.executeQuery();
            if (resultSet.next()) {
                String mBookID = bID;
                String mMemeberID = resultSet.getString("memeber_id");
                Timestamp mtimestamp = resultSet.getTimestamp("issue_time");
                int mRenewCount = resultSet.getInt("renew_count");

                Date dateOfIssue = new Date(mtimestamp.getTime());
                issueDateHolder.setText(dateOfIssue.toString());
                Long timeElapsed = System.currentTimeMillis() - mtimestamp.getTime();
                Long days = TimeUnit.DAYS.convert(timeElapsed, TimeUnit.MILLISECONDS) + 1;
                String daysElapsed = String.format("Used %d days", days);
                numberDaysHolder.setText(daysElapsed);

                Pstatement = conn.prepareStatement("select name, email, mobile from memeber where id = ?");
                Pstatement.setString(1, mMemeberID);
                resultSet = Pstatement.executeQuery();
                while (resultSet.next()) {

                    memberNameHolder.setText(resultSet.getString("name"));
                    memberEmailHolder.setText(resultSet.getString("email"));
                    memberContactHolder.setText(resultSet.getString("mobile"));
                }

                Pstatement = conn.prepareStatement("select title, author , publisher from book where id = ?");
                Pstatement.setString(1, mBookID);
                resultSet = Pstatement.executeQuery();
                while (resultSet.next()) {

                    bookNameHolder.setText(resultSet.getString("book.title"));
                    bookAuthorHolder.setText(resultSet.getString("book.author"));
                    bookPublisherHolder.setText(resultSet.getString("book.publisher"));
                }
                isreadyForSubmission = true;
                disableEnableControls(true);
                submissionData.setOpacity(1);
            } else {
                JFXButton button = new JFXButton("Okay.I'll Check");
                AlertMaker.showMaterialDialog(rootPane, borderRoot, Arrays.asList(button), "No such Book Exists in Issue Database", null);

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (Pstatement != null) {
                    Pstatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    private void loadIssueSubmission(ActionEvent event) {

        if (!isreadyForSubmission) {
            JFXButton btn = new JFXButton("Okay!");
            AlertMaker.showMaterialDialog(rootPane, borderRoot, Arrays.asList(btn), "Please select a book to submit", "Cant simply submit a null book :-)");

            return;
        }
        JFXButton yesButton = new JFXButton("YES, Please");
        yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    String bID = book_ID.getText();

                    conn = DBConnection.getConnection();
                    Pstatement = conn.prepareStatement("delete from issue where book_id =?");
                    Pstatement.setString(1, bID);
                    pstmt1 = Pstatement.executeUpdate();
                    conn.commit();

                    Pstatement = conn.prepareStatement("update book set isavail = true where id = ?");
                    Pstatement.setString(1, bID);
                    pstmt = Pstatement.executeUpdate();
                    conn.commit();
                    loadBookInfo2(null);

                    if (pstmt != 0 && pstmt1 != 0) {
                        JFXButton btn = new JFXButton("Done!");
                        AlertMaker.showMaterialDialog(rootPane, borderRoot, Arrays.asList(btn), "Book has been submitted", null);
                        disableEnableControls(false);
                        submissionData.setOpacity(0);
                    } else {
                        JFXButton btn = new JFXButton("Okay.I'll Check");
                        AlertMaker.showMaterialDialog(rootPane, borderRoot, Arrays.asList(btn), "Submission Has Been Failed", null);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (Pstatement != null) {
                            Pstatement.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        JFXButton noButton = new JFXButton("No, Cancel");
        noButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent ev) -> {
            JFXButton btn = new JFXButton("Okay!");
            AlertMaker.showMaterialDialog(rootPane, borderRoot, Arrays.asList(btn), "Submission Operation cancelled", null);
        });

        AlertMaker.showMaterialDialog(rootPane, borderRoot, Arrays.asList(yesButton, noButton), "Confirm Submission Operation", "Are you sure want to return the book ?");

    }

    /*
    implemented an option to renew the book. During book renewal,
    the issue date-time is updated and renew_count variable is incremented in the ISSUE table.

     */
    @FXML
    private void loadrenewMethod(ActionEvent event) {

        if (!isreadyForSubmission) {
            JFXButton btn = new JFXButton("Okay!");
            AlertMaker.showMaterialDialog(rootPane, borderRoot, Arrays.asList(btn), "Please select a book to renew", null);
            return;
        }

        JFXButton yesButton = new JFXButton("YES, Please");
        yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    conn = DBConnection.getConnection();

                    Pstatement = conn.prepareStatement("update issue set issue_time = CURRENT_TIMESTAMP, renew_count = renew_count+1 where book_id = ?");
                    Pstatement.setString(1, book_ID.getText());
                    pstmt = Pstatement.executeUpdate();
                    loadBookInfo2(null);

                    if (pstmt != 0) {
                        JFXButton btn = new JFXButton("Alright!");
                        AlertMaker.showMaterialDialog(rootPane, borderRoot, Arrays.asList(btn), "Book Has Been Renewed", null);
                        disableEnableControls(false);
                        submissionData.setOpacity(0);

                    } else {
                        JFXButton btn = new JFXButton("Okay!");
                        AlertMaker.showMaterialDialog(rootPane, borderRoot, Arrays.asList(btn), "Renew Has Been Failed", null);

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (Pstatement != null) {
                            Pstatement.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        JFXButton noButton = new JFXButton("No, Don't!");
        noButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
            JFXButton btn = new JFXButton("Okay!");
            AlertMaker.showMaterialDialog(rootPane, borderRoot, Arrays.asList(btn), "Renew Operation cancelled", null);
        });
        AlertMaker.showMaterialDialog(rootPane, borderRoot, Arrays.asList(yesButton, noButton), "Confirm Renew Operation", "Are you sure want to renew the book ?");

    }

    @FXML
    private void handleMenuClose(ActionEvent event) {
        ((Stage) rootPane.getScene().getWindow()).close();
    }

    @FXML
    private void handleMenuAddMemeber(ActionEvent event) {
        Utile.loadWindow(getClass().getResource("/memberAdd/addMember.fxml"), " Add New Memeber", null);
    }

    @FXML
    private void handleMenuAddBook(ActionEvent event) {
        Utile.loadWindow(getClass().getResource("/addBook/FXMLAddBook.fxml"), "Add New Book", null);
    }

    @FXML
    private void handleMenuVeiwMemeber(ActionEvent event) {
        Utile.loadWindow(getClass().getResource("/memeberList/MemeberList.fxml"), " Memeber List", null);
    }

    @FXML
    private void handleMenuVeiwBook(ActionEvent event) {
        Utile.loadWindow(getClass().getResource("/listBook/FXMLBookList.fxml"), " Book List", null);
    }

    @FXML
    private void handleMenuFullScreen(ActionEvent event) {
        Stage stage = ((Stage) rootPane.getScene().getWindow());
        stage.setFullScreen(!stage.isFullScreen());
    }

    /*
    attached the VBox toolbar created  to the main screen.
    When the JFXHamburger is clicked, the toolbar will be toggled between open and close.
     */
    private void initDrawer() {
        try {
            VBox toolbar = FXMLLoader.load(getClass().getResource("/toolbar/Toolbar.fxml"));
            drawer.setSidePane(toolbar);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(hamburger);
        task.setRate(-1);

        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                task.setRate(task.getRate() * -1);
                task.play();
                if (drawer.isHidden()) {
                    drawer.open();
                } else {
                    drawer.close();
                }

            }
        });
    }

    private void clearEntries() {

        memberContactHolder.setText("");
        memberEmailHolder.setText("");
        memberNameHolder.setText("");
        bookAuthorHolder.setText("");
        bookNameHolder.setText("");
        bookPublisherHolder.setText("");
        fineInfoHolder.setText("");
        issueDateHolder.setText("");
        numberDaysHolder.setText("");
        disableEnableControls(false);
        submissionData.setOpacity(0);
    }

    private void disableEnableControls(Boolean enableFlag) {
        if (enableFlag) {
            renewButton.setDisable(false);
            submissionButton.setDisable(false);
        } else {
            renewButton.setDisable(true);
            submissionButton.setDisable(true);
        }
    }

    private boolean checkForIssueValidity() {
        bookIDInput.fireEvent(new ActionEvent());
        memeberIDInput.fireEvent(new ActionEvent());
        return bookIDInput.getText().isEmpty() || memeberIDInput.getText().isEmpty()
                || memeberName.getText().isEmpty() || bookName.getText().isEmpty()
                || bookName.getText().equals(NO_SUCH_BOOK_AVAILABLE) || memeberName.getText().equals(NO_SUCH_MEMBER_AVAILABLE);
    }

    private void clearIssueEntries() {
        bookIDInput.clear();
        memeberIDInput.clear();
        bookName.setText("");
        bookAuthor.setText("");
        bookStatus.setText("");
        memeberContact.setText("");
        memeberName.setText("");
        enableDisableGraph(true);
    }

    /*
    adds a PieChart for displaying book information in the dashboard. 
     */
    private ObservableList<PieChart.Data> getBookGraphStatistics() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        try {
            conn = DBConnection.getConnection();
            Pstatement = conn.prepareStatement("select count(*) from book");
            resultSet = Pstatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                data.add(new PieChart.Data("Total Books (" + count + ")", count));
            }
            Pstatement = conn.prepareStatement("select count(*)from issue");
            resultSet = Pstatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                data.add(new PieChart.Data("Issued Books (" + count + ")", count));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (Pstatement != null) {
                    Pstatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    private ObservableList<PieChart.Data> getMemberGraphStatistics() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        try {
            conn = DBConnection.getConnection();
            Pstatement = conn.prepareStatement("select count(*) from memeber");
            resultSet = Pstatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                data.add(new PieChart.Data("Total Memebers (" + count + ")", count));
            }
            conn = DBConnection.getConnection();
            Pstatement = conn.prepareStatement("select count(*) from issue");
            resultSet = Pstatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                data.add(new PieChart.Data("Active (" + count + ")", count));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (Pstatement != null) {
                    Pstatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /*
        The chart contains two entries. 
       One is total number of books available in the library and the second shows number of books currently issued. 
     */
    private void initGraphs() {
        bookChart = new PieChart(getBookGraphStatistics());
        memeberChart = new PieChart(getMemberGraphStatistics());
        BookInfoContainer.getChildren().add(bookChart);
        memeberInfoContainer.getChildren().add(memeberChart);
        clearIssueEntries();
        bookIssueTab.setOnSelectionChanged((Event event) -> {
            clearIssueEntries();
            if (bookIssueTab.isSelected()) {
                refreshGraphs();
            }
        });
    }

    private void refreshGraphs() {
        bookChart.setData(getBookGraphStatistics());
        memeberChart.setData(getMemberGraphStatistics());
    }

    private void enableDisableGraph(Boolean status) {
        if (status) {
            bookChart.setOpacity(1);
            memeberChart.setOpacity(1);
        } else {
            bookChart.setOpacity(0);
            memeberChart.setOpacity(0);
        }
    }
}
