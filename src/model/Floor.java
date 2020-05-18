package model;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.media.opengl.GL;
import javax.media.opengl.GLException;

import util.Collidable;
import util.CollisionDetector;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

import controller.InputHandler;

public class Floor extends Surface {
	private static Texture floorBrickNormalTexture;
	private static int floorsTexturesNumber = 6;
	private static int floorNumber = 0;
	private static boolean firstBuild = false;
	//"src/texture/floor/floorBrickNormal.jpg"
	//"src/texture/floorBrickBlackAndWhite.jpg"
	//"src/texture/floorBrickGray.jpg"
	//"src/texture/floorWood.jpg"
	public Floor(GL gl, int displayListIndex, float initWidth, float initHeight,
			float initDepth, float endWidth, float endHeight, float endDepth, 
			float repeatFirstValue, float repeatSecondValue) {
		
		super(gl, displayListIndex, initWidth, initHeight, initDepth, endWidth, 
				endHeight, endDepth, repeatFirstValue, repeatSecondValue);
		if(!firstBuild) {
			try {
				Random rand = new Random();
				int i = rand.nextInt(floorsTexturesNumber);
				if(i == 0) {
					floorBrickNormalTexture = TextureIO.newTexture(
							new File("src/texture/floor/floorBrickNormal.jpg"), true);
					floorNumber = 0;
				} else if(i == 1) {
					floorBrickNormalTexture = TextureIO.newTexture(
							new File("src/texture/floor/floorBrickBlackAndWhite.png"), true);
					floorNumber = 0;
				} else if(i == 2) {
					floorBrickNormalTexture = TextureIO.newTexture(
							new File("src/texture/floor/floorBrickGray.png"), true);
					floorNumber = 0;
				} else if(i == 3) {
					floorBrickNormalTexture = TextureIO.newTexture(
							new File("src/texture/floor/floorWood.jpg"), true);
					floorNumber = 1;
				}  else if(i == 4) {
					floorBrickNormalTexture = TextureIO.newTexture(
							new File("src/texture/floor/floorGrayGravel.jpg"), true);
					floorNumber = 2;
				}  else if(i == 5) {
					floorBrickNormalTexture = TextureIO.newTexture(
							new File("src/texture/floor/floorOrangeGravel.jpg"), true);
					floorNumber = 2;
				}
			} catch (GLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		InputHandler.floorNumberChanged(floorNumber);
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

		texture = floorBrickNormalTexture;
	}
	
	public int getObjectDisplayList() {
		return super.getObjectDisplayList();
	}
	
	public int makeFloor(GL gl) {
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
		gl.glVertex3f(initWidth, sizeY, initDepth);
		gl.glTexCoord2f(repeatFirstVal, 0.0f);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glVertex3f(sizeX, sizeY, initDepth);
		gl.glTexCoord2f(repeatFirstVal, repeatSecondVal);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glVertex3f(sizeX, sizeY, sizeZ);
		gl.glTexCoord2f(0.0f, repeatSecondVal);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glVertex3f(initWidth, sizeY, sizeZ);
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
		return super.isColliding(other);
	}
	
	@Override
	public CollisionDetector getCollisionDetector() {
		return super.getCollisionDetector();
	}
	
	public boolean isPassed(Collidable otherA, Collidable otherB) {
		return super.isPassed(otherA, otherB);
	}
	
	public static void setFloorBrickNormalTexture(Texture textureToSet) {
		floorBrickNormalTexture = textureToSet;
		firstBuild = false;
	}
	
	public static int getFloorNumber() {
		return floorNumber;
	}
}
