package project.toeicfighter.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import project.toeicfighter.cores.DatabaseManager;
import project.toeicfighter.cores.SceneManager;
import project.toeicfighter.models.Account;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class accountCenterController implements Initializable {

    @FXML
    private VBox accountContainer;
    @FXML
    private ScrollPane accountPane;
    public Label noticeLabel;
    @FXML
    private ChoiceBox<String> roleChoiceBox = new ChoiceBox<>();
    @FXML
    private TextField usernameField, fullnameField;
    @FXML
    private PasswordField passwordField;

    private ArrayList<Account> accountList;

    // ===== Function below will render an account list an 2 button helping reset password or delete account =====
    public void accountPaneRender() throws SQLException {
        //clear previous list and make sure new list will not contain old element;
        accountContainer.getChildren().clear();

        // call SQL again to update data in arraylist
        accountList = DatabaseManager.getAccountList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        //Render Header
        HBox header = new HBox(20);
        header.getStyleClass().add("account-header");
        Label hID = new Label("ID");          hID.getStyleClass().add("col-id");
        Label hUser = new Label("Username");  hUser.getStyleClass().add("col-username");
        Label hFullname = new Label("Full Name"); hFullname.getStyleClass().add("col-fullname");
        Label hRole = new Label("Role");      hRole.getStyleClass().add("col-role");
        Label hCreate = new Label("Created"); hCreate.getStyleClass().add("col-created");
        Label hLogin = new Label("Last Login"); hLogin.getStyleClass().add("col-login");
        Label hAction = new Label("Actions"); hAction.setMinWidth(130); hAction.setAlignment(Pos.CENTER);
        header.getChildren().addAll(hID, hUser, hFullname, hRole, hCreate, hLogin, hAction);
        accountContainer.getChildren().add(header);

        // render account list, in this case, we must add class to each element helping easier for CSS;
        for (Account acc : accountList) {
            HBox row = new HBox(20);
            row.getStyleClass().add("account-row");
            Label colID = new Label(acc.getAccountID()); colID.getStyleClass().add("col-id");
            Label colUsername = new Label(acc.getUsername()); colUsername.getStyleClass().add("col-username");
            Label colFullname = new Label(acc.getFullname()); colFullname.getStyleClass().add("col-fullname");
            Label colRole = new Label(acc.getRole()); colRole.getStyleClass().add("col-role");
            Label colCreateDate = new Label(acc.getCreateDate() != null
                    ? acc.getCreateDate().format(formatter) : "N/A");
            colCreateDate.getStyleClass().add("col-created");
            Label colLastLogin = new Label(acc.getLastLogin() != null
                    ? acc.getLastLogin().format(formatter) : "No Login");
            colLastLogin.getStyleClass().add("col-login");

            // two action button
            Button btnDelete = new Button("Delete");
            btnDelete.getStyleClass().add("btn-delete");
            btnDelete.setOnAction(e -> {
                try {
                    handleDeleteAccount(acc);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });

            Button btnReset = new Button("Reset");
            btnReset.getStyleClass().add("btn-reset");
            btnReset.setOnAction(e -> {
                try {
                    handleResetAccount(acc);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            });

            // with admin role, reset or delete button must be disabled
            if(acc.getRole().equals("Admin")) {
                btnDelete.setDisable(true);
                btnReset.setDisable(true);
            }

            HBox actionBox = new HBox(10, btnDelete, btnReset);
            actionBox.getStyleClass().add("action-box");

            row.getChildren().addAll(
                    colID, colUsername, colFullname, colRole,
                    colCreateDate, colLastLogin, actionBox
            );

            accountContainer.getChildren().add(row);
        }

        accountPane.setFitToWidth(true);
        accountPane.setContent(accountContainer);
    }

    // delete and reset password function
    private void handleDeleteAccount(Account acc) throws SQLException {
        boolean confirmed = showConfirmation("Delete", acc.getUsername());
        if (confirmed) {
            DatabaseManager.deleteAccount(acc.getAccountID());
            accountPaneRender();
        }
    }

    private void handleResetAccount(Account acc) throws SQLException {
        boolean confirmed = showConfirmation("Reset", acc.getUsername());
        if (confirmed) {
            DatabaseManager.changePassword(acc.getUsername(), "123");
        }
    }

    //alert: confirmation
    public boolean showConfirmation(String action, String username) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(action + " Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to " + action.toLowerCase() +
                " the account '" + username + "'?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    // function create account
    public void createAccountButtonHandle() throws SQLException {
        String newUsername = usernameField.getText();
        boolean checkUsername = accountList.stream().anyMatch(account -> account.getUsername().equals(newUsername));
        if(checkUsername) {
            noticeLabel.setText("*** Account already existed");
            noticeLabel.setStyle("-fx-text-fill: red;");
        } else {
            if(fullnameField.getText().isEmpty()) {
                noticeLabel.setStyle("-fx-text-fill: red;");
                noticeLabel.setText("*** Please tell me what is his/her name.");
            }
            else if (passwordField.getText().isEmpty()){
                noticeLabel.setText("*** Please input password.");
            }
            else {
                noticeLabel.setText("*** Account created :>>>");
                noticeLabel.setStyle("-fx-text-fill: green;");
                DatabaseManager.createAccount(new Account(newUsername, passwordField.getText(),fullnameField.getText(),roleChoiceBox.getValue()));
                accountPaneRender();
            }
        }
    }
    public void goBackButtonHandle() throws SQLException {
        SceneManager.switchTo("optionScene");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roleChoiceBox.getItems().addAll("Student", "Teacher", "Admin");
        roleChoiceBox.setValue("Student");
        try {
            accountPaneRender();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
