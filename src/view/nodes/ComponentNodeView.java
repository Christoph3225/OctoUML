package view.nodes;

import view.nodes.PortNodeView;
import model.nodes.ClassNode;
import model.nodes.ComponentNode;
import model.nodes.PortNode;
import util.Constants;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

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
import javafx.scene.shape.Shape;
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
    private Rectangle rectangle2;
    private Shape RecUnion;
    
    private StackPane container;
    //private StackPane titlePane;
    private VBox vbox;

    
    private final int STROKE_WIDTH = 1;

    public ComponentNodeView(ComponentNode node) {
        super(node);
        //setChangeListeners();
        System.out.println("Wir sind in ComponentNodeViewConstructor");
        container = new StackPane();
        rectangle = new Rectangle();
        rectangle2 = new Rectangle();
        vbox = new VBox();
        
//        container.getChildren().addAll(RecUnion, vbox);

        // hier müssen auf jeden Fall die Ports mitgesetzt werden
        // die Ports werden dann auch nicht als eigenständiger Knoten in den Graph gezeichnet
        // man kann überlegen, ob die zugehörigen Klassen nicht einfach rausfliegen. Zumindest die Klasse für die PortNodeView

        initVBox();
        createRectangles();
        Line l = createRectanglesPort();
        changeHeight(node.getHeight());
        changeWidth(node.getWidth());
        System.out.println("Rectangle: " + rectangle);
        System.out.println("Rectangle2: " + rectangle2);
//      der Union Befehl richtet sich komisch aus
        RecUnion =  Shape.union((Shape)rectangle, (Shape)rectangle2);
//        RecUnion =  Shape.intersect((Shape)rectangle, (Shape)rectangle2);
        
        System.out.println("RecUnion: " + RecUnion);
        container.getChildren().addAll(RecUnion, vbox,l);

        initLooks();

        
        
        this.getChildren().add(container);

        this.setTranslateX(node.getTranslateX());
        this.setTranslateY(node.getTranslateY());
    
    }

    private void createRectangles(){
        ComponentNode node = (ComponentNode) getRefNode();
        changeHeight(node.getHeight());
        changeWidth(node.getWidth());
        rectangle.setX(node.getX());
        rectangle.setY(node.getY());
//        for (PortNode p : node.getPorts()) {
////        	Rectangle rec = new Rectangle(p.getX(),p.getY());
////        	rec.setHeight(p.getHeight());
////        	rec.setWidth(p.getWidth());
////        	System.out.println("Rec for port" + p + "looks like" + rec);
//        	PortNodeView pView = new PortNodeView(p);
//        	System.out.println("PortNodeView" + pView);
//        }
    }
    private Line createRectanglesPort(){
        ComponentNode node = (ComponentNode) getRefNode();
        ArrayList<PortNode> pArray = node.getPorts();
        Line l = new Line();
        for (PortNode p : pArray) {
        	changeHeightPort(p.getHeight());
            changeWidthPort(p.getWidth());
            rectangle2.setX(p.getX());
            rectangle2.setY(p.getY());
            System.out.println("Port x Pos" + rectangle2.getX());
            System.out.println("Port y Pos" + rectangle2.getY());
            System.out.println("Port Height" + rectangle2.getHeight());
            System.out.println("Port Width" + rectangle2.getWidth());
            l = new Line(p.getX(),node.getY()- 1/2*node.getHeight() + p.getY(),p.getX()+p.getWidth(),node.getY()- 1/2*node.getHeight() + p.getY());
//            l = new Line(p.getX(),p.getY(),p.getX()+p.getWidth(),p.getY());
            l.setStroke(Color.BLACK);
        }
        return l;
        
        
    }

    private void changeHeight(double height){
        setHeight(height);
        rectangle.setHeight(height);
    }
    private void changeHeightPort(double height){
        setHeight(height);
        rectangle2.setHeight(height);
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
    private void changeWidthPort(double width){
        setWidth(width);
        rectangle2.setWidth(width);
//        container.setMaxWidth(width);
//        container.setPrefWidth(width);
//
//        vbox.setMaxWidth(width);
//        vbox.setPrefWidth(width);
        
        /*title.setMaxWidth(width);
        title.setPrefWidth(width);

        attributes.setMaxWidth(width);
        attributes.setPrefWidth(width);

        operations.setMaxWidth(width);
        operations.setPrefWidth(width);*/
    }

    
    private void initVBox(){
        ComponentNode node = (ComponentNode) getRefNode();

//       Nur für die Box intern 
//        vbox.setPadding(new Insets(5, 0, 5, 0));
//        vbox.setSpacing(5);
        
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
    	RecUnion.setStrokeWidth(STROKE_WIDTH);
    	RecUnion.setFill(Color.LIGHTSKYBLUE);
    	RecUnion.setStroke(Color.BLACK);
    	
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
    	RecUnion.setStrokeWidth(scale);
    }

    public void setFill(Paint p) {
    	RecUnion.setFill(p);
    }

    public void setStroke(Paint p) {
    	RecUnion.setStroke(p);
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
