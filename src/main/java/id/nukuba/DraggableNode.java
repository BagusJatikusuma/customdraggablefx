package id.nukuba;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.UUID;

public class DraggableNode extends AnchorPane {

    @FXML AnchorPane root_pane;

    private EventHandler  mContextDragOver;
    private EventHandler  mContextDragDropped;

    private DragIconType mType = null;

    private Point2D mDragOffset = new Point2D(0.0, 0.0);

    @FXML private Label title_bar;
    @FXML private Label close_button;

    @FXML AnchorPane left_link_handle;
    @FXML AnchorPane right_link_handle;

    private EventHandler <MouseEvent> mLinkHandleDragDetected;
    private EventHandler <DragEvent> mLinkHandleDragDropped;
    private EventHandler <DragEvent> mContextLinkDragOver;
    private EventHandler <DragEvent> mContextLinkDragDropped;

    private NodeLink mDragLink = null;
    private AnchorPane right_pane = null;

    private final DraggableNode self;

    public DraggableNode() {
        self = this;

        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/DraggableNode.fxml")
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

        buildNodeDragHandlers();
        buildLinkDragHandlers();

        left_link_handle.setOnDragDetected(mLinkHandleDragDetected);
        right_link_handle.setOnDragDetected(mLinkHandleDragDetected);

        left_link_handle.setOnDragDropped(mLinkHandleDragDropped);
        right_link_handle.setOnDragDropped(mLinkHandleDragDropped);

        mDragLink = new NodeLink();
        mDragLink.setVisible(false);

        parentProperty().addListener((observableValue, parent, t1) -> right_pane = (AnchorPane) getParent());

        //provide a universally unique identifier for this object
        setId(UUID.randomUUID().toString());

    }

    private void buildNodeDragHandlers() {

        //dragover to handle node dragging in the right pane view
        mContextDragOver = (EventHandler<DragEvent>) event -> {

            event.acceptTransferModes(TransferMode.ANY);
            relocateToPoint(new Point2D( event.getSceneX(), event.getSceneY()));

            event.consume();
        };

        //dragdrop for node dragging
        mContextDragDropped = (EventHandler<DragEvent>) event -> {

            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);

            event.setDropCompleted(true);

            event.consume();
        };

        //close button click
        close_button.setOnMouseClicked(event -> {

            AnchorPane parent  = (AnchorPane) self.getParent();
            parent.getChildren().remove(self);
        });

        //drag detection for node dragging
        title_bar.setOnDragDetected ((EventHandler<MouseEvent>) event -> {

            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);

            getParent().setOnDragOver (mContextDragOver);
            getParent().setOnDragDropped (mContextDragDropped);

            //begin drag ops
            mDragOffset = new Point2D(event.getX(), event.getY());

            relocateToPoint (new Point2D(event.getSceneX(), event.getSceneY()));

            ClipboardContent content = new ClipboardContent();
            DragContainer container = new DragContainer();

            container.addData ("type", mType.toString());
            content.put(DragContainer.DragNode, container);

            startDragAndDrop (TransferMode.ANY).setContent(content);

            event.consume();
        });
    }

    public void relocateToPoint (Point2D p) {

        //relocates the object to a point that has been converted to
        //scene coordinates
        Point2D localCoords = getParent().sceneToLocal(p);

        relocate (
                (int) (localCoords.getX() - mDragOffset.getX()),
                (int) (localCoords.getY() - mDragOffset.getY())
        );
    }

    public DragIconType getType () { return mType; }

    public void setType (DragIconType type) {

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

    private void buildLinkDragHandlers() {

        mLinkHandleDragDetected = event -> {

            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);

            getParent().setOnDragOver(mContextLinkDragOver);
            getParent().setOnDragDropped(mLinkHandleDragDropped);

            //Set up user-draggable link
            right_pane.getChildren().add(0,mDragLink);

            mDragLink.setVisible(false);

            Point2D p = new Point2D(
                    getLayoutX() + (getWidth() / 2.0),
                    getLayoutY() + (getHeight() / 2.0)
            );

            mDragLink.setStart(p);

            //Drag content code
            ClipboardContent content = new ClipboardContent();
            DragContainer container = new DragContainer ();

            AnchorPane link_handle = (AnchorPane) event.getSource();
            DraggableNode parent = (DraggableNode) link_handle.getParent().getParent().getParent();

            container.addData("source", getId());

            content.put(DragContainer.AddLink, container);

            parent.startDragAndDrop (TransferMode.ANY).setContent(content);

            event.consume();
        };

        mLinkHandleDragDropped = event -> {
            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);

            //get the drag data.  If it's null, abort.
            //This isn't the drag event we're looking for.
            DragContainer container =
                    (DragContainer) event.getDragboard().getContent(DragContainer.AddLink);

            if ( container == null ) {
                System.out.println( "container is null" );
                return;
            }

            AnchorPane link_handle = (AnchorPane) event.getSource();
            DraggableNode parent;

            if ( link_handle.getParent().getParent().getParent() instanceof DraggableNode ) {
                parent = (DraggableNode) link_handle.getParent().getParent().getParent();
            } else {
                System.out.println("target of node link is not found");
                return;
            }

            ClipboardContent content = new ClipboardContent();

            container.addData("target", getId());

            content.put(DragContainer.AddLink, container);

            event.getDragboard().setContent(content);

            //hide the draggable NodeLink and remove it from the right-hand AnchorPane's children
            if ( mDragLink.isVisible() ) System.out.println("draglink dropped is visible");
            else System.out.println("draglink dropped is not visible");

            mDragLink.setVisible(false);
            right_pane.getChildren().remove(0);

            event.setDropCompleted(true);

            event.consume();
        };

        mContextLinkDragOver = event -> {
            event.acceptTransferModes(TransferMode.ANY);

            //Relocate user-draggable link
            if (!mDragLink.isVisible())
                mDragLink.setVisible(true);

            System.out.println("link is dragged : " + event.getX() + ", " + event.getY());

            mDragLink.setEnd(new Point2D(event.getX(), event.getY()));

            event.consume();

        };

        mContextLinkDragDropped = event -> {

            getParent().setOnDragOver(null);
            getParent().setOnDragDropped(null);

            //hide the draggable NodeLink and remove it from the right-hand AnchorPane's children
            if ( mDragLink.isVisible() ) System.out.println("draglink dropped context is visible");
            else System.out.println("draglink dropped context is not visible");

            mDragLink.setVisible(false);
            right_pane.getChildren().remove(0);

            event.setDropCompleted(true);
            event.consume();
        };
    }

}
