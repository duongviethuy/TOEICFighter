package project.toeicfighter.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import project.toeicfighter.cores.DatabaseManager;
import project.toeicfighter.cores.SceneManager;
import project.toeicfighter.models.Question;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class editQuestionController implements Initializable {
    @FXML
    private TextArea questionContentField, hintField;
    @FXML
    private TextArea answerAField, answerBField,  answerCField, answerDField;
    public Button goBackButton;

    public Label questionIDLabel;
    public Label noticeLabel;

    @FXML
    private ChoiceBox<String> correctAnswerBox = new ChoiceBox<>();
    private ArrayList<Question> questionsList;
    private int questionIndex = 0;

    Question question;

    public void renderAQuestion(int questionIndex) {
        question = questionsList.get(questionIndex);
        questionContentField.setText(question.getQuestion());
        answerAField.setText(question.getAnswerA());
        answerBField.setText(question.getAnswerB());
        answerCField.setText(question.getAnswerC());
        answerDField.setText(question.getAnswerD());
        hintField.setText(question.getHint());
        correctAnswerBox.getItems().clear();
        correctAnswerBox.getItems().addAll("A", "B", "C", "D");
        correctAnswerBox.setValue(question.getCorrectAnswer());
        questionIDLabel.setText("Question's ID: " + question.getId());
    }

    public void nextButtonHandle() {
        if(questionIndex < questionsList.size() - 1) {
            questionIndex++;
            renderAQuestion(questionIndex);
            noticeLabel.setText("");
        }
        else {
            return;
        }
    }

    public void previousButtonHandle() {
        if(questionIndex == 0) { return; }
        else {
            questionIndex--;
            renderAQuestion(questionIndex);
            noticeLabel.setText("");
        }
    }

    public void deleteButtonHandle() throws SQLException {
        if(showDeleteConfirmation(questionsList.get(questionIndex).getId())) {
            DatabaseManager.deleteAQuestion(questionsList.get(questionIndex).getId());
            questionsList = DatabaseManager.getAllQuestion();
            if(questionIndex > questionsList.size() - 1)
                renderAQuestion(questionIndex - 1);
        }
    }

    public boolean showDeleteConfirmation(int questionID) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this question?");
        alert.setContentText("This action cannot be undone.");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public void applyButtonHandle() throws SQLException {
        String content = questionContentField.getText();
        String answerA = answerAField.getText();
        String answerB = answerBField.getText();
        String answerC = answerCField.getText();
        String answerD = answerDField.getText();
        String hint = hintField.getText();
        String correctAnswer = correctAnswerBox.getValue();
        if(content.isEmpty() || answerA.isEmpty() || answerB.isEmpty() || answerC.isEmpty() || answerD.isEmpty()) {
            noticeLabel.setText("Make sure everything is OK :>>>");
            noticeLabel.setStyle("-fx-text-fill: red;");
        }
        else {
            noticeLabel.setText("Update Successfully :>>>");
            noticeLabel.setStyle("-fx-text-fill: green;");
            DatabaseManager.updateAQuestion(question.getId(), new Question(content, answerA, answerB, answerC, answerD, correctAnswer, hint, question.getAddbyID()));
            questionsList = DatabaseManager.getAllQuestion();
        }
    }

    public void goBackButtonHandle() throws SQLException {
        SceneManager.switchTo("optionScene");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            questionsList = DatabaseManager.getAllQuestion();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        renderAQuestion(questionIndex);
    }

}
