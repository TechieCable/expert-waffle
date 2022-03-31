import java.awt.Graphics;

public class Boat extends Picture {
	double dy, dx;
	Queue moves = new Queue();

	public Boat(int x, int y, String fileName) {
		super(x, y, fileName, 1);
	}

	public void paint(Graphics g) {
		super.paint(g);
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
