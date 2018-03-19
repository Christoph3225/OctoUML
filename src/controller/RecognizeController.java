package controller;

import edu.tamu.core.sketch.BoundingBox;
import edu.tamu.core.sketch.Point;
import edu.tamu.core.sketch.Shape;
import edu.tamu.core.sketch.Stroke;
import edu.tamu.recognition.paleo.PaleoConfig;
import edu.tamu.recognition.paleo.PaleoSketchRecognizer;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import model.*;
import model.edges.AbstractEdge;
import model.edges.AssociationEdge;
import model.nodes.AbstractNode;
import model.nodes.ClassNode;
import model.nodes.ComponentNode;
import model.nodes.PortNode;
import model.nodes.Node;
import util.commands.AddDeleteEdgeCommand;
import util.commands.AddDeleteNodeCommand;
import util.commands.CompoundCommand;
import view.edges.AbstractEdgeView;
import view.nodes.AbstractNodeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Used by MainController for handling the recognition of drawn shapes and transforming them in to UML-notations.
 */
public class RecognizeController {
    private Pane aDrawPane;
    private AbstractDiagramController diagramController;
    private PaleoSketchRecognizer recognizer;
    private Graph graph;
    HashMap<BoundingBox, Sketch> sketchMap = new HashMap<>();


    public RecognizeController(Pane pDrawPane, AbstractDiagramController pController) {
        aDrawPane = pDrawPane;
        diagramController = pController;
        graph = diagramController.getGraphModel();

        //TODO Find a nicer solution for this:
        //This is to load the recognizer when starting app, not when starting to draw.

        recognizer = new PaleoSketchRecognizer(PaleoConfig.allOn());
        Stroke init = new Stroke();
        init.addPoint(new Point(0,1));
        recognizer.setStroke(init);
        recognizer.recognize().getBestShape();
    }

    public synchronized void recognize(List<Sketch> sketches) {
        ArrayList<AbstractNode> recognizedNodes = new ArrayList();
        ArrayList<Sketch> sketchesToBeRemoved = new ArrayList<>();
        ArrayList<AbstractEdge> recognizedEdges = new ArrayList<>();
        ArrayList<BoundingBox> boxes = new ArrayList();
        CompoundCommand recognizeCompoundCommand = new CompoundCommand();

        
        //Go through all sketches to find Nodes.
        for (Sketch s : sketches) {
            if (s.getStroke() != null && s.getStroke().getPoints() != null && !s.getStroke().getPoints().isEmpty()) {
                //TODO This sometimes throws IndexOutOfBoundsException...
                recognizer.setStroke(s.getStroke());
                Shape bestMatch = recognizer.recognize().getBestShape();
                String bestMatchString = bestMatch.getInterpretation().label;
                if (bestMatchString.equals("Square") || bestMatchString.equals("Rectangle")) {
                    BoundingBox box = s.getStroke().getBoundingBox();
                    ClassNode node = new ClassNode(box.getX(), box.getY(),box.getWidth(), box.getHeight());
                    s.setRecognizedElement(node);
                    recognizedNodes.add(node);
                    sketchesToBeRemoved.add(s);
                    graph.addNode(node, false); //Really don't want to do this until mainController.createNodeView but need to because of graph.findNode.
                }

            }
        }
        
        
        //Go through all sketches to find edges. Edges need to be recognized after nodes
        for (Sketch s : sketches) {
            if (s.getStroke() != null) {
                recognizer.setStroke(s.getStroke());
                String bestMatchString = recognizer.recognize().getBestShape().getInterpretation().label;

                if (bestMatchString.equals("Line") || bestMatchString.startsWith("Polyline") || bestMatchString.equals("Arc") ||
                        bestMatchString.equals("Curve") || bestMatchString.equals("Arrow")){

                    Point2D startPoint = new Point2D(s.getStroke().getFirstPoint().getX(), s.getStroke().getFirstPoint().getY());
                    Point2D endPoint = new Point2D(s.getStroke().getLastPoint().getX(), s.getStroke().getLastPoint().getY());
                    Node startNode = graph.findNode(startPoint);
                    Node endNode = graph.findNode(endPoint);

                    //For arrows, which don't have an endpoint
                    List<Point> points = s.getStroke().getPoints();
                    for (int i = points.size()-1; i > points.size()/2; i--) {
                        Point2D point = new Point2D(points.get(i).getX(), points.get(i).getY());
                        if (graph.findNode(point) != null) {
                            endNode = graph.findNode(point);
                            break;
                        }
                    }

                    if (startNode != null && endNode != null && !startNode.equals(endNode)) {
                        AssociationEdge newEdge = new AssociationEdge(startNode, endNode);
                        if (bestMatchString.equals("Arrow")) {
                            newEdge.setDirection(AbstractEdge.Direction.START_TO_END);
                        }
                        s.setRecognizedElement(newEdge);
                        sketchesToBeRemoved.add(s);
                        recognizedEdges.add(newEdge);
                    }
                }
            }
        }

        // Ab hier m�sste es wieder f�r alle gleich sein. 
        
        for(AbstractNode node : recognizedNodes){
            AbstractNodeView nodeView = diagramController.createNodeView(node, false);
            recognizeCompoundCommand.add(new AddDeleteNodeCommand(diagramController, graph, nodeView, node, true));
        }
        for(AbstractEdge edge : recognizedEdges){
            AbstractEdgeView edgeView = diagramController.addEdgeView(edge, false);
            if (edgeView != null) {
                recognizeCompoundCommand.add(new AddDeleteEdgeCommand(diagramController, edgeView, edge, true));
            }
        }
        for(Sketch sketch : sketchesToBeRemoved){
            diagramController.deleteSketch(sketch, recognizeCompoundCommand, false);
        }
        if(recognizeCompoundCommand.size() > 0){
            diagramController.getUndoManager().add(recognizeCompoundCommand);
        }

    }


