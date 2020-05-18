package model;

import java.io.File;
import java.io.IOException;
import javax.media.opengl.GL;
import javax.media.opengl.GLException;
import util.Collidable;
import util.CollisionDetector;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

public class Door extends Surface {
	private static Texture doorTexture;
	private static boolean firstBuild = false;
	//"src/texture/door.jpg"
	public Door(GL gl, int displayListIndex, float initWidth, float initHeight,
			float initDepth, float endWidth, float endHeight, float endDepth, 
			float repeatFirstValue, float repeatSecondValue) {
		
		super(gl, displayListIndex, initWidth, initHeight, initDepth, endWidth, 
				endHeight, endDepth, repeatFirstValue, repeatSecondValue);
		if(!firstBuild) {
			try {
				doorTexture = TextureIO.newTexture(
					new File("src/texture/door.jpg"), true);
			} catch (GLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		this.surfaceSuggestor = "Y";
		
		verticesSurface.add(new Vector3D(initWidth, initHeight, initDepth));
		verticesSurface.add(new Vector3D(endWidth, initHeight, initDepth));
		verticesSurface.add(new Vector3D(endWidth, endHeight, endDepth));
		verticesSurface.add(new Vector3D(initWidth, endHeight, endDepth));
		
		float[] vertex = {initWidth, initHeight, initDepth};
		float[] vertex2 = {endWidth, initHeight, initDepth};
		float[] vertex3 = {endWidth, endHeight, endDepth};
		float[] vertex4 = {initWidth, endHeight, endDepth};
		
		super.getVerticesRO().add(vertex);
		super.getVerticesRO().add(vertex2);
		super.getVerticesRO().add(vertex3);
		super.getVerticesRO().add(vertex4);
		
		texture = doorTexture;
	}
	
	public int getObjectDisplayList() {
		return super.getObjectDisplayList();
	}
	
	public int makeDoor(GL gl) {
		float sizeX = endWidth, sizeY = endHeight, sizeZ = endDepth;
		gl.glNewList(getObjectDisplayList(), GL.GL_COMPILE);
	
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        if(!firstBuild) {
        	firstBuild = true;
        	texture.bind();
        }
		// Front Face.
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glVertex3f(initWidth, initHeight, initDepth);
		gl.glTexCoord2f(repeatFirstVal, 0.0f);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glVertex3f(sizeX, initHeight, sizeZ);
		gl.glTexCoord2f(repeatFirstVal, repeatSecondVal);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glVertex3f(sizeX, sizeY, sizeZ);
		gl.glTexCoord2f(0.0f, repeatSecondVal);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glVertex3f(initWidth, sizeY, initDepth);
		gl.glEnd();
		gl.glEndList();
		return getObjectDisplayList();
	}
	
	public Vector3D getInitPlace() {
		return super.getInitPlace();
	}
	
	public Vector3D getEndPlace() {
		return super.getEndPlace();
	}
	
	@Override
	public boolean isColliding(Collidable other) {
		return false;
	}
	
	@Override
	public CollisionDetector getCollisionDetector() {
		return super.getCollisionDetector();
	}
	
	public boolean isPassed(Collidable otherA, Collidable otherB) {
		return false;
	}
	
	public static void setDoorTexture(Texture textureToSet) {
		doorTexture = textureToSet;
		firstBuild = false;
	}

}
