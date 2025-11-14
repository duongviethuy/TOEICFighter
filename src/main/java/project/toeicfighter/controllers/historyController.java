package project.toeicfighter.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import project.toeicfighter.cores.DatabaseManager;
import project.toeicfighter.cores.SceneManager;
import project.toeicfighter.models.Account;
import project.toeicfighter.models.PracticeHistory;

import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class historyController implements Initializable {
    public ScrollPane scrollPaneHistory;
    @FXML
    private ChoiceBox<Integer> recordCountBox = new ChoiceBox<>();
    @FXML
    private ChoiceBox<String> findByUser = new ChoiceBox<>();
    public static Button goBackButton;
    public static Button goButton;

    @FXML
    private VBox vBoxHistory;

    ArrayList<PracticeHistory> practiceHistories;
    ArrayList<Account> userList;

    private void renderHistoryVBox(ArrayList<PracticeHistory> preparedHistoryList) throws SQLException {
        vBoxHistory.getChildren().clear();

        if (preparedHistoryList.isEmpty()) {
            Label noDataLabel = new Label("NO DATA");
            noDataLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: red;");

            // Căn giữa VBox
            vBoxHistory.setAlignment(Pos.CENTER);
            vBoxHistory.getChildren().add(noDataLabel);
            return;
        }

        // Header như cũ
        HBox header = new HBox(20);
        header.setPrefWidth(720);
        header.getStyleClass().add("table-header");
        Label hNo = new Label("No.");           hNo.getStyleClass().addAll("label-no");
        Label hName = new Label("Full Name");  hName.getStyleClass().add("label-name");
        Label hStart = new Label("Start Time"); hStart.getStyleClass().add("label-starttime");
        Label hDuration = new Label("Duration"); hDuration.getStyleClass().add("label-duration");
        Label hQ = new Label("Question");       hQ.getStyleClass().add("label-question");
        Label hCorrect = new Label("Correct");  hCorrect.getStyleClass().add("label-correct");
        Label hScore = new Label("Score");      hScore.getStyleClass().add("label-score");
        header.getChildren().addAll(hNo,hName,hStart,hDuration,hQ,hCorrect,hScore);
        vBoxHistory.getChildren().add(header);

        int count = 1;

        for (PracticeHistory history : preparedHistoryList) {
            HBox row = new HBox(20); // spacing giữa các label
            row.setPrefWidth(720);   // cố định chiều ngang
            row.getStyleClass().add("table-label");

            Label labelNo = new Label(String.valueOf(count));
            labelNo.getStyleClass().add("label-no");

            Label labelName = new Label(DatabaseManager.getFullNameByUserID(history.getUserID()));
            labelName.getStyleClass().add("label-name");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            Label labelStartTime = new Label(history.getStart_time().format(formatter));
            labelStartTime.getStyleClass().add("label-starttime");

            long seconds = Duration.between(history.getStart_time(), history.getEnd_time()).getSeconds();
            long min = seconds / 60;
            long sec = seconds % 60;
            String time = min + "m " + sec + "s";
            Label labelDuration = new Label(time);
            labelDuration.getStyleClass().add("label-duration");

            Label labelNumberOfQuestion = new Label(String.valueOf(history.getNumberOfQuestion()));
            labelNumberOfQuestion.getStyleClass().add("label-question");

            Label labelNumberOfCorrectQuestion = new Label(String.valueOf(history.getNumberOfCorrectQuestion()));
            labelNumberOfCorrectQuestion.getStyleClass().add("label-correct");

            double score = ((double) history.getNumberOfCorrectQuestion() / history.getNumberOfQuestion()) * 100;
            Label labelScore = new Label(String.format("%.2f%%", score));
            labelScore.getStyleClass().add("label-score");

            row.getChildren().addAll(
                    labelNo, labelName, labelStartTime, labelDuration,
                    labelNumberOfQuestion, labelNumberOfCorrectQuestion, labelScore
            );

            vBoxHistory.getChildren().add(row);
            count++;
        }

    }

    public void goButtonHandle() throws SQLException {
        vBoxHistory.setVisible(true);
        ArrayList<PracticeHistory> preparedHistoryList = new ArrayList<>();
        String queryUser = findByUser.getValue().trim();
        int numberOfRecord = recordCountBox.getValue();
        int count = 0;
        if (queryUser.equals("Everyone")) {
            preparedHistoryList = practiceHistories.stream().limit(numberOfRecord).collect(Collectors.toCollection(ArrayList::new));
            renderHistoryVBox(preparedHistoryList);
        }
        else {
            Account user = userList.stream()
                    .filter(a -> a.getFullname().equals(queryUser))
                    .findFirst()
                    .orElse(null);
            for(PracticeHistory history : practiceHistories) {
                assert user != null;
                if(history.getUserID().equals(user.getAccountID())) {
                    if(count == numberOfRecord)
                        break;
                    preparedHistoryList.add(history);
                    renderHistoryVBox(preparedHistoryList);
                    count++;
                }
            }
        }
    }

    public void goBackButtonHandle() throws SQLException {
        SceneManager.switchTo("optionScene");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            practiceHistories = DatabaseManager.getPracticeHistory();
            userList = DatabaseManager.getAccountList();
            Collections.reverse(practiceHistories);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        vBoxHistory.setVisible(false);
        recordCountBox.getItems().addAll(5, 10, 15, 20);
        recordCountBox.setValue(5);
        findByUser.getItems().add("Everyone");
        userList.forEach(account -> findByUser.getItems().add(account.getFullname()));
        findByUser.setValue("Everyone");
    }
}
