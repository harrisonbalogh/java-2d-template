package world.helper.math;

import java.math.BigDecimal;
import world.entities.HXEntity;
import world.helper.math.HXDoublePoint;
import world.helper.math.HXSegment;
import world.helper.math.HXVector;

public class HXCollider {
	
	private double mass; // Momentum NYI
	
	private HXEntity parent;
	
	// Collider vertices
	private HXVector[] vertices;
	// Average center of verties
	private HXVector CENTER = new HXVector(0, 0); // Polygon is centered on (0,0)
	// Bounding circle radius
	private double boundingRadius = 0;
	
	private HXVector testMtv;
	
//	private boolean intersecting = false;
	
//	private double   netForceMagnitude = 0;
//	private HXVector netForceDirection = new HXVector(0,0);
	
//	private HXSegment mtvLine = new HXSegment(new HXVector(), new HXVector());
	
	/** 
	 * A polygonal physics collider attached to an HXEntity. Used for detecting
	 * and translating colliding entities.
	 */
	public HXCollider(HXVector[] vertices, HXEntity parent) {
		this.parent = parent;
		
		this.vertices = new HXVector[vertices.length];
		this.vertices = vertices;
		
		// Center polygon on (0,0) for relative calculations
		if (vertices.length != 0) {
			// Calculate center
			double midX = 0;
			double midY = 0;
			for (int v = 0; v < vertices.length; v++) {
				midX += vertices[v].x();
				midY += vertices[v].y();
			}
			midX /= vertices.length;
			midY /= vertices.length;
			for (int v = 0; v < vertices.length; v++) {
				vertices[v].subtract(midX, midY);
			}
		
			// Calculate smallest circle that encompasses all vertices
			double farthestDistance = 0;
			for (int v = 0; v < vertices.length; v++) {
				double distSqrd = Math.pow(vertices[v].x() - midX, 2) + Math.pow(vertices[v].y() - midY, 2);
				if (distSqrd > farthestDistance) {
					farthestDistance = distSqrd;
				}
			}
			this.boundingRadius = Math.sqrt(farthestDistance);
		}
	}
	
	// Mark: TRANSFORMATIONS
	// =====================
	
	/**
	 * Translate the collider.
	 * <p>
	 * Applies a translation to the vertices of the collider.
	 * @param x - Amount in the x direction to translate collider vertices.
	 * @param y - Amount in the y direction to translate collider vertices.
	 */
//	public void translateBy(double x, double y) {
//		center.x += x;
//		center.y += y;
//		for (int v = 0; v < vertices.length; v++) {
//			vertices[v].x += x;
//			vertices[v].y += y;
//		}
//	}
	/**
	 * Rotate the collider.
	 * <p>
	 * Applies a rotation to the vertices of the collider.
	 * @param rads - The amount in radians to rotate the collider.
	 * @param anchor - The point around which to rotate the collider.
	 */
	public void rotateBy(double rads, HXVector anchor) {
		for (int v = 0; v < vertices.length; v++) {

			// The following has been converted to BigDecimal to avoid (floating point) rounding errors
			// vertices[v].x = anchor.x + (vertices[v].x - anchor.x) * Math.cos(rads) - (vertices[v].y - anchor.y) * Math.sin(rads);
			// vertices[v].y = anchor.y + (vertices[v].x - anchor.x) * Math.sin(rads) + (vertices[v].y - anchor.y) * Math.cos(rads);
			
			// Yeah... that doesn't correct rounding errors. Any time you leave the space of rational numbers, and try to, say, divide
			// a number, your double may not be able to hold all the siginificant digits of that real number - doing this repeatedly
			// will grow the inaccuracy until, in the case of this application, the collider visibly is not a square anymore.
			
			BigDecimal t0 = BigDecimal.valueOf(Math.cos(rads)).multiply(BigDecimal.valueOf(vertices[v].x() - anchor.x()));
			BigDecimal t1 = BigDecimal.valueOf(Math.sin(rads)).multiply(BigDecimal.valueOf(vertices[v].y() - anchor.y()));
			BigDecimal t2 = BigDecimal.valueOf(Math.sin(rads)).multiply(BigDecimal.valueOf(vertices[v].x() - anchor.x()));
			BigDecimal t3 = BigDecimal.valueOf(Math.cos(rads)).multiply(BigDecimal.valueOf(vertices[v].y() - anchor.y()));
			
			vertices[v].x(t0.subtract(t1).add(BigDecimal.valueOf(anchor.x())).doubleValue());
			vertices[v].y(t2.add(t3).add(BigDecimal.valueOf(anchor.y())).doubleValue());
		}
	}
	/**
	 * Calls rotateBy(double rads, HXVector anchor) but on the polygon's center.
	 * @param rads - The amount in radians to rotate the collider.
	 */
	public void rotateBy(double rads) {
		this.rotateBy(rads, CENTER);
	}
	/**
	 * Scale the collider
	 * <p>
	 * Applies a scaling factor to the vertices of the collider.
	 * @param scale - Amount to increase or decrease collider shape by.
	 * @param anchor - The center point of the shape.
	 */
	public void scaleBy(double scale, HXVector anchor) {
		for (int v = 0; v < vertices.length; v++) {
			vertices[v].x(anchor.x() + (vertices[v].x() - anchor.x()) * scale);
			vertices[v].y(anchor.y() + (vertices[v].y() - anchor.y()) * scale);
		}
	}
	
