package maze;

import java.awt.Point;

import model.Vector3D;

public class PairPoint {
	private Point leftPoint;
	private Point rightPoint;
	
	public PairPoint(int lpX, int lpY, int rpX, int rpY) {
		leftPoint = new Point(lpX, lpY);
		rightPoint = new Point(rpX, rpY);
	}
	
	public Point getLeftPoint() {
		return leftPoint;
	}
	
	public Point getRightPoint() {
		return rightPoint;
	}
	
	/**
	 * assumes this marks the exit barriers from the maze
	 * leftPoint is the minimum values of the barrier
	 * rightPoint is the maximum values of the barrier.
	 * (x, y) correspond to (x, z)
	 * @param afterMove holds x y and z coords of the player position.
	 * @return true if player position is at exit, otherwise false.
	 */
	public boolean betweenPoints(Vector3D cameraPoint) {
		if(leftPoint.x > cameraPoint.x) {
			return false;
		} else if(rightPoint.x < cameraPoint.x) {
			return false;
		} else if(leftPoint.y > cameraPoint.z) {
			return false;
		} else if(rightPoint.y < cameraPoint.z) {
			return false;
		}
		return true;
	}

}
