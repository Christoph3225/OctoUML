package controller;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Optional;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import util.POMEditor;


public class StartPlugInController {
	
	@FXML
	VBox otherPropsBox;
	
	@FXML
    Button classDiagramButton, sequenceDiagramButton, otherDiagramButton, startModelingButton, chooseFolderButton, insertDependencyButton;
    @FXML
    Label titleLabel, subTitleLabel, folderPathLabel;

    TabController tabController;
    Stage stage;
    
    private String currDependency;

    @FXML
    public void initialize() {
    		Image icon = new Image("/icons/classDiagram.PNG");
        classDiagramButton.setGraphic(new ImageView(icon));
        classDiagramButton.setContentDisplay(ContentDisplay.BOTTOM);

        icon = new Image("/icons/sequenceDiagram.PNG");
        sequenceDiagramButton.setGraphic(new ImageView(icon));
        sequenceDiagramButton.setContentDisplay(ContentDisplay.BOTTOM);
        
        icon = new Image("/icons/otherDiagram.PNG");
        otherDiagramButton.setGraphic(new ImageView(icon));
        otherDiagramButton.setContentDisplay(ContentDisplay.BOTTOM);
        
    		otherPropsBox.setVisible(false);
    	
        titleLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 60));
        titleLabel.setTextFill(Color.web("#3F4144"));

        subTitleLabel.setFont(Font.font("Verdana", 30));
    }

    public void handleActionNewOtherDiagram() {
    		otherPropsBox.setVisible(true);
    }
    
    public void handleActionChooseFolder() {
    		DirectoryChooser chooser = new DirectoryChooser();
    		File selectedDirectory = chooser.showDialog(stage);
            
        if(selectedDirectory == null){
        } else {
        		folderPathLabel.setText(selectedDirectory.getAbsolutePath());
        }
    }
    
    public void handleActionStartModelingOther() {
    		// handle invalid dependecy and invalid folder
    		String msg = "No";
    		if(folderPathLabel.getText().equals("No usage folder selected...")) {
    			msg += " folder is chosen";
    			
    			if(currDependency.equals("") || currDependency == null) {
    				msg += " and no valid dependency is inserted!";
    			} else {
    				msg += "!";
    			}
    		} else {
    			if(currDependency.equals("") || currDependency == null) {
    				msg += " valid dependency is inserted!";
    			}
    		}
    		
    		if(!msg.equals("No")) {
    			Alert alert = new Alert(AlertType.INFORMATION);
    			alert.setTitle("Error in Starting");
    			alert.setHeaderText(null);
    			alert.setContentText(msg);
    			
    			alert.initOwner(this.stage);
    			
    			alert.showAndWait();
    		} else {
    			// 1) füge dependency ein
    			POMEditor pomEdit = POMEditor.getInstance();
    			pomEdit.addDependency(currDependency);
    			  
    			//TODO
    			// maven invoker
    			// download sources from dependency
    			// get fxml file from sources
    			// restart application with fxml file from sources
    			
    			//2) get name von dem fxml file
    			String filename = findFXML(folderPathLabel.getText())[0].getName();
    			 
    			//3) move files to maven strucutre
    			String sourcePath = folderPathLabel.getText();
    			File sourceFolder = new File(sourcePath);
    			File[] allFiles = sourceFolder.listFiles();
    			String targetPath = "";
    			File currentDirFile = new File(".");
    			String currentSourceDir = currentDirFile.getAbsolutePath().substring(0, currentDirFile.getAbsolutePath().length()-1) + "src/main/java/";
    			String currentResourceDir = currentDirFile.getAbsolutePath().substring(0, currentDirFile.getAbsolutePath().length()-1) + "src/main/resources/";
    			
    			for(File f : allFiles) {
    				if(f.getName().endsWith(".fxml")) {
    					targetPath = currentResourceDir + "/view/fxml/";
    					f.renameTo(new File(targetPath + f.getName()));
    				}
    				if(f.getName().contains("Controller")) {
    					targetPath = currentSourceDir + "controller/";
    					f.renameTo(new File(targetPath + f.getName()));
    				}
    				if(f.getName().contains("Dialog")) {
    					targetPath = currentSourceDir + "controller/dialog/";
    					f.renameTo(new File(targetPath + f.getName()));
    				}
    				if(f.getName().contains("Edge")) {
    					targetPath = currentSourceDir + "model/edges/";
    					f.renameTo(new File(targetPath + f.getName()));
    				}
    				if(f.getName().contains("Node")) {
    					targetPath = currentSourceDir + "model/nodes/";
    					f.renameTo(new File(targetPath + f.getName()));
    				}
    				if(f.getName().contains("EdgeView")) {
    					targetPath = currentSourceDir + "view/edges/";
    					f.renameTo(new File(targetPath + f.getName()));
    				}
    				if(f.getName().contains("NodeView")) {
    					targetPath = currentSourceDir + "view/nodes/";
    					f.renameTo(new File(targetPath + f.getName()));
    				}
    			}
    			
    			// 4) refresh workspace
    			
    			// 5) invoke maven clean install
    			InvocationRequest request = new DefaultInvocationRequest(); 
    			request.setPomFile(new File(currentDirFile.getAbsolutePath().substring(0, currentDirFile.getAbsolutePath().length()-1) + "pom.xml")); 
    			request.setGoals(Arrays.asList( "clean", "install")); 
    			Invoker invoker = new DefaultInvoker(); 
    			invoker.setMavenHome(new File("/usr/local/Cellar/maven/3.5.3/libexec"));
    			try {
					invoker.execute(request);
			} catch (MavenInvocationException e) {
					e.printStackTrace();
			}
    			//TODO Progress von Maven anzeigen
    			
    			 
    			//6) set fxml path
    			String fxmlPath = currentResourceDir + "/view/fxml/" + filename;
    			
    			tabController.getTabPane().getTabs().clear();
    			tabController.addTab(fxmlPath);
    			System.out.println(currDependency);
    		}
    }
    
    public File[] findFXML(String dirName){
        File dir = new File(dirName);

        return dir.listFiles(new FilenameFilter() { 
                 public boolean accept(File dir, String filename)
                      { return filename.endsWith(".fxml"); }
        } );

    }
    
    public void handleActionInsertDependency() {
    		TextInputDialog dialog = new TextInputDialog("");
    		dialog.setTitle("Dependency Input Dialog");
    		dialog.setHeaderText(null);
    		dialog.setContentText("Please enter the maven dependency:");
    		
    		dialog.initOwner(this.stage);

    		// Traditional way to get the response value.
    		Optional<String> result = dialog.showAndWait();
    		if (result.isPresent()){
    			currDependency = result.get();
    			//System.out.println("Your name: " + result.get());
    		}
    }
    
    public void handleActionNewClassDiagram(){
        tabController.getTabPane().getTabs().clear();
        tabController.addTab(TabController.CLASS_DIAGRAM_VIEW_PATH);
    }

    public void handleActionNewSequenceDiagram(){
        tabController.getTabPane().getTabs().clear();
        tabController.addTab(TabController.SEQUENCE_DIAGRAM_VIEW_PATH);
    }

    public void setTabController(TabController tc){
        tabController = tc;
    }
    
    public void setStage(Stage s) {
    		stage = s;
    }
}
