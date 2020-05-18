package util;

import java.util.ArrayList;
import model.Vector3D;

public class AxisAlignedBoundingBox extends CollisionDetector {
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	private double minZ;
	private double maxZ;
	
	
	public AxisAlignedBoundingBox() {
		detectionType = "AABB";
	}
	
	public AxisAlignedBoundingBox(ArrayList<float[]> vertices) {
		detectionType = "AABB";
		extractMinMax(vertices);
	}
	
	public void setNewVertices(ArrayList<float[]> vertices) {
		extractMinMax(vertices);		
	}

	/**
	 * Get the center of this AABB
	 * @return the center of the AABB
	 */
	public Vector3D getCenter() {
		return new Vector3D((maxX+minX)/2, (maxY+minY)/2, (maxZ+minZ)/2);
	}
	
	/**
	 * Get the size of this axis aligned bounding box X axis 
	 * @return the size of this axis aligned bounding box X axis
	 */
	public double getSizeX() {
		return maxX - minX;
	}
	/**
	 * Get the size of this axis aligned bounding box Y axis 
	 * @return the size of this axis aligned bounding box Y axis
	 */
	public double getSizeY() {
		return maxY - minY;
	}
	/**
	 * Get the size of this axis aligned bounding box Z axis 
	 * @return the size of this axis aligned bounding box Z axis
	 */
	public double getSizeZ() {
		return maxZ - minZ;
	}
	
	
	
	/**
	 * Assuming the object has more than a single point, it extracts minMax vals
	 * @param vertices the vertices from which to extract min max coordinates
	 */
	private void extractMinMax(ArrayList<float[]> vertices) {
		float[] temp = vertices.get(0);
		minX = temp[0];
		maxX = temp[0];
		minY = temp[1];
		maxY = temp[1];
		minZ = temp[2];
		maxZ = temp[2];
		for(float[] vertex:vertices) {
			if(vertex[0] < minX) {
				minX = vertex[0];
			}
			else if(vertex[0] > maxX) {
				maxX = vertex[0];
			}
			if(vertex[1] < minY) {
				minY = vertex[1];
			}
			else if(vertex[1] > maxY) {
				maxY = vertex[1];
			}
			if(vertex[2] < minZ) {
				minZ = vertex[2];
			}
			else if(vertex[2] > maxZ) {
				maxZ = vertex[2];
			}
		}
	}
	
	@Override
	public boolean isColliding(Collidable objectA, Collidable objectB) {
		return false;
	}
	
	@Override
	public boolean isCollidingCollisionDetections(CollisionDetector objectB) {
		if(objectB.getDetectionType().equals("AABB")) {
			return isCollidingAABB((AxisAlignedBoundingBox)objectB);
		}
		else if(objectB.getDetectionType().equals("BS")) {
			return isCollidingBS((BoundingSphere)objectB);
		}
		else if(objectB.getDetectionType().equals("POINT")) {
			return isCollidingPoint((Point3D)objectB);
		}
		
		return false;
	}
	
	/**
    * @param bs bounding sphere
    * @return true, if AABB intersects with sphere
    */
	private boolean isCollidingBS(BoundingSphere bs) {
		
        double s, d = 0;
        // find the square of the distance
        // from the sphere to the box
        
        // X //
        if (bs.getCenter().x < minX) {
            s = bs.getCenter().x - minX;
            d = s * s;
        } else if (bs.getCenter().x > maxX) {
            s = bs.getCenter().x - maxX;
            d += s * s;
        }

        // Y //
        if (bs.getCenter().y < minY) {
            s = bs.getCenter().y - minY;
            d += s * s;
        } else if (bs.getCenter().y > maxY) {
            s = bs.getCenter().y - maxY;
            d += s * s;
        }

        // Z //
        if (bs.getCenter().z < minZ) {
            s = bs.getCenter().z - minZ;
            d += s * s;
        } else if (bs.getCenter().z > maxZ) {
            s = bs.getCenter().z - maxZ;
            d += s * s;
        }

        if(d <= bs.getRadiusSquared() * bs.getRadiusSquared())
        	return true;
        return false;
	}
	
