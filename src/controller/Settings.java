package controller;

public class Settings {
	public static final boolean FULL_SCREEN = true;
//	public static int FRAME_WIDTH = 800;
//	public static int FRAME_WIDTH = 800;
	public static int FRAME_HEIGHT = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;;
	public static int FRAME_WIDTH = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;;
	public static final int MAZE_WIDTH = 2;
	public static final int MAZE_DEPTH = 2;
	public static final double VIEW_FOV = 110.0f; //Field of view for the projection window. 45.0f works like normal view.
}