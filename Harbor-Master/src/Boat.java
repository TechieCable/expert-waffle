import java.awt.Graphics;

public class Boat extends Picture {
	int speed;
	DataStructure moves = new Queue();
	Position target;

	public Boat(int x, int y, String fileName) {
		super(x, y, fileName, 1);
		speed = 2;
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
		if (moves.size() > 0) {
			Position nextMove = moves.peek();
			if (nextMove != null) {
				this.rotateTo(nextMove.angle(x, y));
				moves.pop();
			}
		}

//		System.out.println("angle " + angle * 180 / Math.PI);

//		x += speed * Math.abs(Math.cos(angle)) * (angle > 0 ? -1 : 1);
//		y += speed * Math.abs(Math.sin(angle)) * (Math.abs(angle) > Math.PI / 2 ? -1 : 1);
		x += speed * Math.cos(angle);
		y += speed * Math.sin(angle);
	}

}

class PNode {
	PNode next;
	Position data;

	public PNode() {

	}
}
