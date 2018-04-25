package plugin;

import java.util.List;

import controller.AbstractDiagramController;
import de.monticore.ast.ASTNode;
import de.monticore.prettyprint.IndentPrinter;
import model.Graph;

public interface MontiCorePlugIn {
  // returns the diagram controller
  AbstractDiagramController getController();
  
  // returns the name of the fxml file
  String getView();
  
  // returns the name of the DSL
  String getDSLName();
  
  // returns the file ending to the DSL for saving models
  String getFileEnding();
  
  // returns the path to the picture
  String getDSLPicture();
  
  // returns the prettyprinter of the DSL
  IndentPrinter getPrettyPrinter();
  
  // returns the type of AST node
  Class getASTNode();
  
  // returns the transformed (model to model) AST from the OctoUML Graph
  ASTNode shapeToAST(Graph graph, String path);
  
  // returns a list of errors
  List<String> check(ASTNode node);
  
  // returns the generator of the DSL
  String getGenerator();
  
  // returns true if generation of code was successful
  boolean generateCode(ASTNode node, String path);
}
