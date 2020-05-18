package util;

public abstract class CollisionDetector {
	protected String detectionType;
	
	public abstract boolean isColliding(Collidable objectA, Collidable objectB);
	
	public abstract boolean isCollidingCollisionDetections(CollisionDetector objectB);
	
	public String getDetectionType() {return detectionType;}
	
	public void setDetectionType(String typeToSet) {detectionType = typeToSet;}
}
