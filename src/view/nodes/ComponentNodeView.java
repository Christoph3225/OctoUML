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
	// Wir erzeugen ein Array von Ports, sorgen dafür, dass die alle vernünftig ausschauen als Rechtecke
	// dann erzeugen wir das Rechteck für die Component
	// dann rufen wir UNION darauf auf und erhalten eine hoffentlich passende Komponente
	// Problem könnte sein, dass die Linien nicht mehr gezeigt werden
	
	// dann können wir auch eine Klasse PortNodeView erzeugen. Die kann dann fast das gleiche wie die ComponentView,
	// nur das in der ComponentView der Union stattfindet
}