	//Check if Box1's max is greater than Box2's min and Box1's min is less than Box2's max
	private boolean isCollidingAABB(AxisAlignedBoundingBox aabbB) {
		if(this.maxX >= aabbB.minX) {
			if(this.minX <= aabbB.maxX) {
				if(this.maxY >= aabbB.minY) {
					if(this.minY <= aabbB.maxY) {
						if(this.maxZ >= aabbB.minZ) {
							if(this.minZ <= aabbB.maxZ) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean isCollidingPoint(Point3D point) {
		int extendedLength = 2;
		if(point.getX() <= this.maxX + extendedLength) {
			if(point.getX() >= this.minX - extendedLength) {
				if(point.getY() <= this.maxY + extendedLength) {
					if(point.getY() >= this.minY - extendedLength) {
						if(point.getZ() <= this.maxZ + extendedLength) {
							if(point.getZ() >= this.minZ - extendedLength) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean isPassedPlane(AxisAlignedBoundingBox aabb, 
			CollisionDetector beforePoint, CollisionDetector afterPoint) {
		if(beforePoint.getDetectionType().equals("POINT")) {
			if(afterPoint.getDetectionType().equals("POINT")) {
				return this.isPassedPlanePoints(aabb, (Point3D)beforePoint, (Point3D)afterPoint);
			}
		}
		return false;
	}
	
	private boolean isPassedPlanePoints(AxisAlignedBoundingBox aabb, 
						Point3D beforePoint, Point3D afterPoint) {
		////////////
		// X axis //
		////////////
		if(beforePoint.getX() <= aabb.minX) {
			if(afterPoint.getX() >= aabb.maxX) {
				if(beforePoint.getZ() >= aabb.minZ && beforePoint.getZ() <= aabb.maxZ) {
					if(afterPoint.getZ() >= aabb.minZ && afterPoint.getZ() <= aabb.maxZ) {
						if(beforePoint.getY() >= aabb.minY && beforePoint.getY() <= aabb.maxY) {
							if(afterPoint.getY() >= aabb.minY && afterPoint.getY() <= aabb.maxY) {
								return true;
							}
						}
					}
				}
			}
		}
		if(beforePoint.getX() >= aabb.maxX) {
			if(afterPoint.getX() <= aabb.minX) {
				if(beforePoint.getZ() >= aabb.minZ && beforePoint.getZ() <= aabb.maxZ) {
					if(afterPoint.getZ() >= aabb.minZ && afterPoint.getZ() <= aabb.maxZ) {
						if(beforePoint.getY() >= aabb.minY && beforePoint.getY() <= aabb.maxY) {
							if(afterPoint.getY() >= aabb.minY && afterPoint.getY() <= aabb.maxY) {
								return true;
							}
						}
					}
				}
			}
		}
		////////////
		// Z axis //
		////////////
		if(beforePoint.getZ() <= aabb.minZ) {
			if(afterPoint.getZ() >= aabb.maxZ) {
				if(beforePoint.getX() <= aabb.maxX && beforePoint.getX() >= aabb.minX) {
					if(afterPoint.getX() <= aabb.maxX && afterPoint.getX() >= aabb.minX) {
						if(beforePoint.getY() >= aabb.minY && beforePoint.getY() <= aabb.maxY) {
							if(afterPoint.getY() >= aabb.minY && afterPoint.getY() <= aabb.maxY) {
								return true;
							}
						}
					}
				}
			}
		}
		if(beforePoint.getZ() >= aabb.maxZ) {
			if(afterPoint.getZ() <= aabb.minZ) {
				if(beforePoint.getX() <= aabb.maxX && beforePoint.getX() >= aabb.minX) {
					if(afterPoint.getX() <= aabb.maxX && afterPoint.getX() >= aabb.minX) {
						if(beforePoint.getY() >= aabb.minY && beforePoint.getY() <= aabb.maxY) {
							if(afterPoint.getY() >= aabb.minY && afterPoint.getY() <= aabb.maxY) {
								return true;
							}
						}
					}
				}
			}
		}
		////////////
		// Y axis //
		////////////
		if(beforePoint.getY() <= aabb.minY) {
			if(afterPoint.getY() >= aabb.maxY) {
				if(beforePoint.getX() <= aabb.maxX && beforePoint.getX() >= aabb.minX) {
					if(afterPoint.getX() <= aabb.maxX && afterPoint.getX() >= aabb.minX) {
						if(beforePoint.getZ() >= aabb.minZ && beforePoint.getZ() <= aabb.maxZ) {
							if(afterPoint.getZ() >= aabb.minZ && afterPoint.getZ() <= aabb.maxZ) {
								return true;
							}
						}
					}
				}
			}
		}
		if(beforePoint.getY() >= aabb.maxY) {
			if(afterPoint.getY() <= aabb.minY) {
				if(beforePoint.getX() <= aabb.maxX && beforePoint.getX() >= aabb.minX) {
					if(afterPoint.getX() <= aabb.maxX && afterPoint.getX() >= aabb.minX) {
						if(beforePoint.getZ() >= aabb.minZ && beforePoint.getZ() <= aabb.maxZ) {
							if(afterPoint.getZ() >= aabb.minZ && afterPoint.getZ() <= aabb.maxZ) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
}
