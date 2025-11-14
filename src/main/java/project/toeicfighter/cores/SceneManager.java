package project.toeicfighter.cores;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

// ===== SCENE MANAGER =====
// All functions in this class help you switch to another screen, but you must provide a filename :>>>>

public class SceneManager {
    private static Stage stage;
    public static void setStage(Stage s) {
        stage = s;
    }
    public static void returnLoginScene() throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(SceneManager.class.getResource("/project/toeicfighter/views/loginScene.fxml")));
        if(stage.getScene() == null) {
            stage.setScene(new Scene(root));
        }
        else {
            stage.getScene().setRoot(root);
            SessionManager.clearSession();
        }
    }
    public static <T> T switchTo(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(SceneManager.class.getResource("/project/toeicfighter/views/" + fxmlFile + ".fxml")));
            Parent root = loader.load();
            Object controller = loader.getController();
            if(stage.getScene() == null) {
                stage.setScene(new Scene(root));
            }
            else {
                stage.getScene().setRoot(root);
            }
            return loader.getController();
        } catch (Exception e) {
            e.getLocalizedMessage();
            return null;
        }
    }
}
