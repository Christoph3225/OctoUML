package view.nodes;

import view.nodes.PortNodeView;
import model.nodes.ComponentNode;
import util.Constants;

import java.beans.PropertyChangeEvent;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle; 

public class ComponentNodeView extends AbstractNodeView{
// Visuell Representation of a ComponentNode
	
	// Shape C = Shape.union(A, B);
	// Wir erzeugen ein Array von Ports, sorgen daf�r, dass die alle vern�nftig ausschauen als Rechtecke
	// dann erzeugen wir das Rechteck f�r die Component
	// dann rufen wir UNION darauf auf und erhalten eine hoffentlich passende Komponente
	// Problem k�nnte sein, dass die Linien nicht mehr gezeigt werden
	
	// dann k�nnen wir auch eine Klasse PortNodeView erzeugen. Die kann dann fast das gleiche wie die ComponentView,
	// nur das in der ComponentView der Union stattfindet
}
