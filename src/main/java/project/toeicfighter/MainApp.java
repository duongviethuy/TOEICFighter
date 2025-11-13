package project.toeicfighter;

import javafx.application.Application;
import javafx.stage.Stage;
import project.toeicfighter.cores.SceneManager;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        SceneManager.setStage(stage);
        SceneManager.returnLoginScene();

        stage.setTitle("TOEIC FIGHTER");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
