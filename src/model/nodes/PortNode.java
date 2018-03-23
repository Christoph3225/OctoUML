package model.nodes;

public class PortNode extends AbstractNode{
	
	private static final String TYPE = "PORT";
	
	public PortNode(double x, double y, double width, double height){
		super(x, y, width, height );
        //Don't accept nodes with size less than minWidth * minHeight.
        this.width = width > PORT_MAX_WIDTH ? PORT_MAX_WIDTH : width;
        this.height = height > PORT_MAX_HEIGHT ? PORT_MAX_HEIGHT : height;
	}
	
	
	@Override
    public PortNode copy(){
		PortNode newCopy = new PortNode(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        newCopy.setTranslateX(this.getTranslateX());
        newCopy.setTranslateY(this.getTranslateY());
        newCopy.setScaleX(this.getScaleX());
        newCopy.setScaleY(this.getScaleY());

        if(this.getTitle() != null){
            newCopy.setTitle(this.getTitle());

        }
        newCopy.setTranslateX(this.getTranslateX());
        newCopy.setTranslateY(this.getTranslateY());
        return newCopy;


	}

    @Override
    public void setHeight(double height) {
    	this.height = height < PORT_MAX_HEIGHT ? PORT_MAX_HEIGHT : height;
        super.setHeight(height);
    }

    @Override
    public void setWidth(double width) {
    	this.width = width < PORT_MAX_WIDTH ? PORT_MAX_WIDTH : width;
        super.setWidth(width);
    }

    @Override
    public void remoteSetHeight(double height) {
    	this.height = height < PORT_MAX_HEIGHT ? PORT_MAX_HEIGHT : height;
        super.remoteSetHeight(height);    	
    }

    @Override
    public void remoteSetWidth(double width) {
    	this.width = width < PORT_MAX_WIDTH ? PORT_MAX_WIDTH : width;
        super.remoteSetWidth(width);
    }
    
    public String getType(){
        return TYPE;
    }

}


