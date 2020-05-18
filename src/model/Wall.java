package model;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.media.opengl.GL;
import javax.media.opengl.GLException;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import util.Collidable;
import util.CollisionDetector;

public class Wall extends Surface {
	private static Texture wallRedTexture;
	private static int wallsTexturesNumber = 5;
	private static boolean firstBuild = false;
	//"src/texture/wall/wallRed.jpg"
	//"src/texture/wall/wallGray.jpg"
	//"src/texture/wall/wallRedBrick.jpg"
	public Wall(GL gl, int displayListIndex, float initWidth, float initHeight,
			float initDepth, float endWidth, float endHeight, float endDepth, 
			float repeatFirstValue, float repeatSecondValue) {
		
		super(gl, displayListIndex, initWidth, initHeight, initDepth, endWidth, 
				endHeight, endDepth, repeatFirstValue, repeatSecondValue);
		if(!firstBuild) {
			try {
				Random rand = new Random();
				int i = rand.nextInt(wallsTexturesNumber);
				if(i == 0) {
					wallRedTexture = TextureIO.newTexture(
							new File("src/texture/wall/wallRed.jpg"), true);
				} else if(i == 1) {
					wallRedTexture = TextureIO.newTexture(
							new File("src/texture/wall/wallRedBrick.jpg"), true);
				} else if(i == 2) {
					wallRedTexture = TextureIO.newTexture(
							new File("src/texture/wall/wallGray.jpg"), true);
				} else if(i == 3) {
					wallRedTexture = TextureIO.newTexture(
							new File("src/texture/wall/wallWalkway.png"), true);
				} else if(i == 4) {
					wallRedTexture = TextureIO.newTexture(
							new File("src/texture/wall/wallSmall.png"), true);
				}
			} catch (GLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		this.surfaceSuggestor = "Z";
		
		verticesSurface.add(new Vector3D(initWidth, initHeight, initDepth));
		verticesSurface.add(new Vector3D(endWidth, initHeight, endDepth));
		verticesSurface.add(new Vector3D(endWidth, endHeight, endDepth));
		verticesSurface.add(new Vector3D(initWidth, endHeight, initDepth));
		
		float[] vertex = {initWidth, initHeight, initDepth};
		float[] vertex2 = {endWidth, initHeight, endDepth};
		float[] vertex3 = {endWidth, endHeight, endDepth};
		float[] vertex4 = {initWidth, endHeight, initDepth};
		
		super.getVerticesRO().add(vertex);
		super.getVerticesRO().add(vertex2);
		super.getVerticesRO().add(vertex3);
		super.getVerticesRO().add(vertex4);
		texture = wallRedTexture;
	}
	
	public int getObjectDisplayList() {
		return super.getObjectDisplayList();
	}
	
	public int makeWall(GL gl) {
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
		return super.isColliding(other);
	}
	
	@Override
	public CollisionDetector getCollisionDetector() {
		return super.getCollisionDetector();
	}
	
	public boolean isPassed(Collidable otherA, Collidable otherB) {
		return super.isPassed(otherA, otherB);
	}
	
	public static void setWallRedTexture(Texture textureToSet) {
		wallRedTexture = textureToSet;
		firstBuild = false;
	}
}
