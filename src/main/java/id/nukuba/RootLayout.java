package id.nukuba;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.SplitPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class RootLayout extends AnchorPane {
    @FXML SplitPane base_pane;
    @FXML AnchorPane right_pane;
    @FXML VBox left_pane;

    private EventHandler mIconDragOverRoot      = null;
    private EventHandler mIconDragDropped       = null;
    private EventHandler mIconDragOverRightPane = null;

    private DragIcon mDragOverIcon = null;

    public RootLayout() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/RootLayout.fxml")
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

        //Add one icon that will be used for the drag-drop process
        //This is added as a child to the root AnchorPane so it can be
        //visible on both sides of the split pane.
        mDragOverIcon = new DragIcon();

        mDragOverIcon.setVisible(false);
        mDragOverIcon.setOpacity(0.65);
        getChildren().add(mDragOverIcon);

        //populate left pane with multiple colored icons for testing
        for (int i = 0; i < 7; i++) {

            DragIcon icn = new DragIcon();

            addDragDetection(icn);

            icn.setType(DragIconType.values()[i]);
            left_pane.getChildren().add(icn);
        }

        buildDragHandlers();

    }

    private void buildDragHandlers() {

        //drag over transition to move widget form left pane to right pane
        mIconDragOverRoot = (EventHandler<DragEvent>) event -> {

            Point2D p = right_pane.sceneToLocal(event.getSceneX(), event.getSceneY());

            if (!right_pane.boundsInLocalProperty().get().contains(p)) {
                mDragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                return;
            }

            event.consume();
        };

        mIconDragOverRightPane = (EventHandler<DragEvent>) event -> {

            event.acceptTransferModes(TransferMode.ANY);

            mDragOverIcon.relocateToPoint(
                    new Point2D(event.getSceneX(), event.getSceneY())
            );

            event.consume();
        };

        mIconDragDropped = (EventHandler<DragEvent>) event -> {

            DragContainer container =
                    (DragContainer) event.getDragboard().getContent(DragContainer.AddNode);

            container.addData("scene_coords",
                    new Point2D(event.getSceneX(), event.getSceneY()));

            ClipboardContent content = new ClipboardContent();
            content.put(DragContainer.AddNode, container);

            event.getDragboard().setContent(content);
            event.setDropCompleted(true);
        };

        this.setOnDragDone( event -> {

            right_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRightPane);
            right_pane.removeEventHandler(DragEvent.DRAG_DROPPED, mIconDragDropped);
            base_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRoot);

            mDragOverIcon.setVisible(false);

            DragContainer container =
                    (DragContainer) event.getDragboard().getContent(DragContainer.AddNode);

            if (container != null) {
                if (container.getValue("scene_coords") != null) {

                    DraggableNode node = new DraggableNode();

                    System.out.println((String) container.getValue("type"));

                    node.setType(DragIconType.valueOf(container.getValue("type")));
                    right_pane.getChildren().add(node);

                    Point2D cursorPoint = container.getValue("scene_coords");

                    node.relocateToPoint(
                            new Point2D(cursorPoint.getX() - 32, cursorPoint.getY() - 32)
                    );
                }
            }

            event.consume();
        });


    }

    private void addDragDetection(DragIcon dragIcon) {

        dragIcon.setOnDragDetected(event -> {
            // set the other drag event handles on their respective objects
            base_pane.setOnDragOver(mIconDragOverRoot);
            right_pane.setOnDragOver(mIconDragOverRightPane);
            right_pane.setOnDragDropped(mIconDragDropped);

            // get a reference to the clicked DragIcon object
            DragIcon icn = (DragIcon) event.getSource();

            //begin drag ops
            mDragOverIcon.setType(icn.getType());
            mDragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));

            ClipboardContent content = new ClipboardContent();
            DragContainer container = new DragContainer();

            container.addData ("type", mDragOverIcon.getType().toString());
            content.put(DragContainer.AddNode, container);

            mDragOverIcon.startDragAndDrop (TransferMode.ANY).setContent(content);
            mDragOverIcon.setVisible(true);
            mDragOverIcon.setMouseTransparent(true);
            event.consume();

        });
    }

}
