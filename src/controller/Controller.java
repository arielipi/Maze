package controller;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;


import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import sound.SoundHandler;
import view.DisplayHandler;
import view.MyGLJpanel;

public class Controller implements Runnable {
	DisplayHandler displayHandler;
	InputHandler inputHandler;
	Frame frame;
	GLCanvas gameGLCanvas;
	MyGLJpanel menuPanel;
	Animator animator;
	Container helpMenu = new Container();
	String skipLevelMessage;
	JFrame jFrame;
	JFrame instructionsFrame;
	String instructionsMessage;
	
	public Controller() {
		SoundHandler.initGameManGasping();
		displayHandler = new DisplayHandler();
		inputHandler = new InputHandler(this);
		frame = new Frame("The Horror Maze");
		frame.setUndecorated(true);
		gameGLCanvas = new GLCanvas();
		menuPanel = new MyGLJpanel();
		animator = new Animator(gameGLCanvas);
		skipLevelMessage = "Skipped a level!";
		jFrame = new JFrame();
		jFrame.setUndecorated(true);
		instructionsFrame = new JFrame();
		SoundHandler.initGameCrazyLaughter();
		instructionsMessage = "Game Instructions:\n" +
		"Find your way out of the neverending, labyrinth of DOOM,\n" +
		"where each level presents you with a more difficult challenge.\n" +
		"Controls:\n" +
		"Use the A,W,S,D keys for movement, E,Q for up and down.\n" +
		"Use the I,J,K,L for looking, or simply use a mouse.\n" +
		"Press F1 to open the help menu.\n" +
		"Press F11 to skip a level.\n" +
		"Press ESC to exit the game.\n\n" + "ESCAPE.... if you can!";
		instructionsFrame.setUndecorated(true);
		SoundHandler.initGameBreathing();
	}
	
	private void initGameMessagePopUp() {
		String initGameMessage = "You wake up in a red room with a headache.\n" +
				 "You can't remember a thing or even where you are.\n\n" + 
				 "You NEED to find the exit, NOW!";
		JFrame initGameFrame = new JFrame();
		JOptionPane.showMessageDialog(initGameFrame, initGameMessage);
	}
	@Override
	public void run() {
		initGameEnvironment();
		SoundHandler.init();
		initGameMessagePopUp();
		runGame();
	}

	private void initGameEnvironment() {
		GameData.gameIsRunning = true;
		GameData.torchIsBurning = true;
		GameData.backgroundMusicLevel1 = true;
		gameGLCanvas.addGLEventListener(displayHandler);
		frame.setSize(Settings.FRAME_WIDTH, Settings.FRAME_HEIGHT);
		
		//it's display methods sequentially
		frame.add(gameGLCanvas); //add the canvas to the frame.
		
		// create a window adapter the handle the window closing event
		frame.addWindowListener(new WindowAdapter() {
			// to make sure the animator will be stopped before the system will exit
			public void windowClosing(WindowEvent e) {
				//crate a different thread that handle the animator closing and system exit
				new Thread(new Runnable() {				
					public void run() {
						animator.stop();
						frame.dispose();
						System.exit(0);
					}
				}).start(); 
			}
		});
		gameGLCanvas.addMouseMotionListener(inputHandler);
		gameGLCanvas.addKeyListener(inputHandler);
	}
	
	public void runGame() {		
		frame.setVisible(true);     // make the frame visible
		animator.start();       // start the animator
		BufferedImage image = frame.getGraphicsConfiguration().createCompatibleImage(1, 1, Transparency.BITMASK);
		Cursor invisibleCursor = frame.getToolkit().createCustomCursor(  
				image, new Point(0,0), "Invisible");  
		frame.setCursor(invisibleCursor);
		gameGLCanvas.requestFocus();
		while(GameData.gameIsRunning) {
			if(GameData.backgroundMusicLevel1) {
				SoundHandler.LEVEL1AMBIENCE.loop();
			}
			else {
				SoundHandler.LEVEL1AMBIENCE.stop();
			}
		}
	}

	public DisplayHandler getDisplay() {
		return displayHandler;
	}

	public InputHandler getInputHandler() {
		return inputHandler;
	}
	
	public Point getFrameAbsolute() {
		Point locationOnScreen = frame.getLocationOnScreen();
		Dimension box = frame.getSize();
		Point absoluteMiddle = new Point(
				(locationOnScreen.x+(box.width/2)),
				(locationOnScreen.y+(box.height/2)));
		return absoluteMiddle;
	}
	public static class GameData {
		public static boolean gameIsRunning = true;
		static boolean backgroundMusicLevel1 = true;
		static boolean torchIsBurning = true;
		public static int level = 1;
		
	}
	public void skipLevel() {
		JOptionPane.showMessageDialog(jFrame, skipLevelMessage);
		this.displayHandler.levelFinished = true;
	}
	
	public void instructionsMsg(){
		//JOptionPane.showMessageDialog(instructionsFrame, instructionsMessage);
		JOptionPane.showMessageDialog(instructionsFrame, instructionsMessage, "Instructions", 0, new ImageIcon("texture/mazeIcon.jpg"));		
	}
	
}
