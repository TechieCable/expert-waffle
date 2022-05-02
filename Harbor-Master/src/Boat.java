import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Boat extends RotatingPicture {
	static final int startTime = 200;

	int speed;
	double da;
	ArrayList<Position> moves = new ArrayList<Position>();
	Position target;
	int checkTime;
	boolean docked;
	Cargo cargo;

	public Boat() {
		this((int) (Math.random() * (Driver.screenW - 20 - 20 + 1)) + 20,
				(int) (Math.random() * (Driver.screenH - 20 - 20 + 1)) + 20, 1);
	}

	public Boat(int x, int y, int boatNum) {
		super(x, y, "boat" + boatNum + "-0.png", 1.4);
		speed = 4;
		da = Math.PI / 72 * speed;
		target = new Position(ax(), ay());
		checkTime = 0;
		docked = false;
		cargo = new Cargo(boatNum);
	}

	public String toString() {
		return "Boat [speed=" + speed + ", da=" + da + ", " + (moves != null ? "moves=" + moves + ", " : "")
				+ (target != null ? "target=" + target + ", " : "") + "checkTime=" + checkTime + ", docked=" + docked
				+ ", angle=" + angle + ", ax()=" + ax() + ", ay()=" + ay() + "]";
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
		/** Rotated Graphics **/
//		g2.drawRect(ax() - 15, ay() - 45, 30, 15);
//		g2.drawRect(ax() - 15, ay() - 28, 30, 15);
//		g2.drawRect(ax() - 15, ay() - 11, 30, 15);
//		g2.drawRect(ax() - 15, ay() + 6, 30, 15);

		for (int i = 0; i < cargo.get().length; i++) {
			g2.drawRect(ax() - 15, ay() + 6 - (17 * i), 30, 15);
		}
		g2.rotate(-(angle - Math.PI / 2), x + width / 2, y + height / 2);
	}

	public void addMove(Position p) {
		moves.add(p);
		docked = false;
		checkTime = startTime;
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

class Cargo {
	// 0 means no cargo
	// 1 means orange
	// 2 means purple
	private int[] cargo;

	public Cargo(int num) {
		cargo = new int[num];
	}

	public void fillRandom() {
		for (int i = 0; i < cargo.length; i++) {
			cargo[i] = (int) (Math.random() * 2) + 1;
		}
	}

	public boolean clearOne(int type) {
		for (int i = 0; i < cargo.length; i++) {
			if (cargo[i] == type) {
				cargo[i] = 0;
				return true;
			}
		}
		return false;
	}

	public int[] get() {
		return cargo;
	}
}