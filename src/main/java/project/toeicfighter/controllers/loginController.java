package project.toeicfighter.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import project.toeicfighter.cores.DatabaseManager;
import project.toeicfighter.cores.SceneManager;
import project.toeicfighter.cores.SessionManager;
import project.toeicfighter.models.Account;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class loginController implements Initializable {
    public TextField usernameField;
    public PasswordField passwordField;
    public Button loginButton;
    public Label loginCautionLabel;
    private static ArrayList<Account> accountArrayList = new ArrayList<>();

    public void loginButtonHandle(ActionEvent actionEvent) throws Exception {
        var username = usernameField.getText();
        var password = passwordField.getText();

        Account matchedAccount = accountArrayList.stream().filter(account -> account.getUsername().equals(username) && account.getPassword().equals(password)).findFirst().orElse(null);

        if (matchedAccount != null) {
            SessionManager.setCurrentAccount(matchedAccount);
            DatabaseManager.updateLoginStatus(SessionManager.getCurrentAccount().getUsername());
            optionController optionController = SceneManager.switchTo("optionScene");
            assert optionController != null;
        }

        else {
            loginCautionLabel.setText("Please try again");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            DatabaseManager.getConnection();
            accountArrayList = DatabaseManager.getAccountList();
            SessionManager.clearSession();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
