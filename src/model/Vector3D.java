package model;

import util.Collidable;
import util.CollisionDetector;
import util.Point3D;


/**
 * This class is responsible for drawing, filling polygons; saving, loading
 * to a .scn file.
 */
/**
 * * This class represents a 3D vector with supporting methods
 */
public class Vector3D implements Collidable{

	// vector's values.
	public double x;
	public double y;
	public double z;

	//Constructor.
	public Vector3D(double x,double y,double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	//Copy Constructor.
	public Vector3D(Vector3D other){
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}
	
	// add - updates "this" vector to be: this = this + other.
	public Vector3D add(Vector3D other){
		Vector3D toReturn = new Vector3D(this);
		toReturn.x += other.x;
		toReturn.y += other.y;
		toReturn.z += other.z;
		return toReturn;
	}

	// sub - updates "this" vector to be: this = this - other.
	public Vector3D sub(Vector3D other){
		Vector3D toReturn = new Vector3D(this);
		toReturn.x -= other.x;
		toReturn.y -= other.y;
		toReturn.z -= other.z;
		return toReturn;
	}

	/* multiply - returns result of "this" vector*scalar
	   without change "this". */
	public Vector3D scalarMultiply(double scalar){
		Vector3D newVector = new Vector3D(this);
		newVector.x *= scalar;
		newVector.y *= scalar;
		newVector.z *= scalar;
		return newVector;
	}
	/* multiply - returns result of "this" vector*scalar
	   without change "this". */
	public Vector3D vectorMultiply(Vector3D other){
		Vector3D newVector = new Vector3D(this);
		newVector.x = (this.y*other.z-this.z*other.y);
		newVector.y = (this.z*other.x-this.x*other.z);
		newVector.z = (this.x*other.y-this.y*other.x);
		return newVector;
	}
	
	// normalize - normalized "this" vector.
	public void normalize(){
		//calculate the normal.
		double normal = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2)+Math.pow(z, 2));
		//check for division in zero.
		if (normal!=0){
			this.x /= normal;
			this.y /= normal;
			this.z /= normal;
		}
	}
	
	/**
	 * Sets new coordinates for this vector
	 * @param x the x coordinates to set 
	 * @param y the y coordinates to set
	 * @param z the z coordinates to set
	 */
	public void setVector(double x,double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public boolean isProximityClose(Vector3D vectorToCheck) {
		int proximityCloseArea = 16;
		if(this.x - vectorToCheck.x > proximityCloseArea) {
			return false;
		}
		if(this.y - vectorToCheck.y > proximityCloseArea) {
			return false;
		}
		if(this.z - vectorToCheck.z > proximityCloseArea) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "("+x+","+y+","+z+")";
	}

	@Override
	public boolean isColliding(Collidable other) {
		return false;
	}

	@Override
	public CollisionDetector getCollisionDetector() {
		Point3D vectorCD = new Point3D((float)(x), (float)(y), (float)(z));
		return vectorCD;
	}

	public float[] getFloatArray() {
		float[] result = {(float) x,(float) y,(float) z};
		return result;
	}
}
