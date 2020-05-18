package util;

public interface Collidable {
	public boolean isColliding(Collidable other);
	public CollisionDetector getCollisionDetector();
}