	// Mark: COLLISION TESTING
	// =======================
	/**
	 * 
	 * Calculate overlap between target collider and passed collider.
	 * <p>
	 * This method tests for distance between centers and if necessary 
	 * uses the Separating Axis Theorem method for determing precise
	 * overlap. Will return the minimum translation vector if overlap
	 * is found.
	 * @param peer - The other HXCollider to test for overlap against.
	 * @return 
	 * - Will return a vector representing the MTV (minimum
	 * translation vector) to displace the shapes away from each other.
	 * <br>
	 * - Returns <b>null</b> if shapes do not overlap.
	 */
	public HXVector doesOverlap(HXCollider peer) {
		
//		// Find center points for preliminary check of circle intersections
//		double aMidX = this.getCenter().x;
//		double aMidY = this.getCenter().y;
//		double bMidX = peer.getCenter().x;
//		double bMidY = peer.getCenter().y;
//		// Avoid square roots so distance stays as distance squared.
//		double centersDistanceSqrd = Math.pow(aMidX - bMidX, 2) + Math.pow(aMidY - bMidY, 2);
//		double aRadius = this.getBoundingRadius() + 10; // Add arbitrary buffer to avoid rounding errors
//		double bRadius = peer.getBoundingRadius() + 10;
//		// (D > aR + bR)^2 == (D^2 > aR^2 + aR*bR + bR^2)
//		if (centersDistanceSqrd > Math.pow(aRadius, 2) + aRadius * bRadius + Math.pow(bRadius, 2)) {
//			return null;
//		}
		
		// === Separating Axis Theorem ===
		// If able to get past preliminary circle check, use precision check.
		// Apply SAT:
		
//		// Apply velocity (non-angular) of parent object to detect if the shape *will* overlap next update
//		HXVector[] thisVertices = new HXVector[this.getVertices().length];
//		for (int v = 0; v < this.getVertices().length; v++) {
//			thisVertices[v] = this.getVertices()[v].translate(this.getParent().getxVel(), this.getParent().getyVel());
//		}

		// Translate peer vertices into target vector's coordinate space
		
		HXVector[] peerVertices = peer.getVerticesTranslatedBy(peer.getRealCenter().subtractedBy(this.getRealCenter()));
				
		// Using axes on this HXCollider, test both this/peer vertices
		HXVector mtv = testVerticesOnAxes(this.getVertices(), peerVertices);
		if (mtv != null) {
			// If still colliding, use axes on peer HXCollider, test both this/peer vertices
			mtv = testVerticesOnAxes(peerVertices, this.getVertices());
			if (mtv == null) {
				testMtv = null;
				return null;	
			}
		} else {
			testMtv = null;
			return null;
		}
		
		// Parallel lines fix: 
		if (HXVector.dot(peer.getRealCenter().subtractedBy(this.getRealCenter()), mtv.normalized()) < 0)
			mtv.flip();
		
		// If all four axes had projection overlap then the objects overlap
		if (mtv.x() != 0 || mtv.y() != 0) {
			System.out.println("Overlap: " + mtv.x() + ", " + mtv.y());
			testMtv = mtv;
			return mtv;
		}
		testMtv = null;
		return null; // Overlap is true, but its a zero vector (so they are touching)
	}
	
