import java.awt.Color;
import java.awt.Graphics;

public class Position {
	int x, y;

	public Position() {
		this(0, 0);
	}

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawRect(x - 5, y - 5, 10, 10);
	}

	public double angle(int x, int y) {
		return Math.atan2(x - this.x, this.y - y) + Math.PI / 2;
	}

	public double angle(Position p) {
		return angle(p.x, p.y);
	}

	public int distanceFrom(int x, int y) {
		return (int) Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2));
	}

	public String toString() {
		return "Position [x=" + x + ", y=" + y + "]";
	}
}
