package model;

import java.util.ArrayList;
import javax.media.opengl.GL;

public class RenderableObject {
	private ArrayList<float[]> verticesRO;
	@SuppressWarnings("unused")
	private GL gl;
	private int displayList;
	
	public RenderableObject(GL gl, int displayListIndex) {
		this.gl = gl;
		this.displayList = displayListIndex;
		verticesRO = new ArrayList<float[]>();
	}
	
	public int getObjectDisplayList() {
		return displayList;
	}
	
	protected ArrayList<float[]> getVerticesRO(){
		return verticesRO;
	}
	
}
