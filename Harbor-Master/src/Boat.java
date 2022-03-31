import java.awt.Graphics;

public class Boat extends Picture {
	double dy, dx;
	Queue moves;

	public Boat(int x, int y, String fileName) {
		super(x, y, fileName, 1);
		moves = new Queue();
	}

	public void paint(Graphics g) {
		super.paint(g);
		for (Position p : moves.in.data) {
			p.paint(g);
		}
	}

	public void addMove(Position p) {
		moves.push(p);
	}

	public void clearMoves() {
		moves.clear();
	}

	public void move() {
		Position nextMove = moves.pop();
		if (nextMove != null) {

		}
	}

}
