package project.toeicfighter.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import project.toeicfighter.cores.DatabaseManager;
import project.toeicfighter.cores.SceneManager;
import project.toeicfighter.cores.SessionManager;
import project.toeicfighter.models.Question;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class addQuestionController implements Initializable{
    public TextArea questionContentField, hintField;
    public TextArea answerAField, answerBField,  answerCField, answerDField;
    public Button goBackButton, checkQuestionButton, addButton;

    public Label noticeLabel;

    @FXML
    private ChoiceBox<String> correctAnswerBox = new ChoiceBox<>();

    Question newQuestion;

    // in this function, it will check SPACE in each text, is all field are filled up ?
    // in the future, I will develop it, ask ChatGPT or anything else :vv
    public void checkQuestionButtonHandle(){
        String content = questionContentField.getText();
        String answerA = answerAField.getText();
        String answerB = answerBField.getText();
        String answerC = answerCField.getText();
        String answerD = answerDField.getText();
        String hint = hintField.getText();
        String correctAnswer = correctAnswerBox.getValue();
        newQuestion = fixAQuestion(new Question(content, answerA, answerB, answerC, answerD, correctAnswer, hint, SessionManager.getCurrentAccount().getAccountID()));
        if(content.isEmpty() || answerA.isEmpty() || answerB.isEmpty() || answerC.isEmpty() || answerD.isEmpty()) {
            noticeLabel.setText("Make sure all text fields below are filled in");
            addButton.setDisable(true);
        }
        else {
            addButton.setDisable(false);
        }
    }

    public void addButtonHandle() throws SQLException {
        DatabaseManager.addAQuestion(newQuestion);
        clearCurrent();
        addButton.setDisable(true);
    }

    public void goBackButtonHandle() throws SQLException {
        SceneManager.switchTo("optionScene");
    }

    // function to remove Double Spaces in question
    public static String removeDoubleSpaces(String input) {
        if (input == null) return null;
        return input.replaceAll(" {2,}", " ");
    }

    public static Question fixAQuestion(Question question) {
        question.setQuestion(removeDoubleSpaces(question.getQuestion()).trim());
        question.setAnswerA(removeDoubleSpaces(question.getAnswerA()).trim());
        question.setAnswerB(removeDoubleSpaces(question.getAnswerB()).trim());
        question.setAnswerC(removeDoubleSpaces(question.getAnswerC()).trim());
        question.setAnswerD(removeDoubleSpaces(question.getAnswerD()).trim());
        question.setHint(removeDoubleSpaces(question.getHint()).trim());
        return question;
    }

    public void clearCurrent() {
        questionContentField.clear();
        answerAField.clear();
        answerBField.clear();
        answerCField.clear();
        answerDField.clear();
        hintField.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addButton.setDisable(true);
        correctAnswerBox.getItems().addAll("A", "B", "C", "D");
        correctAnswerBox.setValue("A");
    }

}
