import java.awt.Point;

@SuppressWarnings("serial")
public class Position extends Point {
	public Position() {
		super();
	}

	public Position(int x, int y) {
		super(x, y);
	}

	public Position(Point p) {
		super(p.x, p.y);
	}

	public double angle(int x, int y) {
		return Math.atan2(x - this.x, this.y - y) + Math.PI / 2;
	}

	public double angle(Position p) {
		return angle(p.x, p.y);
	}

	public double distanceFrom(int x, int y) {
		return Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));
	}

	public double distanceFrom(Position p) {
		return distanceFrom(p.x, p.y);
	}

	public String toString() {
		return "Position [x=" + x + ", y=" + y + "]";
	}
}
