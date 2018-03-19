package controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import model.Sketch;
import org.controlsfx.control.Notifications;

//import controller.AbstractDiagramController.Mode;
//import controller.AbstractDiagramController.ToolEnum;
import util.commands.CompoundCommand;
import util.commands.MoveGraphElementCommand;
import view.nodes.AbstractNodeView;
import view.nodes.PackageNodeView;

import java.awt.geom.Point2D;

public class MontiArcController extends AbstractDiagramController{
	// verstehen was die eigentlichen Funktionen hier machen
	@FXML
    public void initialize() {
        super.initialize();
        initToolBarActions();
        initDrawPaneActions();
    }
	void initDrawPaneActions(){

    }

    boolean wasAlreadySelected = false;
    //checken wofür das da ist

    void initNodeActions(AbstractNodeView nodeView) {

    }

    //------------ Init Buttons -------------------------------------------
    private void initToolBarActions() {
    	Image image = new Image("/icons/classw.png");
        createBtn.setGraphic(new ImageView(image));
        createBtn.setText("");

        image = new Image("/icons/packagew.png");
        packageBtn.setGraphic(new ImageView(image));
        packageBtn.setText("");

        image = new Image("/icons/edgew.png");
        edgeBtn.setGraphic(new ImageView(image));
        edgeBtn.setText("");

        image = new Image("/icons/selectw.png");
        selectBtn.setGraphic(new ImageView(image));
        selectBtn.setText("");

        image = new Image("/icons/undow.png");
        undoBtn.setGraphic(new ImageView(image));
        undoBtn.setText("");

        image = new Image("/icons/redow.png");
        redoBtn.setGraphic(new ImageView(image));
        redoBtn.setText("");

        image = new Image("/icons/movew.png");
        moveBtn.setGraphic(new ImageView(image));
        moveBtn.setText("");

        image = new Image("/icons/deletew.png");
        deleteBtn.setGraphic(new ImageView(image));
        deleteBtn.setText("");

        image = new Image("/icons/draww.png");
        drawBtn.setGraphic(new ImageView(image));
        drawBtn.setText("");

        image = new Image("/icons/recow.png");
        recognizeBtn.setGraphic(new ImageView(image));
        recognizeBtn.setText("");

        image = new Image("/icons/micw.png");
        voiceBtn.setGraphic(new ImageView(image));
        voiceBtn.setText("");
        
        image = new Image("/icons/recow.png");
        // needs to be replaced by another picture
        checkValidityBtn.setGraphic(new ImageView(image));
        checkValidityBtn.setText("");

    
  //---------------------- Actions for buttons ----------------------------
	    createBtn.setOnAction(event -> {
	        tool = ToolEnum.CREATE_CLASS;
	        setButtonClicked(createBtn);
	    });
	
	    packageBtn.setOnAction(event -> {
	        tool = ToolEnum.CREATE_PACKAGE;
	        setButtonClicked(packageBtn);
	    });
	
	    edgeBtn.setOnAction(event -> {
	        tool = ToolEnum.EDGE;
	        setButtonClicked(edgeBtn);
	    });
	
	    selectBtn.setOnAction(event -> {
	        tool = ToolEnum.SELECT;
	        setButtonClicked(selectBtn);
	    });
	
	    drawBtn.setOnAction(event -> {
	        tool = ToolEnum.DRAW;
	        setButtonClicked(drawBtn);
	    });
	
	    moveBtn.setOnAction(event -> {
	        setButtonClicked(moveBtn);
	        tool = ToolEnum.MOVE_SCENE;
	    });
	
	    undoBtn.setOnAction(event -> undoManager.undoCommand());
	
	    redoBtn.setOnAction(event -> undoManager.redoCommand());
	
	    deleteBtn.setOnAction(event -> deleteSelected());
	
	    recognizeBtn.setOnAction(event -> recognizeController.recognizeMonti(selectedSketches));
	
	    voiceBtn.setOnAction(event -> {
	        if(voiceController.voiceEnabled){
	            Notifications.create()
	                    .title("Voice disabled")
	                    .text("Voice commands are now disabled.")
	                    .showInformation();
	        } else {
	            Notifications.create()
	                    .title("Voice enabled")
	                    .text("Voice commands are now enabled.")
	                    .showInformation();
	        }
	        voiceController.onVoiceButtonClick();
	
	    });
	    
	    //checkValidityBtn.setOnAction(event -> )
	    // hier müssen wir dann den Parser anschmeißen
    }
}






