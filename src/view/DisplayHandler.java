package view;


import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import sound.SoundHandler;
import controller.Controller.GameData;
import controller.Settings;
import maze.PairPoint;
import model.Floor;
import model.Surface;
import model.Vector3D;
import model.World;

public class DisplayHandler implements GLEventListener {
	private Vector3D upVector;// = new Vector3D(0, 1, 0);
	private Vector3D camera;// = new Vector3D(10, 10, 10);
	private double lookXAngle = 0;
	private double lookYAngle = 0;
	private double view_fov = Settings.VIEW_FOV; //Field of view for the projection window. 
	private int gameLevel;
	private World world;
	private GLU glu;
	private ArrayList<Integer> objects;
	private ArrayList<Surface> wallObjectsList;
	//LIGHTING FIELDS
	private float[] ambient = {1.0f, 1.0f, 1.0f, 1.0f};
	private float[] color = {0.2f, 0.2f, 0.2f, 0.2f};
	private float[] position = {0.0f, 0.0f, 1.0f};
	private FloatBuffer ambientFloatBuffer = FloatBuffer.wrap(ambient);
	private FloatBuffer colorFloatBuffer = FloatBuffer.wrap(color);
	private FloatBuffer positionFloatBuffer = FloatBuffer.wrap(position);
	private JFrame jFrame;
	public boolean levelFinished;
	private Thread gameStateChecker = new GameStateChecker();

	public DisplayHandler() {
		upVector = new Vector3D(0, 1, 0);
		camera = new Vector3D(10, 10, 10);
		objects = new ArrayList<Integer>();
		wallObjectsList = new ArrayList<Surface>();
		gameLevel = GameData.level;
		world = new World(gameLevel);
		levelFinished = false;
		jFrame = new JFrame();
		jFrame.setUndecorated(true);
	}

	@Override
	public void display(GLAutoDrawable arg0) {
		final GL gl = arg0.getGL();
		if(levelFinished) {
			SoundHandler.stopWalkAccordingToGivenInt(0);
			SoundHandler.stopWalkAccordingToGivenInt(1);
			SoundHandler.stopWalkAccordingToGivenInt(2);
			SoundHandler.levelFinishedDoorSound();
			SoundHandler.initGameHeartbeat();
			gl.glDeleteLists(objects.get(0), objects.get(objects.size()-1)+1);
			objects = new ArrayList<Integer>();
			wallObjectsList = new ArrayList<Surface>();
			GameData.level++;
			gameLevel = GameData.level;
			world = new World(gameLevel);
			levelFinished = false;
			camera.x = 10;
			camera.y = 10;
			camera.z = 10;
			this.init(arg0);
		}
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT  | GL.GL_DEPTH_BUFFER_BIT); // clear the depth buffer and the color buffer
		gl.glMatrixMode(GL.GL_MODELVIEW); // switch to model matrix
		((Object) gl).glLoadIdentity(); // init the matrix

		Vector3D lookDirection = getLookDirection();

		glu.gluLookAt(	camera.x,			camera.y,			camera.z,
				camera.x - lookDirection.x,	camera.y - lookDirection.y,	camera.z - lookDirection.z,
				upVector.x,			upVector.y,			upVector.z); //set the camera view

