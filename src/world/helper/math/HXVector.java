package world.helper.math;

import java.text.DecimalFormat;

//public class HXVector {
//	
//	private double x;
//	private double y;
//	
//	// if this is not not null, the magnitude has been
//	// cached. x and y will be unit vectors in this case.
//	private Double length = null; 
//	
//	public HXVector(double xComponent, double yComponent, Double length) {
//		if (length == 0) {
//			x = 0; 
//			y = 0;
//		} else {
//			x = xComponent;
//			y = yComponent;
//		}
//		this.length = length;
//	}
//	
//	public HXVector(double xComponent, double yComponent) {
//		x = xComponent;
//		y = yComponent;
//	}
//	
//	/**
//	 * Create HXVector by subtracting a from b (b-a).
//	 * @param a
//	 * @param b
//	 */
//	public HXVector(HXVector a, HXVector b) {
//		x = b.x() - a.x();
//		y = b.y() - a.y();
//	}
//	
//	public HXVector() {
//		this (0, 0, 0.0);
//		length = 0.0;
//	}
//	
//	/**
//	 * Gets the opposite direction vector of the target vector and returns
//	 * this new vector. Will not update original.
//	 * @return - A new vector that is the opposite direction of the target vector.
//	 */
//	public HXVector opposite() {
//		if (length != null) {
//			return new HXVector(x()*-1, y()*-1, length);
//		}
//		return new HXVector(x()*-1, y()*-1);
//	}
//	/**
//	 * Calls the opposite() method but updates the target vector.
//	 */
//	public void flip() {
//		Double len = length; // Flipping a vector will not change length
//		x(x() * -1); // This will set the length to null
//		y(y() * -1); // This will set the length to null
//		length = len; // Restore length here
//		
//	}
//	
//	public HXVector perpendicular() {
//		if (length != null) {
//			return new HXVector(this.y(), -this.x(), length);
//		}
//		return new HXVector(this.y, -this.x);
//	}
//	
//	/**
//	 * Calculates the unit vector from the target vector. Will not update target,
//	 * but will return a new HXVector that is of length 1. If the target vector
//	 * is of length 0, the returned vector will have x, y, and length set as zero.
//	 * @return
//	 */
//	public HXVector normalized() {
//		if (x == 0 && y == 0) {
//			return new HXVector(0.0,0.0,0.0);
//		}
//		if (length == null) {
//			length = Math.sqrt(this.x*this.x + this.y*this.y);
//			return new HXVector(this.x / length, this.y / length, 1.00);
//		}
//		return new HXVector(this.x, this.y, 1.00);
//	}
//	
//	/**
//	 * 
//	 */
//	public void zero() {
//		x = 0;
//		y = 0;
//		length = 0.0;
//	}
//	
//	/**
//	 * Updates the target vetor after adding the argument vector to it.
//	 * @param vec
//	 */
//	public void add(HXVector vec) {
//		x(x() + vec.x());
//		y(y() + vec.y());
//	}
//	/**
//	 * Returns a new vector that is the result of adding the target and argument vectors.
//	 * @param vec - The vector to add to the target.
//	 * @return The result of the vector addition.
//	 */
//	public HXVector addedTo(HXVector vec) {
//		return new HXVector(this.x() + vec.x(), this.y() + vec.y());
//	}
//	
//	/**
//	 * Updates the target vector after subtracting the argument vector from it.
//	 * @param vec
//	 */
//	public void subtract(HXVector vec) {
//		System.out.println("  Calling subtract().");
//		add(vec.opposite());
//	}
//	/**
//	 * Updates the target vector with the subtracting x and y components. This will not force
//	 * a magnitude recalculation, so the the components will no longer be a unit vector.
//	 * @param xVal - Translation on x axis.
//	 * @param yVal - Translation on y axis.
//	 */
//	public void subtract(double xVal, double yVal) {
//		x(x() - xVal);
//		y(y() - yVal);
//	}
//	/**
//	 * Returns a new vector that is the target vector subtracted by the given components. This will
//	 * not update the target vector and the returned vector will not have its length calculated.
//	 * @param xVal - Translation on x axis.
//	 * @param yVal - Translation on y axis.
//	 * @return A new vector which is the target vector subtracted by the given components.
//	 */
//	public HXVector subtractedBy(double xVal, double yVal) {
//		return new HXVector(x() - xVal, y() - yVal);
//	}
//	/**
//	 * Returns a new vector that is the target vector subtracted by the given vector. This will
//	 * not update the target vector and the returned vector will not have its length calculated.
//	 * @param vec - Subtract the target vector by this vector.
//	 * @return A new vector which is the target vector subtracted by the given vector.
//	 */
//	public HXVector subtractedBy(HXVector vec) {
//		return new HXVector(x() - vec.x(), y() - vec.y());
//	}
//	
//	/**
//	 * Reduce's target vectors magnitude
//	 * @param vec
//	 */
//	public void reduceBy(double amount) {
//		if (length == null) {
//			length = getMagnitude();
//		}
//		length = Math.max(0.00, length - amount);
//		if (length == 0) {
//			x = 0;
//			y = 0;
//		}
//	}
// 
//	/**
//	 * HXVector 'a' projected onto HXVector 'b'.
//	 * @param a - An HXVector.
//	 * @param b - An HXVector.
//	 * @return The resultant vector of a projected onto b.
//	 */
//	public static HXVector proj(HXVector a, HXVector b) {
//		double calc = dot(a, b) / (b.x() * b.x() + b.y() * b.y());
//		double xComponent = b.x() * calc;
//		double yComponent = b.y() * calc;
//		
//		return new HXVector(xComponent, yComponent);
//	}
//	
//	public static double dot(HXVector a, HXVector b) {
//		return a.x() * b.x() + a.y() * b.y();
//	}
//	
//	/**
//	 * Will calculate the magnitude of the target vector and return the value. The length
//	 * will be stored in the vector and the components will be normalized so future calls
//	 * will not require another square root calculation. If both components are zero, the
//	 * length will be zero and the components will remain as zero.
//	 * @return - Magnitude of the vector.
//	 */
//	public Double getMagnitude() {
//		if (x == 0 && y == 0) {
//			return length = 0.0;
//		}
//		if (length == null) {
//			length = Math.sqrt(this.x * this.x + this.y * this.y);
//			this.x /= length;
//			this.y /= length;
//		}
//		return length;
//	}
//	public void setMagnitude(double magnitude) {
//		if (x == 0 && y == 0) {
//			return; // There is an error if trying to set the length of a zero vector.
//		}
//		if (length == null) { // Normalize the components if not already done.
//			this.x /= magnitude;
//			this.y /= magnitude;
//		}
//		length = magnitude;
//	}
//	/**
//	 * Won't calculate the magnitude and simply return null if it hasn't been calculated yet or
//	 * will return the value of the magnitude if it has been previously calculated.
//	 * @return - Null if magnitude of target vector had not been previously calculated or
//	 * a double value representing the vector's magnitude.
//	 */
//	public Double getMagnitude_lazy() {
//		return length;
//	}
//	
//	public String toString() {
//		
//		DecimalFormat df = new DecimalFormat();
//		df.setMaximumFractionDigits(2);
//		
//        return "V(" + df.format(x()) +", " + df.format(y()) + ")";
//	}
//	
//	public double x() {
//		if (length == null) {
//			return x;
//		}
//		return x * length;
//	}
//	public void x(double xVal) {
//		this.x = xVal;
//		length = null;
//	}
//	public double y() {
//		if (length == null) {
//			return y;
//		}
//		return y * length;
//	}
//	public void y(double yVal) {
//		this.y = yVal;
//		length = null;
//	}
//}

