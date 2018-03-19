package view.nodes;

import view.nodes.PortNodeView;
import model.nodes.ClassNode;
import model.nodes.ComponentNode;
import util.Constants;

import java.beans.PropertyChangeEvent;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight; 

public class ComponentNodeView extends AbstractNodeView{
// Visuell Representation of a ComponentNode
	
	// Shape C = Shape.union(A, B);
	// Wir erzeugen ein Array von Ports, sorgen dafür, dass die alle vernünftig ausschauen als Rechtecke
	// dann erzeugen wir das Rechteck für die Component
	// dann rufen wir UNION darauf auf und erhalten eine hoffentlich passende Komponente
	// Problem könnte sein, dass die Linien nicht mehr gezeigt werden
	
	// dann können wir auch eine Klasse PortNodeView erzeugen. Die kann dann fast das gleiche wie die ComponentView,
	// nur das in der ComponentView der Union stattfindet
	//private Label title;
    //private Label attributes;
    //private Label operations;

    private Rectangle rectangle;

    private StackPane container;
    //private StackPane titlePane;
    private VBox vbox;

    
    private final int STROKE_WIDTH = 1;

    public ComponentNodeView(ComponentNode node) {
        super(node);
        //setChangeListeners();

        container = new StackPane();
        rectangle = new Rectangle();
        vbox = new VBox();
        container.getChildren().addAll(rectangle, vbox);


        initVBox();
        createRectangles();
        changeHeight(node.getHeight());
        changeWidth(node.getWidth());
        initLooks();

        this.getChildren().add(container);

        this.setTranslateX(node.getTranslateX());
        this.setTranslateY(node.getTranslateY());
        

    }

    private void createRectangles(){
        ClassNode node = (ClassNode) getRefNode();
        changeHeight(node.getHeight());
        changeWidth(node.getWidth());
        rectangle.setX(node.getX());
        rectangle.setY(node.getY());
    }

    private void changeHeight(double height){
        setHeight(height);
        rectangle.setHeight(height);
    }

    private void changeWidth(double width){
        setWidth(width);
        rectangle.setWidth(width);
        container.setMaxWidth(width);
        container.setPrefWidth(width);

        vbox.setMaxWidth(width);
        vbox.setPrefWidth(width);
        
        /*title.setMaxWidth(width);
        title.setPrefWidth(width);

        attributes.setMaxWidth(width);
        attributes.setPrefWidth(width);

        operations.setMaxWidth(width);
        operations.setPrefWidth(width);*/
    }

    
    private void initVBox(){
        ComponentNode node = (ComponentNode) getRefNode();

        vbox.setPadding(new Insets(5, 0, 5, 0));
        vbox.setSpacing(5);

//        titlePane = new StackPane();

        
//        title = new Label();
//        title.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
//        if(node.getTitle() != null) {
//            title.setText(node.getTitle());
//        }
//        title.setAlignment(Pos.CENTER);
//
//        attributes = new Label(node.getAttributes());
//        attributes.setFont(Font.font("Verdana", 10));
//
//        operations = new Label(node.getOperations());
//        operations.setFont(Font.font("Verdana", 10));
//
//        titlePane.getChildren().add(title);
//        vbox.getChildren().addAll(titlePane, attributes, operations);
    }

    private void initLooks(){
        rectangle.setStrokeWidth(STROKE_WIDTH);
        rectangle.setFill(Color.LIGHTSKYBLUE);
        rectangle.setStroke(Color.BLACK);
//        StackPane.setAlignment(title, Pos.CENTER);
//        VBox.setMargin(attributes, new Insets(5,0,0,5));
//        VBox.setMargin(operations, new Insets(5,0,0,5));
    }

    public void setSelected(boolean selected){
        if(selected){
            rectangle.setStrokeWidth(2);
            setStroke(Constants.selected_color);
        } else {
            rectangle.setStrokeWidth(1);
            setStroke(Color.BLACK);
        }
    }

    public void setStrokeWidth(double scale){
        rectangle.setStrokeWidth(scale);
    }

    public void setFill(Paint p) {
        rectangle.setFill(p);
    }

    public void setStroke(Paint p) {
        rectangle.setStroke(p);
    }

    public Bounds getBounds(){
        return container.getBoundsInParent();
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        super.propertyChange(evt);
        if (evt.getPropertyName().equals(Constants.changeNodeX)) {
            setX((double) evt.getNewValue());
        } else if (evt.getPropertyName().equals(Constants.changeNodeY)) {
            setY((double) evt.getNewValue());
        } else if (evt.getPropertyName().equals(Constants.changeNodeWidth)) {
            changeWidth((double) evt.getNewValue());
        } else if (evt.getPropertyName().equals(Constants.changeNodeHeight)) {
            changeHeight((double) evt.getNewValue());
//        } else if (evt.getPropertyName().equals(Constants.changeNodeTitle)) {
//            title.setText((String) evt.getNewValue());
//        } else if (evt.getPropertyName().equals(Constants.changeClassNodeAttributes)) {
//            attributes.setText((String) evt.getNewValue());
//        } else if (evt.getPropertyName().equals(Constants.changeClassNodeOperations)) {
//            operations.setText((String) evt.getNewValue());
        }
    }

}
