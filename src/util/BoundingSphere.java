package util;

import java.util.Collection;
import java.util.Iterator;
import model.Vector3D;

public class BoundingSphere extends CollisionDetector {
	private Vector3D center;
	private double radiusSquared;//R^2 for faster calculations
	
	

	public BoundingSphere(Collection<float[]> vertices) {
		detectionType = "BS";
		simpleBoundingSphere(vertices);
	}

	private void simpleBoundingSphere(Collection<float[]> vertices) {
		center = findCenter(vertices);
		radiusSquared = findRadius(vertices);
	}

	/**
	 * @return the radiusSquared
	 */
	public double getRadiusSquared() {
		return radiusSquared;
	}
	
	/**
	 * @return the center
	 */
	public Vector3D getCenter() {
		return center;
	}
	
	private double findRadius(Collection<float[]> vertices) {
		float[] centerF = {(float) this.center.x, (float) this.center.y, (float) this.center.z};
		return findMaxDistFromPoint(vertices, centerF);		
	}

	private Vector3D findCenter(Collection<float[]> vertices) {
		double x = 0, y = 0, z = 0;		 
		for (Iterator<float[]> iterator = vertices.iterator(); iterator.hasNext();) {
			float[] vertex = (float[]) iterator.next();
			x += vertex[0];
			y += vertex[1];
			z += vertex[2];			
		}
		x = x/vertices.size();
		y = y/vertices.size();
		z = z/vertices.size();
		
		return new Vector3D(x, y, z);
	}
	
	private double findMaxDistFromPoint(Collection<float[]> vertices, float[] point) {

		float[] srcPoint = point; 
		Iterator<float[]> iterator = vertices.iterator();
		double currentMaxDistSquared = 0;
		while(iterator.hasNext()) {
			float[] nextPoint = iterator.next();
			double currentDistanceSquared = DistanceCalculator.distanceSquared(
					srcPoint[0], srcPoint[1], srcPoint[2],
					nextPoint[0], nextPoint[1], nextPoint[2]);
			if(currentDistanceSquared > currentMaxDistSquared) {
				currentMaxDistSquared = currentDistanceSquared;
			}			
		}
		return currentMaxDistSquared;	
	}

	@Override
	public boolean isColliding(Collidable objectA, Collidable objectB) {
		return false;
	}

	@Override
	public boolean isCollidingCollisionDetections(CollisionDetector objectB) {
		if(objectB.getDetectionType().equals("AABB")) {
			return isCollidingAABB((AxisAlignedBoundingBox)objectB);
		} else if(objectB.getDetectionType().equals("BS")) {
			return isCollidingBS((BoundingSphere)objectB);
		} else if(objectB.getDetectionType().equals("POINT")) {
			return isCollidingPoint((Point3D)objectB);
		}
		return false;
	}

	private boolean isCollidingPoint(Point3D objectB) {
		float dx = (float) (objectB.getX() - this.center.x);
		float dy = (float) (objectB.getY() - this.center.y);
		float dz = (float) (objectB.getZ() - this.center.z);
		float d;
		dx = dx * dx;
		dy = dy * dy;
		dz = dz * dz;
		d = dx + dy + dz;
		if(d < radiusSquared) {
			return true;
		}
		return false;
	}

	private boolean isCollidingBS(BoundingSphere objectB) {
		double dx = objectB.center.x - this.center.x;
		double dy = objectB.center.y - this.center.y;
		double dz = objectB.center.z - this.center.z;
		double d;
		double radiiSumSquared; 
		dx = dx * dx;
		dy = dy * dy;
		dz = dz * dz;
		d = dx + dy + dz;
		radiiSumSquared = this.radiusSquared + objectB.radiusSquared;
		radiiSumSquared = radiiSumSquared * radiiSumSquared;
		if(d <= radiiSumSquared) {
			return true;
		}
		return false;
	}

	private boolean isCollidingAABB(AxisAlignedBoundingBox objectB) {
		return objectB.isCollidingCollisionDetections(this);
	}

}
