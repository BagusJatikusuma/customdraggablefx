package id.nukuba;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DragIcon extends AnchorPane {
    private DragIconType mType;

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

    public DragIconType getType() { return mType;}

    /**
     * set type of DragonIconType property
     * @param type
     */
    public void setType(DragIconType type) {
        mType = type;

        getStyleClass().clear();
        getStyleClass().add("dragicon");
        switch (mType) {

            case blue:
                getStyleClass().add("icon-blue");
                break;

            case red:
                getStyleClass().add("icon-red");
                break;

            case green:
                getStyleClass().add("icon-green");
                break;

            case grey:
                getStyleClass().add("icon-grey");
                break;

            case purple:
                getStyleClass().add("icon-purple");
                break;

            case yellow:
                getStyleClass().add("icon-yellow");
                break;

            case black:
                getStyleClass().add("icon-black");
                break;

            default:
                break;
        }
    }

    public void relocateToPoint(Point2D point2D) {

        Point2D localCoords = getParent().sceneToLocal(point2D);

        relocate (
                (int) (localCoords.getX() -
                        (getBoundsInLocal().getWidth() / 2)),
                (int) (localCoords.getY() -
                        (getBoundsInLocal().getHeight() / 2))
        );

    }


}
