package plugin;

import javafx.scene.layout.Pane;
import model.nodes.AbstractNode;

//push comment

public interface MontiCoreException {
  AbstractNode getNode();
  Pane getContentPane();
  String getContentMessage();
  //void handleActionClickOnPane();
}
