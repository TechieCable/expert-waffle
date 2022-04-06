import java.awt.Point;

public class Map extends Picture {
	BoundaryLine[] lines;

	public Map(String fileName) {
		super(0, 0, fileName, 1);

	}

	public Map(String fileName, BoundaryLine[] lines) {
		super(0, 0, fileName, 1);
		this.lines = new BoundaryLine[lines.length];
		for (int i = 0; i < lines.length; i++) {
			this.lines[i] = lines[i];
		}
	}

	public boolean overBoundaries(int x, int y) {
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].overBoundaries(x, y))
				return true;
		}
		return false;
	}

}

class BoundaryLine {
	BoundaryPoint[] points;

	public BoundaryLine(BoundaryPoint[] points) {
		this.points = new BoundaryPoint[points.length];
		for (int i = 0; i < points.length; i++) {
			this.points[i] = points[i];
		}
	}

	public boolean overBoundaries(int x, int y) {
		for (int i = 0; i < points.length; i++) {
			if (points[i].overBoundary(x, y))
				return true;
		}
		return false;
	}
}

@SuppressWarnings("serial")
class BoundaryPoint extends Point {
	// indicates the direction an object cannot be in
	// 1: northeast
	// 2: southeast
	// 3: southwest
	// 4: northwest
	int direction;

	public BoundaryPoint(int x, int y, int direction) {
		super(x, y);
		this.direction = direction;
	}

	public boolean overBoundary(int x, int y) {
		switch (direction) {
		case 1:
			if (x > this.x && y < this.y) {
				return true;
			}
			break;
		case 2:
			if (x > this.x && y > this.y) {
				return true;
			}
			break;
		case 3:
			if (x < this.x && y > this.y) {
				return true;
			}
			break;
		case 4:
			if (x < this.x && y < this.y) {
				return true;
			}
			break;
		default:
			break;
		}
		return false;
	}
}