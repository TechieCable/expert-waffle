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

	public double angle(Position p) {
		return Math.atan2(x - p.x, y - p.y);
	}

	public String toString() {
		return "Position [x=" + x + ", y=" + y + "]";
	}
}
