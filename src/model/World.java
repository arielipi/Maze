package model;

import java.util.ArrayList;
import javax.media.opengl.GL;
import maze.MazeGenerator;
import maze.MazeParser;
import maze.PairPoint;


import controller.Settings;


public class World {

	private MazeGenerator mazeGen;
	private int mazeGenRows;
	private int numberOfWalls;
	
	
	public World(int level) {
		mazeGen = new MazeGenerator(Settings.MAZE_WIDTH + level, Settings.MAZE_DEPTH + level);
		mazeGen.display();
		mazeGenRows = 0;
		numberOfWalls = 0;
		Ceiling.setMetallicTexture(null);
		Floor.setFloorBrickNormalTexture(null);
		Wall.setWallRedTexture(null);
		Door.setDoorTexture(null);
	}
	
	private int makeWall(GL gl, ArrayList<Surface> wallObjectsList, 
						 float initWidth, float initHeight, float initDepth,
						 float endWidth, float endHeight, float endDepth, 
						 float repeatFirstValue, float repeatSecondValue) {
		int list = gl.glGenLists(1);
		Wall wall = new Wall(gl, list, initWidth, initHeight, 
							initDepth, endWidth, endHeight, endDepth, 
							repeatFirstValue, repeatSecondValue);
		wallObjectsList.add(wall);
		list = wall.makeWall(gl);
		return list;
	}
	
	private int makeFloor(GL gl, ArrayList<Surface> wallObjectsList, 
						  float initWidth, float initHeight, float initDepth,
						  float endWidth, float endHeight, float endDepth, 
						  float repeatFirstValue, float repeatSecondValue) {
		int list = gl.glGenLists(1);
		Floor floor = new Floor(gl, list, initWidth, initHeight, 
								initDepth, endWidth, endHeight, endDepth, 
								repeatFirstValue, repeatSecondValue);
		wallObjectsList.add(floor);
		list = floor.makeFloor(gl);
		return list;
	}
	
	private int makeCeiling(GL gl, ArrayList<Surface> wallObjectsList, 
						    float initWidth, float initHeight, float initDepth,
						    float endWidth, float endHeight, float endDepth, 
						    float repeatFirstValue, float repeatSecondValue) {
		int list = gl.glGenLists(1);
		Ceiling ceiling = new Ceiling(gl, list, initWidth, initHeight, 
									  initDepth, endWidth, endHeight, endDepth, 
									  repeatFirstValue, repeatSecondValue);
		wallObjectsList.add(ceiling);
		list = ceiling.makeCeiling(gl);
		return list;
	}
	
	private int makeDoor(GL gl, float initWidth, float initHeight, float initDepth,
			  					float endWidth, float endHeight, float endDepth, 
			  					float repeatFirstValue, float repeatSecondValue) {
		int list = gl.glGenLists(1);
		Door door = new Door(gl, list, initWidth, initHeight, 
							initDepth, endWidth, endHeight, endDepth, 
							repeatFirstValue, repeatSecondValue);
		list = door.makeDoor(gl);
		return list;
	}

	
	public int[] makeWorldSurfaces(GL gl, ArrayList<Surface> wallObjectsList, int level) {
		ArrayList<PairPoint> wallsZAxisPointsPositions = new ArrayList<PairPoint>();
		ArrayList<PairPoint> wallsXAxisPointsPositions = new ArrayList<PairPoint>();
		MazeParser mazeParser = new MazeParser();
		wallsXAxisPointsPositions.addAll(mazeParser.parseMazeEvenRows(mazeGen));
		wallsZAxisPointsPositions.addAll(mazeParser.parseMazeOddRows(mazeGen, mazeGenRows));
		numberOfWalls = wallsXAxisPointsPositions.size() + wallsZAxisPointsPositions.size();
		
		int[] wallsList = new int[numberOfWalls + 4];
		int xAxisSize = wallsXAxisPointsPositions.size();
		int zAxisSize = wallsZAxisPointsPositions.size();
		int roofAndFloorXAxisLength = (Settings.MAZE_WIDTH + level) * 30;
		int roofAndFloorZAxisLength = (Settings.MAZE_DEPTH + level) * 30;
		float yMin = 0;
		float yMax = 20;
		float xRepeat = 8.0f;
		float zRepeat = 8.0f;
		float roofXRepeat = 32.0f; 
		float roofZRepeat = 32.0f;
		float floorXRepeat = 32.0f;
		float floorZRepeat = 32.0f;
		for(int i = 0; i < wallsList.length; i++) {
			wallsList[i] = 0;
		}
		//make z-axis-flat walls
		for(int i = 0; i < xAxisSize; i++) {
			PairPoint pp = wallsXAxisPointsPositions.get(i);
			wallsList[i] = makeWall(gl, wallObjectsList, 
					(float)pp.getLeftPoint().getX(), yMin, (float)pp.getLeftPoint().getY(),
					(float)pp.getRightPoint().getX(), yMax, (float)pp.getRightPoint().getY(),
					xRepeat, zRepeat);
		}
		//make x-axis-flat walls
		for(int i = 0; i < zAxisSize; i++) {
			PairPoint pp = wallsZAxisPointsPositions.get(i);
			wallsList[i + xAxisSize] = makeWall(gl, wallObjectsList, 
					(float)pp.getLeftPoint().getX(), yMin, (float)pp.getLeftPoint().getY(),
					(float)pp.getRightPoint().getX(), yMax, (float)pp.getRightPoint().getY(),
					xRepeat, zRepeat);
		}
		wallsList[numberOfWalls] = makeFloor
				(gl, wallObjectsList, 
				 0,							yMin,	  					  0, 
				 roofAndFloorXAxisLength,	yMin,	roofAndFloorZAxisLength, 
				 floorXRepeat, floorZRepeat);
		wallsList[numberOfWalls + 1] = makeCeiling
				(gl, wallObjectsList, 
				 0,							yMax,	  					  0, 
				 roofAndFloorXAxisLength,	yMax,	roofAndFloorZAxisLength, 
				 roofXRepeat, roofZRepeat);
		wallsList[numberOfWalls + 2] = makeDoor
				(gl,  
				 roofAndFloorXAxisLength - 1,	yMin,	roofAndFloorZAxisLength - 10, 
				 roofAndFloorXAxisLength - 1,	yMax,	roofAndFloorZAxisLength, 
				 1.0f, 1.0f);
		wallsList[numberOfWalls + 3] = makeDoor
				(gl,  
				 1,	yMin,	0, 
				 1,	yMax,	10, 
				 1.0f, 1.0f);
		return wallsList;
		
	}
}
