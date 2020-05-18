package maze;

import java.util.ArrayList;

public class MazeParser {
	
	private ArrayList<PairPoint> parseMazeEvenRow(char[] mazeRowCharArray, int z) {
		ArrayList<PairPoint> pointArrayXAxis = new ArrayList<PairPoint>();
		int xStart = 0;
		int xEnd = 0;
		int steps = 0;
		int stepLengthWall = 10;
		int minusCounter = 0;
		for(int i = 0; i < mazeRowCharArray.length; i++) {
			if(mazeRowCharArray[i] == '-') {
				minusCounter++;
				xEnd = xEnd + stepLengthWall;
				if(minusCounter == 3) {
					pointArrayXAxis.add(new PairPoint(xStart, z, xStart + xEnd, z));
					minusCounter = 0;
					xEnd = 0;
				}
			}
			else if(mazeRowCharArray[i] == '+') {
				xStart = steps;
				steps = steps - stepLengthWall;
			}
			steps = steps + stepLengthWall;
		}
		return pointArrayXAxis;
	}
	
	private ArrayList<PairPoint> parseMazeOddRow(char[] mazeRowCharArray, int z) {
		ArrayList<PairPoint> pointArrayZAxis = new ArrayList<PairPoint>();
		int steps = 0;
		int fourthSpaceCounter = 0;
		int stepLengthWall = 10;
		int zLength = 3 * stepLengthWall;
		for(int i = 0; i < mazeRowCharArray.length; i++) {
			if(mazeRowCharArray[i] == '|') {
				pointArrayZAxis.add(new PairPoint(steps, z, steps, z + zLength));
				steps = steps - stepLengthWall;
				fourthSpaceCounter = 0;
			}
			
			else if(mazeRowCharArray[i] == ' ') {
				fourthSpaceCounter++;
				if(fourthSpaceCounter == 4) {
					fourthSpaceCounter = 0;
					steps = steps - stepLengthWall;
				}
			}
			steps = steps + stepLengthWall;
		}
		return pointArrayZAxis;
		
	}
	
	public ArrayList<PairPoint> parseMazeEvenRows(MazeGenerator mazeGen) {
		ArrayList<PairPoint> evenRowsPointArray = new ArrayList<PairPoint>();
		String[] mazeStringDisplay = mazeGen.getMazeStringDisplay().split("\n");
		int mazeRows = mazeStringDisplay.length;
		int zAxis = 0;
		int zStepLength = 30;
		// x: + -
		// z: | 'space'
		for(int i = 0; i < mazeRows; i++) {
			if((i % 2) == 0) { // + - row
				char[] mazeRowCharArray = mazeStringDisplay[i].toCharArray();
				evenRowsPointArray.addAll(parseMazeEvenRow(mazeRowCharArray, zAxis));
				zAxis = zAxis + zStepLength;
			}
		}
		return evenRowsPointArray;
	}
	
	public ArrayList<PairPoint> parseMazeOddRows(MazeGenerator mazeGen, int mazeGenRows) {
		ArrayList<PairPoint> oddRowsPointArray = new ArrayList<PairPoint>();
		String[] mazeStringDisplay = mazeGen.getMazeStringDisplay().split("\n");
		int mazeRows = mazeStringDisplay.length;
		mazeGenRows = mazeRows;
		int zAxis = 0;
		int zStepLength = 30;
		// x: + -
		// z: | 'space'
		for(int i = 0; i < mazeRows; i++) {
			if((i % 2) == 1) { // | 'space' row
				char[] mazeRowCharArray = mazeStringDisplay[i].toCharArray();
				oddRowsPointArray.addAll(parseMazeOddRow(mazeRowCharArray, zAxis));
				zAxis = zAxis + zStepLength;
			}
		}
		return oddRowsPointArray;
	}
	
	/**
	 * not tested, use parseMazeOddRows and parseMazeEvenRows instead
	 * @param mazeGen
	 * @param mazeGenRows
	 * @param numberOfWalls
	 * @return
	 */
	public ArrayList<PairPoint> parseMaze(MazeGenerator mazeGen, 
			int mazeGenRows, int numberOfWalls) {
		ArrayList<PairPoint> pointArray = new ArrayList<PairPoint>();
		String[] mazeStringDisplay = mazeGen.getMazeStringDisplay().split("\n");
		int mazeRows = mazeStringDisplay.length;
		mazeGenRows = mazeRows;
		int zAxis = 0;
		int zStepLength = 30;
		// x: + -
		// z: | 'space'
		for(int i = 0; i < mazeRows; i++) {
			char[] mazeRowCharArray = mazeStringDisplay[i].toCharArray();
			if((i % 2) == 0) { // + - row
				pointArray.addAll(parseMazeEvenRow(mazeRowCharArray, zAxis));
			}
			else { // | 'space' row
				pointArray.addAll(parseMazeOddRow(mazeRowCharArray, zAxis));
				zAxis = zAxis + zStepLength;
			}
		}
		numberOfWalls = pointArray.size();
		return pointArray;
	}
}
