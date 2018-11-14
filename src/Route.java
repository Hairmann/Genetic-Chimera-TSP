public class Route {
	
	private Point[] points;
	private double fitness = 0;
	
	//Constructor
	public Route(Point[] points) {
		this.points = points;
		setFitness();
	}
	
	public Route(){
		
	}
	
	
	//Methods
	//setters
	public void setFitness() {
		this.fitness = 0;
		for (int i = 1; i < points.length; i++) {
			this.fitness = this.fitness + getDistance(points[i], points[i - 1]);
		}
	}
	
	public void initEmptyRoute(int n) {
		this.points = new Point[n + 1];
	}
	
	public void setPointInRoute(int i, Point pt) {
		this.points[i] = pt;
	}
	
	//getters
	public double getFitness() {
		return this.fitness;
	}
	
	public int getLength() {
		return this.points.length - 1;
	}
	
	public Point getPoint(int i) {
		return this.points[i];
	}
	
	//Special
	public static double getDistance(Point pt1, Point pt2) {
		return Math.sqrt( Math.pow( pt1.getX() - pt2.getX(), 2 ) + Math.pow( pt1.getY() - pt2.getY(), 2 ));	
	}
	
	public void printSelf() {
		for (int i = 0; i < this.points.length; i++) {
			System.out.print(this.points[i].getNumName() + "  ");
			if (i == this.points.length - 1) System.out.println("  fitness: " + String.format("%.2f", this.fitness));
		}
	}
	
	public void closeRoute() {
		this.points[points.length - 1] = this.points[0];
	}
}