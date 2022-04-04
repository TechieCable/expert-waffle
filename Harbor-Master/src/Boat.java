import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Boat extends RotatingPicture {
	int speed;
	double da;
	ArrayList<Position> moves = new ArrayList<Position>();
	Position target;
	int cx, cy;

	public Boat(int x, int y, String fileName) {
		super(x, y, fileName, 1);
		speed = 4;
		da = Math.PI / 72 * speed;
		target = new Position(x, y);
	}

	public void paint(Graphics g) {
		cx = (int) (Math.cos(Math.PI * 2 - angle) * (width / 2) - Math.sin(Math.PI * 2 - angle) * (height / 2) + x);
		cy = (int) (Math.cos(Math.PI * 2 - angle) * (width / 2) + Math.sin(Math.PI * 2 - angle) * (height / 2) + y);

		speed = (int) (264 / height);
		da = Math.PI / 72 * speed;

		g.setColor(Color.WHITE);

//		g.drawRect(x - 5, y - 5, 10, 10);

		for (int i = 0; i < moves.size() - 1; i++) {
			g.drawLine(moves.get(i).x, moves.get(i).y, moves.get(i + 1).x, moves.get(i + 1).y);
		}

		super.paint(g);

		g.drawRect(cx - 5, cy - 5, 10, 10);
	}

	public void addMove(Position p) {
		moves.add(p);
	}

	public void clearMoves() {
		moves.clear();
		target = new Position(x, y);
	}

	public void move() {
		this.angle %= Math.PI * 2;
		if (moves.size() > 0) {
			if (target.distanceFrom(x, y) < 20) {
				try {
					target = moves.remove(0);
				} catch (Exception e) {
				}
			}
			this.rotateTo(target.angle(x, y));
		}

		x += speed * Math.cos(angle);
		y += speed * Math.sin(angle);

		if (moves.size() == 0) {
			if (x < 20 || x > Driver.screenW - 20) {
				addMove(new Position(Driver.screenW / 2, y));
			} else if (y < 20 || y > Driver.screenH - 40) {
				addMove(new Position(x, Driver.screenH / 2));
			}
		}

	}

	public void rotateTo(double angle) {
		double diff = absMin(absMin(this.angle - angle, Math.PI * 2 - angle + this.angle),
				Math.PI * 2 + angle - this.angle);

//		System.out.print("angle=" + (int) (this.angle * 180 / Math.PI));
//		System.out.print(", tangle=" + (int) (angle * 180 / Math.PI));
//		System.out.println(", diff=" + (int) (diff * 180 / Math.PI));

		if (Math.abs(diff) <= this.da) {
			return;
		} else if (diff > 0) {
			this.angle -= da;
		} else {
			this.angle += da;
		}
	}

	public static double absMin(double a, double b) {
		double min = Math.min(Math.abs(a), Math.abs(b));
		if (Math.abs(a) == min) {
			return min * (Math.abs(a) / a);
		}
		if (Math.abs(b) == min) {
			return min * Math.abs(b) / b;
		}
		return min;
	}

}
