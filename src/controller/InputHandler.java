package controller;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import sound.SoundHandler;

public class InputHandler implements MouseMotionListener, KeyListener {
	private Controller controller;
	protected static Point mouseCurrentDestination = new Point();
	protected static Point mouseOriginPoint = new Point();
	protected boolean robMouseMovedFlag = false;
	protected static final double step = 1;
	private Robot rob;
	private Point absoluteMiddle;
	private static int floorNumber = 0;

	public InputHandler(Controller controller) {
		this.controller = controller;
		try {
			rob = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void mouseMoved(MouseEvent mouseEvent) {	

		absoluteMiddle = controller.getFrameAbsolute();
		if(robMouseMovedFlag) {
			mouseOriginPoint = mouseEvent.getPoint();
			robMouseMovedFlag = false;
			return;
		}
		
		mouseCurrentDestination = mouseEvent.getPoint();
		Point translateDistance = calculateOffset(mouseCurrentDestination, mouseOriginPoint);
		
		mouseOriginPoint = mouseEvent.getPoint();
		robMouseMovedFlag = true;
		rob.mouseMove(absoluteMiddle.x, absoluteMiddle.y);
		controller.getDisplay().yawLeft(translateDistance.getX()*0.5);
		controller.getDisplay().pitchDown(translateDistance.getY()*0.5);
	}
	
	
	private Point calculateOffset(Point mouseCurrentDestination,
			Point mouseOriginPoint) {
		Point translateDistance = new Point((int)(mouseCurrentDestination.getX() - mouseOriginPoint.getX()),
				(int)(mouseCurrentDestination.getY() - mouseOriginPoint.getY()));
		return translateDistance;
	}

	@Override
	public void mouseDragged(MouseEvent mouseEvent) {
	}
	
	

	public void keyPressed(KeyEvent arg0) {
		//arg0.
		switch(arg0.getKeyCode()) {
		case  KeyEvent.VK_S:
			controller.getDisplay().moveBackwards(step);
			SoundHandler.playWalkAccordingToGivenInt(floorNumber);
		break;
		case KeyEvent.VK_W:	
			controller.getDisplay().moveForward(step);
			SoundHandler.playWalkAccordingToGivenInt(floorNumber);
		break;
		case KeyEvent.VK_D:
			controller.getDisplay().moveRight(step);
			SoundHandler.playWalkAccordingToGivenInt(floorNumber);
		break;
		case KeyEvent.VK_A:
			controller.getDisplay().moveLeft(step);
			SoundHandler.playWalkAccordingToGivenInt(floorNumber);
		break;
		case KeyEvent.VK_E:
			controller.getDisplay().moveUp(1);
			SoundHandler.playWalkAccordingToGivenInt(floorNumber);
		break;
		case KeyEvent.VK_Q:
			controller.getDisplay().moveDown(1);
			SoundHandler.playWalkAccordingToGivenInt(floorNumber);
		break;
		case KeyEvent.VK_I: controller.getDisplay().pitchUp(10);
		break;
		case KeyEvent.VK_K: controller.getDisplay().pitchDown(10);
		break;

		case KeyEvent.VK_L: controller.getDisplay().yawRight(-10);
		break;
		case KeyEvent.VK_J: controller.getDisplay().yawLeft(-10);
		break;
		//case KeyEvent.VK_O: controller.getDisplay().rollRight();
		//break;
		//case KeyEvent.VK_U: controller.getDisplay().rollLeft();
		//break;
		}
	}
	public void keyReleased (KeyEvent arg0) {
		//arg0.
		switch(arg0.getKeyCode()) {
		case  KeyEvent.VK_S:
			SoundHandler.stopWalkAccordingToGivenInt(floorNumber);
		break;
		case KeyEvent.VK_W: 
			SoundHandler.stopWalkAccordingToGivenInt(floorNumber);
		break;
		case KeyEvent.VK_D: 
			SoundHandler.stopWalkAccordingToGivenInt(floorNumber);
		break;
		case KeyEvent.VK_A: 
			SoundHandler.stopWalkAccordingToGivenInt(floorNumber);
		break;
		case KeyEvent.VK_E: 
			SoundHandler.stopWalkAccordingToGivenInt(floorNumber);
		break;
		case KeyEvent.VK_Q:
			SoundHandler.stopWalkAccordingToGivenInt(floorNumber);
		break;
		case KeyEvent.VK_I: 
		break;
		case KeyEvent.VK_K: 
		break;
		case KeyEvent.VK_L: 
		break;
		case KeyEvent.VK_J: 
		break;
		case KeyEvent.VK_O: 
		break;
		case KeyEvent.VK_U: 
		break;
		case KeyEvent.VK_F1: 
			controller.instructionsMsg();
		break;
		case KeyEvent.VK_F11: 
			controller.skipLevel();
		break;
		case KeyEvent.VK_ESCAPE: 
			System.exit(0);
		break;
		}
	}
	
	public void keyTyped(KeyEvent e) {
	}

	public double getStep() {
		return step;
	}
	
	public static void floorNumberChanged(int givenFloorNumber) {
		floorNumber = givenFloorNumber;
	}
}
