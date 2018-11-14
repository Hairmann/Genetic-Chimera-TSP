public class Point {
	
	private double x, y;
	private int numName;
	
	//Constructors
	public Point(int numName) {
		this.numName = numName;
	}
	
	public Point() {
		
	}
	
	//Setters
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void setNumName(int numName) {
		this.numName = numName;
	}
	
	//Getters
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public int getNumName() {
		return this.numName;
	}
}