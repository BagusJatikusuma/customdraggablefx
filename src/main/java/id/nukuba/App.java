package id.nukuba;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        BorderPane root = new BorderPane();

        scene = new Scene(root, 640,480);
        stage.setScene(scene);
        stage.show();

        root.setCenter(new RootLayout());
    }

    public static void main(String[] args) {
        launch();
    }

}