    public synchronized void recognizeMonti(List<Sketch> sketches) {	
    	ArrayList<AbstractNode> recognizedNodes = new ArrayList();
        ArrayList<Sketch> sketchesToBeRemoved = new ArrayList<>();
        ArrayList<AbstractEdge> recognizedEdges = new ArrayList<>();
        ArrayList<BoundingBox> boxes = new ArrayList();
        CompoundCommand recognizeCompoundCommand = new CompoundCommand();
        

        for (Sketch s : sketches) {
            if (s.getStroke() != null && s.getStroke().getPoints() != null && !s.getStroke().getPoints().isEmpty()) {
//                //TODO This sometimes throws IndexOutOfBoundsException...
                recognizer.setStroke(s.getStroke());
                Shape bestMatch = recognizer.recognize().getBestShape();
                String bestMatchString = bestMatch.getInterpretation().label;
                if (bestMatchString.equals("Square") || bestMatchString.equals("Rectangle")) {
                    BoundingBox box = s.getStroke().getBoundingBox();
                    boxes.add(box);
                    sketchMap.put(box,s);
        			// vielleicht hier eine Map von sketch s zu box b noch hinzuf�gen oder andersherum
                }
            }
        }
        for (BoundingBox b : boxes) {
        	for (BoundingBox bb: boxes) {
        		if(b.intersects(bb.getX(), bb.getY(), bb.getWidth(), bb.getHeight())) {
//        			// dann schneiden sich die beiden Boxen
//        			// d.h. eine Box ist vermutlich eine Component und eine ist ein Port
        			if(b.getWidth()*b.getHeight() > bb.getWidth()*bb.getHeight()) {
        				// dann wissen wir, dass das Volumen von b gr��er als von bb ist
        				// und leiten ab, dass b Component und bb Port
        				
        				// Jetzt muss erst noch gecheckt werden, ob es bereits einen Node f�r die jeweilige Box gibt
        				// Wenn das der Fall ist, m�ssen wir den ComponentNode nicht mehr erzeugen und nur noch den PortNode zum ComponentNode hinzuf�gen
        				// Ansonsten m�ssen beide erzeugt werden
        				// Die Nodes sind im Graphen drin
        				Point2D point = new Point2D(b.getX(), b.getY());
        				if(graph.findNode(point) != null) {
        					// Fall 1: ComponentNode existiert bereits
        					PortNode port = new PortNode(bb.getX(), bb.getY(), bb.getWidth(), bb.getHeight());
        					((ComponentNode) graph.findNode(point)).addPort(port);
        				}else {
        				// Fall 2: Beide m�ssen erzeugt werden
        					ArrayList<PortNode> ports = new ArrayList<>();
        					PortNode port = new PortNode(bb.getX(), bb.getY(), bb.getWidth(), bb.getHeight());
        					ports.add(port);
        					ComponentNode node = new ComponentNode(b.getX(), b.getY(), b.getWidth(), b.getHeight(), ports);
        					graph.addNode(node, false);
        					// zugeh�rigen Sketch mittels einer HashMap
        					Sketch s = sketchMap.get(bb);
            				s.setRecognizedElement(node);
            	            recognizedNodes.add(node);
            	            sketchesToBeRemoved.add(s);
            			}
        			}else {
        				// also ist bb das gr��ere Rechteck
        				Point2D point = new Point2D(bb.getX(), bb.getY());
        				if(graph.findNode(point) != null) {
        					// Fall 1: ComponentNode existiert bereits
        					PortNode port = new PortNode(b.getX(), b.getY(), b.getWidth(), b.getHeight());
           					((ComponentNode) graph.findNode(point)).addPort(port);
        				}else {
        				// Fall 2: Beide m�ssen erzeugt werden
        					ArrayList<PortNode> ports = new ArrayList<>();
        					PortNode port = new PortNode(b.getX(), b.getY(), b.getWidth(), b.getHeight());
        					ports.add(port);
        					ComponentNode node = new ComponentNode(bb.getX(), bb.getY(), bb.getWidth(), bb.getHeight(), ports);
        					graph.addNode(node, false);
        					// zugeh�rigen Sketch mittels einer HashMap
            				Sketch s = sketchMap.get(b);
            				s.setRecognizedElement(node);
            	            recognizedNodes.add(node);
            	            sketchesToBeRemoved.add(s);
        				}
        			}	
        		} else {
        			// hier kommen verschiedene Optionen jetzt in Frage. Dazu geh�rt dann auch die hierarchische Komponente
        			// dann kann man vielleicht mit contains() �berpr�fen
        			// drei weitere Optionen: Beide sind Ports der gleichen Component, beide sind Ports verschiedener Components, beide sind Components
        			
        		}
        	}
        	
        
//        	// hier muss jetzt entschieden werden, ob ComponentNode oder PortNode
//        	// brauchen sowas wie: f�r componentNode c: c.addPort(new PortNode(...)) um Port zu einer Component hinzuzuf�gen
//        	// Frage, ob die Ports auch in recogniedNodes m�ssen
//        	
        }
    }
}
    
