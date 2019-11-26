package id.nukuba;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.CubicCurve;

import java.io.IOException;
import java.util.UUID;

public class NodeLink extends AnchorPane {
    @FXML CubicCurve node_link;

    public NodeLink() {

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/NodeLink.fxml")
        );

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    @FXML
    private void initialize() {

        System.out.println("first" + node_link.startXProperty());

        node_link.controlX1Property().bind(
                Bindings.add(node_link.startXProperty(), 100)
        );

        node_link.controlX2Property().bind(
                Bindings.add(node_link.endXProperty(), -100)
        );

        node_link.controlY1Property().bind(
                Bindings.add(node_link.startYProperty(), 0)
        );

        node_link.controlY2Property().bind(
                Bindings.add(node_link.endYProperty(), 0)
        );

        //provide a universally unique identifier for this object
        setId(UUID.randomUUID().toString());

    }

    public void setStart(Point2D startPoint) {

        node_link.setStartX(startPoint.getX());
        node_link.setStartY(startPoint.getY());
    }

    public void setEnd(Point2D endPoint) {

        node_link.setEndX(endPoint.getX());
        node_link.setEndY(endPoint.getY());
    }

    public void bindEnds(DraggableNode source, DraggableNode target) {

        node_link.startXProperty().bind(
                Bindings.add(source.layoutXProperty(), (source.getWidth() / 2.0)));

        node_link.startYProperty().bind(
                Bindings.add(source.layoutYProperty(), (source.getWidth() / 2.0)));

        node_link.endXProperty().bind(
                Bindings.add(target.layoutXProperty(), (target.getWidth() / 2.0)));

        node_link.endYProperty().bind(
                Bindings.add(target.layoutYProperty(), (target.getWidth() / 2.0)));

    }
}
