import java.awt.Color;
import java.awt.Graphics;

public class Position {
	int x, y;

	public Position() {
		this(0, 0);
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		// TODO: print location
	}

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public double angle(Position p) {
		return Math.atan2(x - p.x, y - p.y);
	}
}
