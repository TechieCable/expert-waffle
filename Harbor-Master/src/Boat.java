import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Boat extends RotatingPicture {
	int speed;
	double da;
	ArrayList<Position> moves = new ArrayList<Position>();
	Position target;
	int checkTime;
	boolean docked;

	public Boat() {
		this((int) (Math.random() * (Driver.screenW - 20 - 20 + 1)) + 20,
				(int) (Math.random() * (Driver.screenH - 20 - 20 + 1)) + 20, "boat1-0.png");
	}

	public Boat(int x, int y, String fileName) {
		super(x, y, fileName, 1.4);
		speed = 4;
		da = Math.PI / 72 * speed;
		target = new Position(ax(), ay());
		docked = false;
	}

	public int ax() {
		return (int) (x + width / 2);
	}

	public int ay() {
		return (int) (y + height / 2);
	}

	/**
	 * 
	 * @return direction
	 */
	public int facing() {
		// 1: northeast
		// 2: northwest
		// 3: southwest
		// 4: southeast

		if (angle > Math.PI) { // north
			if (angle < Math.PI * 3 / 2) { // northwest
				return 2;
			} else { // northeast
				return 1;
			}
		} else { // south
			if (angle < Math.PI / 2) { // southeast
				return 4;
			} else { // southwest
				return 3;
			}
		}
	}

	public boolean clicked(int x, int y) {
		return (x > ax() - this.width && y > ay() - this.width && x < ax() + this.width && y < ay() + this.width);
	}

	public void paint(Graphics g) {
		speed = (int) ((264 * scaleSize) / height);
		da = Math.PI / 72 * speed;

		g.setColor(Color.WHITE);

		for (int i = 0; i < moves.size() - 1; i++) {
			// g.drawRect(moves.get(i).x - 5, moves.get(i).y - 5, 10, 10);
			g.drawLine(moves.get(i).x, moves.get(i).y, moves.get(i + 1).x, moves.get(i + 1).y);
		}

		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(angle - Math.PI / 2, x + width / 2, y + height / 2);
		// rotated things here, using g2 (not g)
		g2.setColor(new Color(180, 240, 255));
		g2.fillOval(ax() + 1, ay() - 60, 10, 10);
		g2.fillOval(ax()-1, ay() - 60, 10, 10);
		g2.fillOval(ax() - 3, ay() - 60, 10, 10);
		g2.fillOval(ax() - 5, ay() - 60, 10, 10);
		g2.fillOval(ax() - 7, ay() - 60, 10, 10);
		
		g2.fillOval(ax() - 1, ay() - 62, 8, 8);
		g2.fillOval(ax() - 2, ay() - 62, 8, 8);
		g2.fillOval(ax() - 3, ay() - 62, 8, 8);
		g2.fillOval(ax() - 4, ay() - 62, 8, 8);
		g2.fillOval(ax() - 5, ay() - 62, 8, 8);
		
		g2.fillOval(ax() - 1, ay() - 64, 6, 6);
		g2.fillOval(ax() - 2, ay() - 64, 6, 6);
		g2.fillOval(ax() - 3, ay() - 64, 6, 6);
		g2.fillOval(ax() - 4, ay() - 64, 6, 6);
		g2.fillOval(ax() - 5, ay() - 64, 6, 6);
		
		g2.fillOval(ax() - 3, ay() - 65, 6, 6);
		g2.fillOval(ax() - 3, ay() - 66, 6, 6);
		g2.fillOval(ax() - 3, ay() - 67, 6, 6);
		g2.fillOval(ax() - 3, ay() - 68, 6, 6);
		
		g2.fillOval(ax() - 3, ay() - 69, 4, 4);
		g2.fillOval(ax() - 3, ay() - 70, 4, 4);
		g2.fillOval(ax() - 3, ay() - 71, 4, 4);
		g2.fillOval(ax() - 3, ay() - 72, 4, 4);
		/*g2.fillOval(ax(), ay() - 60, 10, 10);
		g2.fillOval(ax(), ay() - 62, 10, 10);
		g2.fillOval(ax(), ay() - 64, 9, 9);
		g2.fillOval(ax(), ay() - 66, 8, 8);
		g2.fillOval(ax(), ay() - 68, 6, 6);
		g2.fillOval(ax(), ay() - 70, 3, 3);
		g2.fillOval(ax(), ay() - 72, 2, 2);
*/
		g2.rotate(-(angle - Math.PI / 2), x + width / 2, y + height / 2);
		g.setColor(Color.RED);
//		g.drawRect((int) (ax() - this.width), (int) (ay() - this.width), (int) (this.width * 2),
//				(int) (this.width * 2));
	}

	public void addMove(Position p) {
		moves.add(p);
		docked = false;
		checkTime = 200;
	}

	public void clearMoves() {
		moves.clear();
		target = new Position(ax(), ay());
	}

	public void move() {
		if (docked)
			return;
		if (checkTime > 0) {
			checkTime--;
		}

		this.angle %= Math.PI * 2;

		if (moves.size() > 0) {
			if (target.distanceFrom(ax(), ay()) < 20) {
				try {
					target = moves.remove(0);
				} catch (Exception e) {
				}
			}
			this.rotateTo(target.angle(ax(), ay()));
		}

		x += speed * Math.cos(angle);
		y += speed * Math.sin(angle);

		if (moves.size() == 0) {
			if (ax() < 20 || ax() > Driver.screenW - 20) {
				addMove(new Position(Driver.screenW / 2, ay()));
			} else if (ay() < 20 || ay() > Driver.screenH - 40) {
				addMove(new Position(ax(), Driver.screenH / 2));
			}
		}

	}

	public void rotateTo(double angle) {
		double diff = absMin(absMin(this.angle - angle, Math.PI * 2 - angle + this.angle),
				Math.PI * 2 + angle - this.angle);

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