	/**
	 * Test for overlap from two shapes on one axis set.
	 * <p>
	 * The axes iterated through are from the first array of vertices supplied.
	 * Vertices from both arrays are projected onto the axes created by the first
	 * array of vertices. Will update mtvDirection and mtvMagnitude with the smallest
	 * values of overlap calculated.
	 * @param thisAxisVertices - The first shape and source of axes.
	 * @param peerAxisVertices - the second shape.
	 */
	private static HXVector testVerticesOnAxes(HXVector[] thisAxisVertices, HXVector[] peerAxisVertices) {
		HXVector mtv = null;
		// Number of axes == number of vertices
		for (int a = 0; a < thisAxisVertices.length; a++) {
			// Generate axis
			HXVector axis = new HXVector(thisAxisVertices[(a+1)%thisAxisVertices.length], thisAxisVertices[a]).perpendicular().normalized();
			
			HXVector minMaxThis = minMaxForAxis(thisAxisVertices, axis);
			HXVector minMaxPeer = minMaxForAxis(peerAxisVertices, axis);
			
			double overlap = Math.min((minMaxThis.y() - minMaxPeer.x()),(minMaxPeer.y() - minMaxThis.x()));
			
			if (minMaxPeer.x() > minMaxThis.y() || minMaxPeer.y() < minMaxThis.x()) {
				return null; // No overlap = break out of SAT check, no collision present
			} else if (mtv == null || overlap < mtv.getMagnitude()) {
				mtv = new HXVector(axis.x() * overlap, axis.y() * overlap);
//				mtv = new HXVector(axis.x(), axis.y(), Math.abs(overlap));
			}
		}
		return mtv;
	}
	
	/**
	 * Calculate min/max dot products.
	 * <p>
	 * Calculates the minimum and maxmimum dot product on the axis with
	 * the given vertices. Note return syntax.
	 * @param verts - The vectors to project onto the axis.
	 * @param axis - The axis to be projected onto.
	 * @return - Will return a vector whose x value is the minimum and
	 * whose y value is the maximum.
	 */
	private static HXVector minMaxForAxis(HXVector[] verts, HXVector axis) {
		// Dot product all corners with the axis then select max/min for object
		double min = HXVector.dot(verts[0], axis);
		double max = min;
		// Find max/min dot of this object.
		for (int c = 1; c < verts.length; c++) {
			double value = HXVector.dot(verts[c], axis);
			if (value < min) {
				min = value;
			}
			if (value > max) {
				max = value;
			}
		}
		return new HXVector(min, max);
	}
	
	/**
	 * Checks if point is inside polygon. Polygon must have at least 3 corners,
	 * and can be convex or concave.
	 * @param x - X coordinate of point to check.
	 * @param y - Y coordinate of point to check.
	 * @param corners - The corners of the polygon.
	 * @return True if the point is located within the bounds formed by the polygon.
	 */
	public Boolean contains(int x, int y) {
		return contains(new HXDoublePoint(x - parent.getxPos(), y - parent.getyPos()));
	}
	
	/**
	 * Checks if point is inside polygon. Polygon must have at least 3 corners,
	 * and can be convex or concave(?).
	 * @param p - The point to check.
	 * @param corners - The corners of the polygon.
	 * @return True if the point is located within the bounds formed by the polygon.
	 */
	private Boolean contains(HXDoublePoint p) {
		if (vertices.length < 3) { return false; }
		
		HXSegment pInfinity = new HXSegment(p, new HXDoublePoint(p.x + 9999, p.y));
		int count = 0;
		
		for (int v = 0; v < this.vertices.length; v++) {
			HXSegment side = new HXSegment(this.vertices[v], this.vertices[(v+1)%vertices.length]);
			if (side.intersects(pInfinity) == 1) {
				count++;
			}
		}
		return (count%2 == 1);
	}
	
//	public void intersectApplyMTV(HXVector mtv) {
//		
//		mtvLine.a.x = center.x;
//		mtvLine.a.y = center.y;
//		mtvLine.b.x = center.x + mtv.x;
//		mtvLine.b.y = center.y + mtv.y;
//		
//		if (!interacting && !statis) {
////			this.xPos -= mtv.x;
////			this.yPos -= mtv.y;
//			
//			translateBy(-mtv.x, -mtv.y);
//		}
//	}
	
	
	// Mark: GETTERS & SETTERS
	// =======================
	public HXVector[] getVertices() {
		return this.vertices;
	}
	public HXVector[] getVerticesTranslatedBy(double x, double y) {
		HXVector[] translatedVertices = new HXVector[this.vertices.length];
		for (int v = 0; v < this.vertices.length; v++) {
			translatedVertices[v] = this.vertices[v].subtractedBy(x, y);
		}
		return translatedVertices;
	}
	public HXVector[] getVerticesTranslatedBy(HXVector vec) {
		return getVerticesTranslatedBy(vec.x(), vec.y());
	}
	public HXVector getRealCenter() {
		return new HXVector(CENTER.x() + parent.getxPos(), CENTER.y() + parent.getyPos());
	}
	public double getBoundingRadius() {
		return this.boundingRadius;
	}
	public HXVector getTestMtv() {
		return testMtv;
	}
//	public HXEntity getParent() {
//		return this.parent;
//	}

}
