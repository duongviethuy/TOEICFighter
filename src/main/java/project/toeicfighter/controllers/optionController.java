package project.toeicfighter.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Pair;
import project.toeicfighter.cores.DatabaseManager;
import project.toeicfighter.cores.SceneManager;
import project.toeicfighter.cores.SessionManager;
import project.toeicfighter.models.Account;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class optionController implements Initializable {

    public Label usernameLabel;
    public Button praticeButton;
    public Button addQuestionButton;
    public Button editQuestionButton;
    public Button practiceHistoryButton;
    public Button accountCenterButton;
    public Button changePasswordButton;
    public Button logoutButton;
    public Label loginWithAdminRoleLabel;

    public Account account;


    public void setAccount(Account acc) {
        account = acc;
        setUsernameLabel();
        disableFunction();
    }

    void setUsernameLabel() {
        usernameLabel.setText(account.getRole() + " " + account.getFullname());
    }

    void disableFunction() {
        if(account.getRole().equals("Student")) {
            praticeButton.setDefaultButton(true);
            accountCenterButton.setDisable(true);
            addQuestionButton.setDisable(true);
            editQuestionButton.setDisable(true);
            loginWithAdminRoleLabel.setText("Note: You can log in with an admin account to unlock all features");
        }
        if(account.getRole().equals("Teacher")) {
            accountCenterButton.setDisable(true);
            loginWithAdminRoleLabel.setText("Note: You can log in with an admin account to unlock all features");
        }
    }

    public void addQuestionButtonHandle(ActionEvent event) {
        SceneManager.switchTo("addQuestionScene");
    }
    public void practiceButtonHandle(ActionEvent event) {SceneManager.switchTo("practiceScene");}

    public void practiceHistoryButtonHandle(ActionEvent event) throws Exception {
        SceneManager.switchTo("historyScene");
    }

    public void editQuestionButtonHandle(ActionEvent event) throws Exception{
        SceneManager.switchTo("editQuestionScene");
    }
    public void logoutButtonHandle(ActionEvent event) throws Exception {
        SceneManager.returnLoginScene();
    }

    public void changePasswordButtonHandle() throws Exception {
        boolean valid = false; // để kiểm tra mật khẩu cũ
        while (!valid) {
            // Tạo dialog
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Change Password");
            dialog.setHeaderText("Enter your current and new password");

            ButtonType okButtonType = new ButtonType("Change", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            PasswordField currentPassword = new PasswordField();
            currentPassword.setPromptText("Current password");
            PasswordField newPassword = new PasswordField();
            newPassword.setPromptText("New password");

            grid.add(new Label("Current:"), 0, 0);
            grid.add(currentPassword, 1, 0);
            grid.add(new Label("New:"), 0, 1);
            grid.add(newPassword, 1, 1);

            GridPane.setHgrow(currentPassword, Priority.ALWAYS);
            GridPane.setHgrow(newPassword, Priority.ALWAYS);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {
                    return new Pair<>(currentPassword.getText(), newPassword.getText());
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();

            if (result.isPresent()) {
                String current = result.get().getKey();
                String newPass = result.get().getValue();

                if (current.equals(SessionManager.getCurrentAccount().getPassword())) {
                    if(newPass.isEmpty()) {
                        showAlert(Alert.AlertType.INFORMATION, "New password can not Empty!");
                    }
                    else {
                        // Change password
                        DatabaseManager.changePassword(account.getUsername(), newPass);
                        valid = true;
                        showAlert(Alert.AlertType.INFORMATION, "Password changed successfully, please login again");
                        SceneManager.returnLoginScene();
                    }
                } else {
                    // Nếu sai → thông báo lỗi và lặp lại
                    showAlert(Alert.AlertType.ERROR, "Incorrect current password! Try again.");
                }
            }
            else {
                return;
            }
        }

    }


    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Account currentAccount = SessionManager.getCurrentAccount();
        setAccount(currentAccount);
        setUsernameLabel();
    }
}
