package world.helper.math;

public class HXDoublePoint {

	public double x;
	public double y;
	
	public HXDoublePoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public HXDoublePoint() {
		this(0, 0);
	}
	
	public String toString() {
        return "HXDoublePoint(x: " + x +", y: " + y + ")";
    }
	
}
