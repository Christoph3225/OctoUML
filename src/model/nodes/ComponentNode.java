package model.nodes;

import util.Constants;

import java.io.Serializable;


public class ComponentNode extends AbstractNode implements Serializable {
//Representation einer einzelnen Komponente -> interne Darstellung
	private static final String TYPE = "COMPONENT";
	private String Ports;
	// überlegen, ob wir für Ports nicht auch einen Node einfach erstellen und hier ein Array von Ports
	private String Subcomponents; // TODO: Wieso String und nicht besser ein Array?

	public ComponentNode(double x, double y, double width, double height){
		// irgendwas für Ports. Vielleicht auch einfach ein Array von Ports
	}
	
	
	@Override
    public ComponentNode copy(){
		return null;
	}

    @Override
    public void setHeight(double height) {
    	this.height = height < COMPONENT_MIN_HEIGHT ? COMPONENT_MIN_HEIGHT : height;
        super.setHeight(height);
    }

    @Override
    public void setWidth(double width) {
    	this.width = width < COMPONENT_MIN_WIDTH ? COMPONENT_MIN_WIDTH : width;
        super.setWidth(width);
    }

    @Override
    public void remoteSetHeight(double height) {
    	this.height = height < COMPONENT_MIN_HEIGHT ? COMPONENT_MIN_HEIGHT : height;
        super.remoteSetHeight(height);    	
    }

    @Override
    public void remoteSetWidth(double width) {
    	this.width = width < COMPONENT_MIN_WIDTH ? COMPONENT_MIN_WIDTH : width;
        super.remoteSetWidth(width);
    }
    
    public String getType(){
        return TYPE;
    }

}
