package controller;

import controller.dialog.GithubLoginDialogController;
import controller.dialog.GithubRepoDialogController;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import plugin.CD4APlugin;

import org.controlsfx.control.Notifications;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The class controlling the top menu and the tabs.
 */
public class TabController {
  
  @FXML
  private CheckMenuItem umlMenuItem, sketchesMenuItem, mouseMenuItem, gridMenuItem, snapToGridMenuItem, snapIndicatorsMenuItem;
  
  @FXML
  private TabPane tabPane;
  private Stage stage;
  private Map<Tab, AbstractDiagramController> tabMap = new HashMap<>();
  
  public static final String CLASS_DIAGRAM_VIEW_PATH = "view/fxml/classDiagramView.fxml";
  public static final String SEQUENCE_DIAGRAM_VIEW_PATH = "view/fxml/sequenceDiagramView.fxml";
  
  @FXML
  public void initialize() {
  }
  
  public TabPane getTabPane() {
    return tabPane;
  }
  
  public void setStage(Stage pStage) {
    stage = pStage;
  }
  
  public Tab addTab(String pathToDiagram) {
    BorderPane canvasView = null;
    AbstractDiagramController diagramController = null;
    FXMLLoader loader;
    try {
      loader = new FXMLLoader(getClass().getClassLoader().getResource(pathToDiagram));
      canvasView = loader.load();
      diagramController = loader.getController();
    }
    catch (IOException e) {
      System.out.println(e.getMessage());
    }
    
    Tab tab = new Tab();
    
    tab.setContent(canvasView);
    tabMap.put(tab, diagramController);
    
    tab.setText(diagramController.getTabControllerName() + " " + tabMap.size());
    
    tabPane.getTabs().add(tab);
    diagramController.setStage(stage);
    return tab;
  }
  
  public Tab addPluginTab(Class c, String path, AbstractDiagramController adc) {
    BorderPane canvasView = null;
    // AbstractDiagramController diagramController = null;
    FXMLLoader loader;
    try {
      loader = new FXMLLoader();
      loader.setLocation(c.getResource(path));
      //System.out.println(loader.getController().toString());
      canvasView = loader.load();
      loader.setController(adc);
      
      /**
       * welchen class loader nutzt fxml
       */
      
      
      // diagramController = adc;
    }
    catch (IOException e) {
      e.printStackTrace();
      //System.out.println(e.getMessage());
    }
    
    Tab tab = new Tab();
    
    tab.setContent(canvasView);
    tabMap.put(tab, adc);
    
    if (adc instanceof ClassDiagramController) {
      tab.setText("Class Diagram " + tabMap.size());
    }
    else {
      tab.setText("Sequence Diagram " + tabMap.size());
    }
    tabPane.getTabs().add(tab);
    adc.setStage(stage);
    return tab;
  }
  
  public void handleMenuActionUML() {
    tabMap.get(tabPane.getSelectionModel().getSelectedItem()).handleMenuActionUML();
  }
  
  public void handleMenuActionSketches() {
    tabMap.get(tabPane.getSelectionModel().getSelectedItem()).handleMenuActionSketches();
  }
  
  public void handleMenuActionGrid() {
    tabMap.get(tabPane.getSelectionModel().getSelectedItem()).handleMenuActionGrid();
  }
  
  public void handleMenuActionMouse() {
    tabMap.get(tabPane.getSelectionModel().getSelectedItem()).handleMenuActionMouse();
  }
  
  public void handleMenuActionExit() {
    Platform.exit();
  }
  
  public void handleMenuActionSave() {
    tabMap.get(tabPane.getSelectionModel().getSelectedItem()).handleMenuActionSave();
  }
  
  public void handleMenuActionLoad() {
    Tab tab = addTab(CLASS_DIAGRAM_VIEW_PATH);
    tabPane.getSelectionModel().select(tab);
    tabMap.get(tab).handleMenuActionLoad();
    tab.setText(tabMap.get(tab).getGraphModel().getName());
  }
  
  public void handleMenuActionNewClassDiagram() {
    Tab tab = addTab(CLASS_DIAGRAM_VIEW_PATH);
    tabPane.getSelectionModel().select(tab);
  }
  
  public void handleMenuActionNewSequenceDiagram() {
    Tab tab = addTab(SEQUENCE_DIAGRAM_VIEW_PATH);
    tabPane.getSelectionModel().select(tab);
  }
  
  public void handleMenuActionNewOtherDiagram() {
    //TODO
    Dialog<List<String>> dialog = new Dialog<List<String>>();
    dialog.setTitle("Enter Other Diagram Properties");

    DialogPane pane = new DialogPane();
    GridPane gridPane = new GridPane();
    Label dependencyLbl = new Label("Enter maven dependency:");
    Label folderLbl = new Label("Enter folder for generation:");
    Label diagramPathLbl = new Label("Enter path to diagram:");
    TextField dependencyTF = new TextField();
    TextField folderTF = new TextField();
    TextField diagramPathTF = new TextField();
    gridPane.getColumnConstraints().add(new ColumnConstraints(170));
    gridPane.getColumnConstraints().add(new ColumnConstraints(200));
    gridPane.setHgap(30);
    gridPane.setVgap(10);
    gridPane.add(dependencyLbl, 0, 0);
    gridPane.add(dependencyTF, 1, 0);
    gridPane.add(folderLbl, 0, 1);
    gridPane.add(folderTF, 1, 1);
    gridPane.add(diagramPathLbl, 0, 2);
    gridPane.add(diagramPathTF, 1, 2);

    pane.getChildren().add(gridPane);
    pane.setPrefSize(400, 160);
    pane.getButtonTypes().add(ButtonType.OK);
    dialog.setDialogPane(pane);
    dialog.initOwner(stage);
    Optional<List<String>> result = dialog.showAndWait();
    String OTHER_DIAGRAM_VIEW_PATH = "";
    String folderPath = "";
    if (result.isPresent()) {
      OTHER_DIAGRAM_VIEW_PATH = diagramPathTF.getText();
      folderPath = folderTF.getText();
    }
    CD4APlugin p = CD4APlugin.getInstance();
    p.setUsageFolderPath(folderPath);
    Tab tab = addTab(OTHER_DIAGRAM_VIEW_PATH);
    tabPane.getSelectionModel().select(tab);
    //TODO handle maven dependency wenn dynamically loading is working
  }
  
