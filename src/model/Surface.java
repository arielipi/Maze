package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.media.opengl.GL;

import util.AxisAlignedBoundingBox;
import util.Collidable;
import util.CollisionDetector;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

public class Surface extends RenderableObject implements Collidable {
		
	protected float initWidth;
	protected float initHeight;
	protected float initDepth;
	protected float endWidth;
	protected float endHeight;
	protected float endDepth;
	protected float repeatFirstVal;
	protected float repeatSecondVal;
	protected ArrayList<Vector3D> verticesSurface;
	protected Texture texture;
	protected String texturePath;
	// denotes on which axis the surface is on, X means X axis is flat and so on
	// X means Xmin and Xmax are the same
	// Y means Ymin and Ymax are the same
	// Z means Zmin and Zmax are the same
	protected String surfaceSuggestor;

	public Surface(GL gl, int displayListIndex, float initWidth, float initHeight,
			float initDepth, float endWidth, float endHeight, float endDepth, 
			float repeatFirstValue, float repeatSecondValue) {
		super(gl, displayListIndex);
		this.initWidth = initWidth;
		this.initHeight = initHeight;
		this.initDepth = initDepth;
		this.endWidth = endWidth;
		this.endHeight = endHeight;
		this.endDepth = endDepth;
		this.repeatFirstVal = repeatFirstValue;
		this.repeatSecondVal = repeatSecondValue;
		verticesSurface = new ArrayList<Vector3D>();
	}
	
	public int getObjectDisplayList() {
		return super.getObjectDisplayList();
	}
	
	public void setTexture(Texture textureToSet) {
		this.texture = textureToSet;
	}
	
	public int makeSurface(GL gl) {
		float sizeX = endWidth, sizeY = endHeight, sizeZ = endDepth;
		gl.glNewList(getObjectDisplayList(), GL.GL_COMPILE);
		
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        try {
    		String filename = "src/texture/redWall.jpg"; // the FileName to open
    		texture = TextureIO.newTexture(new File(filename), true);
    		texture.bind();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }        
		// Front Face.
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glVertex3f(initWidth, initHeight, sizeZ);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glVertex3f(sizeX, initHeight, sizeZ);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glVertex3f(sizeX, sizeY, sizeZ);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glVertex3f(initWidth, sizeY, sizeZ);
		gl.glEnd();
		gl.glEndList();

		return getObjectDisplayList();
	}
	
	public Vector3D getInitPlace() {
		return (new Vector3D(initWidth, initHeight, initDepth));
	}
	
	public Vector3D getEndPlace() {
		return (new Vector3D(endWidth, endHeight, endDepth));
	}
	
	@Override
	public boolean isColliding(Collidable other) {
		AxisAlignedBoundingBox aabb = (AxisAlignedBoundingBox) getCollisionDetector();
		boolean returnValue = aabb.isCollidingCollisionDetections(other.getCollisionDetector());
		return returnValue;
	}
	
	@Override
	public CollisionDetector getCollisionDetector() {
		AxisAlignedBoundingBox aabb = new AxisAlignedBoundingBox(super.getVerticesRO());
		return aabb;
	}
	
	public boolean isPassed(Collidable otherA, Collidable otherB) {
		AxisAlignedBoundingBox aabb = (AxisAlignedBoundingBox) getCollisionDetector();
		boolean returnValue = aabb.isPassedPlane(aabb, otherA.getCollisionDetector(), otherB.getCollisionDetector());
		return returnValue;
	}
}