		for (int i = 0; i < objects.size(); i++) {
			gl.glCallList(objects.get(i));	
		}
		gl.glFlush();
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambientFloatBuffer);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, colorFloatBuffer);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, colorFloatBuffer);
		gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, positionFloatBuffer);
	}

	@Override
	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	}

	@Override
	public void init(GLAutoDrawable gLDrawable) {
		final GL gl = gLDrawable.getGL(); //get the GL from the GLAutoDrawable
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glEnable(GL.GL_LIGHTING);
		gl.glEnable(GL.GL_LIGHT0);
		gl.glEnable(GL.GL_LIGHT1);
		gl.glEnable(GL.GL_NORMALIZE);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL.GL_DEPTH_TEST); // enable depth buffer
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // define the clear color to black
		glu = new GLU(); //init the GLU object
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glMatrixMode(GL.GL_PROJECTION); //switch to projection matrix for more realistic image mode
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		gl.glEnable(GL.GL_TEXTURE_2D);
		int[] allWorldObjects = world.makeWorldSurfaces(gl, wallObjectsList, gameLevel);
		for(int i = 0; i < allWorldObjects.length; i++){
			objects.add(allWorldObjects[i]);
		}
		gl.glLoadIdentity();
		glu.gluPerspective(view_fov, Settings.FRAME_WIDTH/Settings.FRAME_HEIGHT, 1, 1000); // init the 3D perspective view
		if(!gameStateChecker.isAlive()){
			gameStateChecker.start();
		}
	}

	@Override
	public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width,
			int height) {
		final GL gl = gLDrawable.getGL();
		if(height <= 0) {
			height = 1;
		}
		float h = (float)width / (float)height;
//		gl.glMatrixMode(GL.GL_PROJECTION);
//		gl.glLoadIdentity();
//		glu.gluPerspective(view_fov, h, 1.0, 3000.0);
//		gl.glMatrixMode(GL.GL_MODELVIEW);
//		gl.glLoadIdentity();
	}


	private Vector3D getLookDirection() {
		Vector3D lookDirection = new Vector3D(
				-Math.cos(Math.toRadians(lookXAngle))*Math.cos(Math.toRadians(lookYAngle)),
				-Math.sin(Math.toRadians(lookYAngle)),
				Math.sin(Math.toRadians(lookXAngle))*Math.cos(Math.toRadians(lookYAngle)));
		return lookDirection;


	}

	private ArrayList<Surface> getProximityCloseWalls(
			Vector3D beforeMoveTest, Vector3D afterMoveTest) {
		ArrayList<Surface> proximityCloseWalls = new ArrayList<Surface>();
		for(int i = 0; i < wallObjectsList.size(); i++) {
			if(wallObjectsList.get(i).getInitPlace().isProximityClose(beforeMoveTest)) {
				proximityCloseWalls.add(wallObjectsList.get(i));
			} else if(wallObjectsList.get(i).getEndPlace().isProximityClose(beforeMoveTest)) {
				proximityCloseWalls.add(wallObjectsList.get(i));
			} else if(wallObjectsList.get(i).getInitPlace().isProximityClose(afterMoveTest)) {
				proximityCloseWalls.add(wallObjectsList.get(i));
			} else if(wallObjectsList.get(i).getEndPlace().isProximityClose(afterMoveTest)) {
				proximityCloseWalls.add(wallObjectsList.get(i));
			}
		}
		return proximityCloseWalls;
	}


	private boolean hasReachMazeExit(Vector3D afterMove) {
		int maxXBarrier = (Settings.MAZE_WIDTH + gameLevel) * 30;
		int maxZBarrier = (Settings.MAZE_DEPTH + gameLevel) * 30;
		int xAxisBarrier = maxXBarrier - 10;
		int zAxisBarrier = maxZBarrier - 10;
		PairPoint pp = new PairPoint(xAxisBarrier, zAxisBarrier, maxXBarrier, maxZBarrier);
		if(pp.betweenPoints(afterMove)) {
			return true;
		}
		return false;
	}

	private void showLevelCompleteMessage() {
		//SoundHandler.WALK.stop();
		SoundHandler.stopWalkAccordingToGivenInt(Floor.getFloorNumber());
		String msg = "level " + gameLevel + " finished! starting level " +  (gameLevel + 1);
		JOptionPane.showMessageDialog(jFrame, msg);
		SoundHandler.stopWalkAccordingToGivenInt(Floor.getFloorNumber());
	}

	private boolean isMoveAvailable(Vector3D beforeMoveVector, Vector3D moveVector) {
		Vector3D afterMoveTest = new Vector3D
				(beforeMoveVector.x - moveVector.x, 
						beforeMoveVector.y - moveVector.y, 
						beforeMoveVector.z - moveVector.z);

		Vector3D beforeMoveTest = new Vector3D
				(beforeMoveVector.x, 
						beforeMoveVector.y, 
						beforeMoveVector.z);
		ArrayList<Surface> proximityCloseWalls = new ArrayList<Surface>();
		proximityCloseWalls.addAll(getProximityCloseWalls(beforeMoveTest, afterMoveTest));
		for(int i = 0; i < proximityCloseWalls.size(); i++) {
			if(proximityCloseWalls.get(i).isColliding(afterMoveTest))
				return true;
			if(proximityCloseWalls.get(i).isPassed(beforeMoveTest, afterMoveTest))
				return true;
		}
		return false;
	}


	// moveForward - move forward.
	public void moveForward(double step) {
		Vector3D moveVector = getLookDirection();
		moveVector.y = 0;
		moveVector.normalize();
		moveVector = moveVector.scalarMultiply(step);
		boolean moveTest = isMoveAvailable(camera, moveVector);
		if(moveTest) {
			return;
		}
		camera.x -= moveVector.x;
		camera.y -= moveVector.y;
		camera.z -= moveVector.z;
	}


	// moveBackwards - move backward.
	public void moveBackwards(double step) {
		moveForward(-step);
	}

	// moveRight - move (strafe) right.
	public void moveRight(double step) {
		Vector3D moveVector = getLookDirection();
		Vector3D upVectorMove = new Vector3D(upVector.x, upVector.y, upVector.z);
		moveVector = upVectorMove.vectorMultiply(moveVector);
		moveVector.normalize();
		moveVector = moveVector.scalarMultiply(-step);
		boolean moveTest = isMoveAvailable(camera, moveVector);
		if(moveTest) {
			return;
		}
		camera.x -= moveVector.x;
		camera.y -= moveVector.y;
		camera.z -= moveVector.z;
	}

	// moveLeft - move (strafe) left.
	public void moveLeft(double step) {
		moveRight(-step);
	}

	// moveUp - move up.
	public void moveUp(double step) {
		Vector3D moveVector = new Vector3D(upVector.x, upVector.y, upVector.z);
		moveVector.normalize();
		moveVector = moveVector.scalarMultiply(-step);
		boolean moveTest = isMoveAvailable(camera, moveVector);
		if(moveTest) {
			return;
		}
		camera.x -= moveVector.x;
		camera.y -= moveVector.y;
		camera.z -= moveVector.z;
	}

	// moveDown - move down.
	public void moveDown(double step) {
		moveUp(-step);
	}

	// yawLeft - look left.
	public void yawLeft(double amount) {
		yawRight(-amount);
	}

	// yawRight - look right.
	public void yawRight(double amount) {
		lookXAngle = (lookXAngle+amount) % 360;
	}

	// pitchDown - look down.
	public void pitchDown(double amount) {
		pitchUp(-amount);
	}

	// pitchUp - look up.
	public void pitchUp(double amount) {

		double temp = lookYAngle + amount;
		if(Math.abs(temp) < 90) {
			lookYAngle = temp;
		}
	}

	// rollRight - tilt view to the right.
	//public void rollRight() {
		//		Vector3D temp1 = new Vector3D(vecX);
		//		Vector3D temp2 = new Vector3D(upVector);
		//		vecX = (temp1.scalarMultiply(Math.cos((rad_alpha_step)))).sub(
		//				temp2.scalarMultiply(Math.sin((rad_alpha_step))));
		//		upVector = (temp1.scalarMultiply(Math.sin((rad_alpha_step)))).add(
		//				temp2.scalarMultiply(Math.cos((rad_alpha_step))));
		//		vecX.normalize();
		//		upVector.normalize();
	//}

	// rollLeft - tilt view to the left.
	//public void rollLeft() {
		//		Vector3D temp1 = new Vector3D(vecX);
		//		Vector3D temp2 = new Vector3D(upVector);
		//		vecX = (temp1.scalarMultiply(Math.cos((-rad_alpha_step)))).sub(
		//				temp2.scalarMultiply(Math.sin((-rad_alpha_step))));
		//		upVector = (temp1.scalarMultiply(Math.sin((-rad_alpha_step)))).add(
		//				temp2.scalarMultiply(Math.cos((-rad_alpha_step))));
		//		vecX.normalize();
		//		upVector.normalize();
	//}

	private class GameStateChecker extends Thread {


		private long timeout = 250;

		@Override
		public void run() {
			while(true) {
				if(levelFinished = hasReachMazeExit(camera)) {
					showLevelCompleteMessage();
				}				
				try {
					GameStateChecker.sleep(timeout);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
		}

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}
}