public class HXVector {
	
	private double x;
	private double y;
	
	public HXVector(double xComponent, double yComponent) {
		x = xComponent;
		y = yComponent;
	}
	
	/**
	 * Create HXVector by subtracting a from b (b-a).
	 * @param a
	 * @param b
	 */
	public HXVector(HXVector a, HXVector b) {
		x = b.x() - a.x();
		y = b.y() - a.y();
	}
	
	public HXVector() {
		this (0, 0);
	}
	
	/**
	 * Gets the opposite direction vector of the target vector and returns
	 * this new vector. Will not update original.
	 * @return - A new vector that is the opposite direction of the target vector.
	 */
	public HXVector opposite() {
		return new HXVector(x()*-1, y()*-1);
	}
	/**
	 * Calls the opposite() method but updates the target vector.
	 */
	public void flip() {
		x(x() * -1); // This will set the length to null
		y(y() * -1); // This will set the length to null
		
	}
	
	public HXVector perpendicular() {
		return new HXVector(this.y, -this.x);
	}
	
	/**
	 * Calculates the unit vector from the target vector. Will not update target,
	 * but will return a new HXVector that is of length 1. If the target vector
	 * is of length 0, the returned vector will have x, y, and length set as zero.
	 * @return
	 */
	public HXVector normalized() {
		if (x == 0 && y == 0) {
			return new HXVector(0,0);
		}
		return new HXVector(this.x / getMagnitude(), this.y  / getMagnitude());
	}
	
