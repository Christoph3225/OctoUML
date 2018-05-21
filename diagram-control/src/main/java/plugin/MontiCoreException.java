package plugin;

import javafx.scene.layout.Pane;
import model.nodes.AbstractNode;

public interface MontiCoreException {
  AbstractNode getNode();
  Pane getContentPane();
  String getContentMessage();
}
