package project.toeicfighter.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import project.toeicfighter.cores.DatabaseManager;
import project.toeicfighter.cores.SceneManager;
import project.toeicfighter.cores.SessionManager;
import project.toeicfighter.models.Question;

import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class practiceController implements Initializable {
    private static class AnswerSheet {
        int questionNo;
        String userAnswer;
        String correctAnswer;
    }

    AnswerSheet[] answerSheets;
    ArrayList<ToggleGroup> userAnswer = new ArrayList<>();

    public static Button exitButton;
    @FXML
    private Button goPracticeButton;
    @FXML
    private Button finishButton;
    @FXML
    private TabPane questionPane;
    @FXML
    private ChoiceBox<Integer> questionCountBox = new ChoiceBox<>();
    @FXML
    private ProgressBar testScoreScale;
    @FXML
    private Label totalScoreLabel;
    LocalDateTime startTime;

    // Disable these buttons to make sure nothing will be changed while practicing;
    public void goPracticeButtonHandle() throws SQLException {
        questionCountBox.setDisable(true);
        goPracticeButton.setDisable(true);
        finishButton.setDisable(false);
        PracticePaneRender();
        startTime = LocalDateTime.now();
    }

    // Render a pane, and then practice in there
    public void PracticePaneRender() throws SQLException {
        int numberOfQuestion = questionCountBox.getValue();
        ArrayList<Question> questionList = DatabaseManager.getRandomQuestions(numberOfQuestion);
        answerSheets = new AnswerSheet[numberOfQuestion];
        int count = 0;
        for(Question question : questionList) {
            //Create Vbox as a NODE
            VBox questionTabBox = new VBox();

            //Create content for question tab
            Label questionContent = new Label();
            questionContent.setText(count + 1 + ". " +question.getQuestion());
            questionContent.setFont(Font.font("", FontWeight.BOLD, 16));
            questionContent.setWrapText(true);

            //Create 4 answer for question and add them to a group
            ToggleGroup group = new ToggleGroup();

            RadioButton answerA = new RadioButton("A. " + question.getAnswerA());
            RadioButton answerB = new RadioButton("B. " + question.getAnswerB());
            RadioButton answerC = new RadioButton("C. " + question.getAnswerC());
            RadioButton answerD = new RadioButton("D. " + question.getAnswerD());

            answerA.getStyleClass().add("answer");
            answerB.getStyleClass().add("answer");
            answerC.getStyleClass().add("answer");
            answerD.getStyleClass().add("answer");

            answerA.setToggleGroup(group);
            answerB.setToggleGroup(group);
            answerC.setToggleGroup(group);
            answerD.setToggleGroup(group);

            userAnswer.add(group);

            answerSheets[count] = new AnswerSheet();
            answerSheets[count].questionNo = count;
            answerSheets[count].correctAnswer = question.getCorrectAnswer();

            //Create a tab.
            Tab questionTab = new Tab();
            questionTab.setText(String.valueOf(count + 1));
            questionTabBox.getChildren().addAll(questionContent, answerA, answerB, answerC, answerD);
            questionTab.setContent(questionTabBox);

            questionPane.getTabs().add(questionTab);
            count++;
        }
    }

    // After finishing the practice, user click finish, it will call this function
    // below and save user's record to database
    public void finishButtonHandle() throws SQLException {
        int correctAnswerCount = 0;
        for(int i = 0; i < userAnswer.size(); i++) {
            ToggleGroup group = userAnswer.get(i);
            Toggle selected = group.getSelectedToggle();
            RadioButton answer = (RadioButton) selected;
            if(answer != null)
                // Collect A, B, C or D from the full answer;
                answerSheets[i].userAnswer = answer.getText().substring(0, 1);
            else answerSheets[i].userAnswer = "E";

            Label correctLabel = new Label();
            if (answerSheets[i].userAnswer.trim().equals(answerSheets[i].correctAnswer.trim())) {
                correctLabel.setText("You are correct !");
                correctLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                correctAnswerCount++;
            }
            else {
                correctLabel.setText("Correct answer: " + answerSheets[i].correctAnswer);
                correctLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            }

            Tab tab = questionPane.getTabs().get(i);
            VBox tabContent = (VBox) tab.getContent();
            tabContent.getChildren().add(correctLabel);
        }
        for (ToggleGroup group : userAnswer) {
            for (Toggle toggle : group.getToggles()) {
                ((RadioButton) toggle).setDisable(true);
            }
        }
        finishButton.setDisable(true);
        double progress = (double) correctAnswerCount / userAnswer.size();
        testScoreScale.setVisible(true);
        testScoreScale.setProgress(progress);
        LocalDateTime endTime = LocalDateTime.now();
        long seconds = Duration.between(startTime, endTime).getSeconds();
        long minutes = seconds / 60;
        long sec = seconds % 60;

        totalScoreLabel.setText(String.format("%.2f", progress * 100) + "% - " + correctAnswerCount + "/" + userAnswer.size() + " in " + minutes + "m " + sec + "s");

        // save to database
        DatabaseManager.saveToHistory(SessionManager.getCurrentAccount().getAccountID(), userAnswer.size(), correctAnswerCount, startTime,endTime);
    }

    public void exitButtonHandle() {
        SceneManager.switchTo("optionScene");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questionPane.getStyleClass().add("custom-tabpane");
        questionPane.getTabs().clear();
        userAnswer.clear();
        questionCountBox.getItems().addAll(5, 10, 20);
        questionCountBox.setValue(5);
        finishButton.setDisable(true);
        testScoreScale.setVisible(false);
    }
}
