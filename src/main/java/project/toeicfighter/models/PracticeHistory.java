package project.toeicfighter.models;

import java.time.LocalDateTime;

public class PracticeHistory {
    private int test_history_id;
    private String userID;
    private int numberOfQuestion;
    private int numberOfCorrectQuestion;
    private LocalDateTime start_time;
    private LocalDateTime end_time;


    public PracticeHistory(int test_history_id, String userID, int numberOfQuestion, int numberOfCorrectQuestion, LocalDateTime start_time, LocalDateTime end_time) {
        this.test_history_id = test_history_id;
        this.userID = userID;
        this.numberOfQuestion = numberOfQuestion;
        this.numberOfCorrectQuestion = numberOfCorrectQuestion;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public int getTest_history_id() {
        return test_history_id;
    }

    public void setTest_history_id(int test_history_id) {
        this.test_history_id = test_history_id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getNumberOfQuestion() {
        return numberOfQuestion;
    }

    public void setNumberOfQuestion(int numberOfQuestion) {
        this.numberOfQuestion = numberOfQuestion;
    }

    public int getNumberOfCorrectQuestion() {
        return numberOfCorrectQuestion;
    }

    public void setNumberOfCorrectQuestion(int numberOfCorrectQuestion) {
        this.numberOfCorrectQuestion = numberOfCorrectQuestion;
    }

    public LocalDateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalDateTime start_time) {
        this.start_time = start_time;
    }

    public LocalDateTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalDateTime end_time) {
        this.end_time = end_time;
    }



}
