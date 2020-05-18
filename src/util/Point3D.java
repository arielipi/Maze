package util;

public class Point3D extends CollisionDetector {

	private float x;
	private float y;
	private float z;
	
	public Point3D() {
		detectionType = "POINT";
	}
	
	public Point3D(float givenX, float givenY, float givenZ) {
		detectionType = "POINT";
		x = givenX;
		y = givenY;
		z = givenZ;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getZ() {
		return z;
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
		if(this.x == objectB.x) {
			if(this.y == objectB.y) {
				if(this.z == objectB.z) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isCollidingBS(BoundingSphere objectB) {
		return objectB.isCollidingCollisionDetections(this);
	}

	private boolean isCollidingAABB(AxisAlignedBoundingBox objectB) {
		return objectB.isCollidingCollisionDetections(this);
	}

}
