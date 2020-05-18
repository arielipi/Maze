package util;

import model.Vector3D;

public class DistanceCalculator {
	public static double distanceSquared(double x0,double y0,double z0,double x1,double y1,double z1) {
		double dx = x1-x0;
		double dy = y1-y0;
		double dz = z1-z0;
		double distanceSquared = dx*dx + dy*dy + dz*dz;		
		return distanceSquared;
	}
	
	public static double distanceSquared(Vector3D pointA,Vector3D pointB) {
		return distanceSquared(pointA.x, pointA.y, pointA.z, pointB.x, pointB.y, pointB.z);
	}
	public static double distance(Vector3D pointA,Vector3D pointB) {
		return Math.sqrt(distanceSquared(pointA, pointB));
	}
	
	public static double distance(double x0,double y0,double z0,double x1,double y1,double z1) {
		return Math.sqrt(distanceSquared(x0,y0,z0,x1,y1,z1));
	}
}