  public void handleMenuActionServer() {
    Tab tab = addTab(CLASS_DIAGRAM_VIEW_PATH);
    tabPane.getSelectionModel().select(tab);
    tabMap.get(tabPane.getSelectionModel().getSelectedItem()).handleMenuActionServer();
  }
  
  public void handleMenuActionClient() {
    Tab tab = addTab(CLASS_DIAGRAM_VIEW_PATH);
    tabPane.getSelectionModel().select(tab);
    if (!tabMap.get(tabPane.getSelectionModel().getSelectedItem()).handleMenuActionClient()) {
      tabPane.getTabs().remove(tab);
    }
  }
  
  public void handleMenuActionImage() {
    tabMap.get(tabPane.getSelectionModel().getSelectedItem()).handleMenuActionImage();
  }
  
  public void handleMenuActionSnapToGrid() {
    tabMap.get(tabPane.getSelectionModel().getSelectedItem()).handleMenuActionSnapToGrid(snapToGridMenuItem.isSelected());
  }
  
  public void handleMenuActionSnapIndicators() {
    tabMap.get(tabPane.getSelectionModel().getSelectedItem()).handleMenuActionSnapIndicators(snapIndicatorsMenuItem.isSelected());
  }
  
  public void stop() {
    for (AbstractDiagramController mc : tabMap.values()) {
      mc.closeServers();
      mc.closeClients();
      mc.closeLog();
    }
  }
  
  public void handleMenuActionGit() {
    try {
      File localPath = File.createTempFile("GitRepository", "");
      localPath.delete();
      
      GithubRepoDialogController gitRepoController = showGithubRepoDialog();
      
      if (gitRepoController != null && gitRepoController.isOkClicked()) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Please wait");
        alert.setHeaderText("Please wait while the repository is being downloaded");
        alert.show();
        Git git = Git.cloneRepository().setURI(gitRepoController.urlTextField.getText()).setDirectory(localPath).call();
        alert.close();
        
        AbstractDiagramController diagramController = tabMap.get(tabPane.getSelectionModel().getSelectedItem());
        
        if (gitRepoController.imageCheckBox.isSelected()) {
          WritableImage image = diagramController.getSnapShot();
          String imageFileName = gitRepoController.imageNameTextField.getText() + ".png";
          File imageFile = new File(localPath + "/" + imageFileName);
          ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", imageFile);
          
          git.add().addFilepattern(imageFileName).call();
        }
        
        if (gitRepoController.xmiCheckBox.isSelected()) {
          String xmiFileName = gitRepoController.xmiNameTextField.getText() + ".xmi";
          diagramController.createXMI(localPath + "/" + xmiFileName);
          git.add().addFilepattern(xmiFileName).call();
          
        }
        
        git.commit().setMessage(gitRepoController.commitTextField.getText()).call();
        PushCommand pushCommand = git.push();
        GithubLoginDialogController gitLoginController = showGithubLoginDialog();
        if (gitLoginController != null && gitLoginController.isOkClicked() && (gitRepoController.imageCheckBox.isSelected() || gitRepoController.xmiCheckBox.isSelected())) {
          pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitLoginController.nameTextField.getText(), gitLoginController.passwordField.getText()));
          pushCommand.call();
          Notifications.create().title("Upload succesfull!").text("Your diagram has been uploaded successfully.").showInformation();
        }
        else {
          Notifications.create().title("Nothing was uploaded!").text("Either you cancelled or didn't select anything to upload.").showError();
        }
      }
      
    }
    catch (InvalidRemoteException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Remote not found");
      alert.setContentText("The URL was incorrect.");
      alert.showAndWait();
    }
    catch (NullPointerException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("No diagram open");
      alert.setContentText("There is no diagram to upload.");
      alert.showAndWait();
    }
    catch (TransportException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Not authorized");
      alert.setContentText("You are not authorized to modify repository");
      alert.showAndWait();
    }
    
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public GithubRepoDialogController showGithubRepoDialog() {
    GithubRepoDialogController controller = null;
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/fxml/githubRepoDialog.fxml"));
      AnchorPane page = loader.load();
      Stage dialogStage = new Stage();
      dialogStage.initModality(Modality.WINDOW_MODAL);
      dialogStage.initOwner(this.stage);
      dialogStage.setScene(new Scene(page));
      
      controller = loader.getController();
      controller.setDialogStage(dialogStage);
      dialogStage.showAndWait();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return controller;
    
  }
  
  public GithubLoginDialogController showGithubLoginDialog() {
    GithubLoginDialogController controller = null;
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/fxml/githubLoginDialog.fxml"));
      AnchorPane page = loader.load();
      Stage dialogStage = new Stage();
      dialogStage.initModality(Modality.WINDOW_MODAL);
      dialogStage.initOwner(this.stage);
      dialogStage.setScene(new Scene(page));
      
      controller = loader.getController();
      controller.setDialogStage(dialogStage);
      dialogStage.showAndWait();
      
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    
    return controller;
    
  }
}