	/**
	 * 
	 */
	public void zero() {
		x = 0;
		y = 0;
	}
	
	/**
	 * Updates the target vetor after adding the argument vector to it.
	 * @param vec
	 */
	public void add(HXVector vec) {
		x(x() + vec.x());
		y(y() + vec.y());
	}
	/**
	 * Returns a new vector that is the result of adding the target and argument vectors.
	 * @param vec - The vector to add to the target.
	 * @return The result of the vector addition.
	 */
	public HXVector addedTo(HXVector vec) {
		return new HXVector(this.x() + vec.x(), this.y() + vec.y());
	}
	
	/**
	 * Updates the target vector after subtracting the argument vector from it.
	 * @param vec
	 */
	public void subtract(HXVector vec) {
		System.out.println("  Calling subtract().");
		add(vec.opposite());
	}
	/**
	 * Updates the target vector with the subtracting x and y components. This will not force
	 * a magnitude recalculation, so the the components will no longer be a unit vector.
	 * @param xVal - Translation on x axis.
	 * @param yVal - Translation on y axis.
	 */
	public void subtract(double xVal, double yVal) {
		x(x() - xVal);
		y(y() - yVal);
	}
	/**
	 * Returns a new vector that is the target vector subtracted by the given components. This will
	 * not update the target vector and the returned vector will not have its length calculated.
	 * @param xVal - Translation on x axis.
	 * @param yVal - Translation on y axis.
	 * @return A new vector which is the target vector subtracted by the given components.
	 */
	public HXVector subtractedBy(double xVal, double yVal) {
		return new HXVector(x() - xVal, y() - yVal);
	}
	/**
	 * Returns a new vector that is the target vector subtracted by the given vector. This will
	 * not update the target vector and the returned vector will not have its length calculated.
	 * @param vec - Subtract the target vector by this vector.
	 * @return A new vector which is the target vector subtracted by the given vector.
	 */
	public HXVector subtractedBy(HXVector vec) {
		return new HXVector(x() - vec.x(), y() - vec.y());
	}
	
	/**
	 * Reduce's target vectors magnitude
	 * @param vec
	 */
	public void reduceBy(double amount) {
		double length = getMagnitude();
		length = Math.max(0.00, length - amount);
		setMagnitude(length);
	}
 
	/**
	 * HXVector 'a' projected onto HXVector 'b'.
	 * @param a - An HXVector.
	 * @param b - An HXVector.
	 * @return The resultant vector of a projected onto b.
	 */
	public static HXVector proj(HXVector a, HXVector b) {
		double calc = dot(a, b) / (b.x() * b.x() + b.y() * b.y());
		double xComponent = b.x() * calc;
		double yComponent = b.y() * calc;
		
		return new HXVector(xComponent, yComponent);
	}
	
	public static double dot(HXVector a, HXVector b) {
		return a.x() * b.x() + a.y() * b.y();
	}
	
	/**
	 * Will calculate the magnitude of the target vector and return the value. The length
	 * will be stored in the vector and the components will be normalized so future calls
	 * will not require another square root calculation. If both components are zero, the
	 * length will be zero and the components will remain as zero.
	 * @return - Magnitude of the vector.
	 */
	public Double getMagnitude() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}
	public void setMagnitude(double magnitude) {
		if (x == 0 && y == 0) {
			return; // There is an error if trying to set the length of a zero vector.
		}
		if (magnitude == 0) {
			x = 0;
			y = 0;
		} else {
			double angle = Math.atan2(y, x);
			x = magnitude * Math.cos(angle);
			y = magnitude * Math.sin(angle);
		}
	}
	/**
	 * Won't calculate the magnitude and simply return null if it hasn't been calculated yet or
	 * will return the value of the magnitude if it has been previously calculated.
	 * @return - Null if magnitude of target vector had not been previously calculated or
	 * a double value representing the vector's magnitude.
	 */
//	public Double getMagnitude_lazy() {
//		return length;
//	}
	
	public String toString() {
		
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
        return "V(" + df.format(x()) +", " + df.format(y()) + ")";
	}
	
	public double x() {
		return x;
	}
	public void x(double xVal) {
		this.x = xVal;
	}
	public double y() {
		return y;
	}
	public void y(double yVal) {
		this.y = yVal;
	}
}
