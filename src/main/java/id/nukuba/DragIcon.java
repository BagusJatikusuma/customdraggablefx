package id.nukuba;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DragIcon extends AnchorPane {

    public DragIcon() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/DragIcon.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();

            throw new RuntimeException(e);
        }
    }

    @FXML
    private void initialize() {
    }

}
