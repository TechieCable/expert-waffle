import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;

public class Boat extends RotatingPicture {
	static final Color purple = new Color(167, 40, 238);
	static final Color orange = new Color(255, 192, 0);
	static final int unloadTime = 100;

	int speed;
	double da;
	ArrayList<Position> moves;
	Position target;
	int checkTime;
	DockStamp dockInfo;
	Cargo cargo;
	int boatNum;
	boolean remove;
	ArrayList<WakeBubble> wake = new ArrayList<WakeBubble>();

	/**
	 * default constructor
	 */
	public Boat() {
		this((int) (Math.random() * (Driver.screenW - 20 - 20 + 1)) + 20,
				(int) (Math.random() * (Driver.screenH - 20 - 20 + 1)) + 20, 1);
	}

	/**
	 * constructor with position and boat selection
	 * 
	 * @param x
	 * @param y
	 * @param boatNum
	 */
	public Boat(int x, int y, int boatNum) {
		super(x, y, "boat" + boatNum + "-0.png", 1.4);
		speed = 4;
		da = Math.PI / 72 * speed;
		moves = new ArrayList<Position>();
		target = new Position(ax(), ay());
		checkTime = 0;
		dockInfo = new DockStamp();
		cargo = new Cargo(boatNum);
		this.boatNum = boatNum;
		remove = false;
	}

	public String toString() {
		return "Boat [speed=" + speed + ", da=" + da + ", " + (moves != null ? "moves=" + moves + ", " : "")
				+ (target != null ? "target=" + target + ", " : "") + "checkTime=" + checkTime
				+ (dockInfo != null ? "dock=" + dockInfo + ", " : "") + ", angle=" + angle + ", ax()=" + ax()
				+ ", ay()=" + ay() + "]";
	}

	/**
	 * returns the direction the boat is facing
	 * 
	 * 1: northeast, 2: northwest, 3: southwest, 4: southeast
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

	/**
	 * returns if x and y are within the boat
	 * 
	 * @param x
	 * @param y
	 * @return wasClicked
	 */
	public boolean clicked(int x, int y) {
		return getRect().contains(x, y);
	}

	/**
	 * returns a rectangle representation of the boat
	 * 
	 * @return Rectangle
	 */
	public Rectangle getRect() {
		double tempAngle = Math.PI * 2 - angle;
		tempAngle %= Math.PI * 2;
		int maxWidth = (int) Math.max(Math.abs(width * Math.sin(tempAngle)), Math.abs(height * Math.cos(tempAngle)));
		int maxHeight = (int) Math.max(Math.abs(width * Math.cos(tempAngle)), Math.abs(height * Math.sin(tempAngle)));
		return new Rectangle(ax() - maxWidth / 2, ay() - maxHeight / 2, maxWidth, maxHeight);
	}

	/**
	 * calculate speed, manage dock, paint move lines, draw wake, draw the boat,
	 * draw cargo
	 * 
	 * @param Graphics
	 */
	public void paint(Graphics g) {
		speed = (int) ((364 * scaleSize) / height); // 264
		da = Math.PI / 72 * speed / (364 / 264);

		if (dockInfo.time(cargo)) {
			angle -= Math.PI;
		}

		g.setColor(Color.WHITE);

		for (int i = 0; i < moves.size() - 1; i++) {
			g.drawLine(moves.get(i).x, moves.get(i).y, moves.get(i + 1).x, moves.get(i + 1).y);
		}

		for (int i = 0; i < wake.size(); i++) {
			WakeBubble a = wake.get(i);
			a.time--;
			g.setColor(WakeBubble.colors[a.colorID]);
			int radius = (int) (a.time / 10);
			g.fillOval(a.x - radius / 2, a.y - radius / 2, radius, radius);
			if (a.time <= 0) {
				wake.remove(i);
				i--;
			}
		}

		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(angle - Math.PI / 2, x + width / 2, y + height / 2);
		/** ROTATED ITEMS **/
		/* DRAW CARGO */
		for (int i = 0; i < cargo.get().length; i++) {
			if (cargo.get()[i] == 0) {
				continue;
			} else if (cargo.get()[i] == 1) {
				g2.setColor(orange);
			} else if (cargo.get()[i] == 2) {
				g2.setColor(purple);
			}
			g2.translate(-speed * Math.cos(angle) * (dockInfo.docked ? 0 : 1),
					-speed * Math.sin(angle) * (dockInfo.docked ? 0 : 1));
			if (boatNum == 4) {
				g2.fillRect(ax() - 15, ay() + 6 - (17 * i), 30, 15);
			} else if (boatNum == 2) {
				g2.fillRect(ax() - 14, ay() + 2 - (17 * i), 28, 15);
			} else if (boatNum == 1) {
				g2.fillRect(ax() - 13, ay() - 25, 26, 22);
			}
			g2.translate(speed * Math.cos(angle) * (dockInfo.docked ? 0 : 1),
					speed * Math.sin(angle) * (dockInfo.docked ? 0 : 1));
		}
		g2.rotate(-(angle - Math.PI / 2), x + width / 2, y + height / 2);
	}

	/**
	 * add a move to the list
	 * 
	 * @param Position
	 */
	public void addMove(Position p) {
		moves.add(p);
		if (dockInfo.docked) {
			dockInfo.docked = false;
			checkTime = 200;
		}
	}

	/**
	 * sets the boat's focus move
	 * 
	 * @param Position
	 */
	public void setFocus(Position p) {
		clearMoves();
		target = p;
		addMove(p);
		addMove(p);
	}

	/**
	 * clear moves from the list
	 */
	public void clearMoves() {
		moves.clear();
		target = new Position(ax(), ay());
	}

	/**
	 * find next move, rotate toward correct angle, and add wake to boat
	 */
	public void move() {
		if (dockInfo.docking || dockInfo.docked) {
			return;
		}
		if (checkTime > 0) {
			checkTime--;
		}

		this.angle %= Math.PI * 2;

		if (moves.size() > 0) {
			if (target.distanceFrom(ax(), ay()) < 20) { // 20
				try {
					target = moves.remove(0);
				} catch (Exception e) {
				}
			}
			this.rotateTo(target.angle(ax(), ay()));
		}

		addWake(400);

		x += speed * Math.cos(angle);
		y += speed * Math.sin(angle);
	}

	/**
	 * add wake to the boat
	 * 
	 * @param time
	 */
	public void addWake(int time) {
		wake.add(new WakeBubble(ax() - (height / 2 - 10) * Math.cos(angle), ay() - (height / 2 - 10) * Math.sin(angle),
				time / speed));
	}

	/**
	 * rotate toward the given angle
	 * 
	 * @param angle
	 */
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

	/**
	 * calculates absolute min between a and b and returns its
	 * 
	 * @param a
	 * @param b
	 * @return abs min of a and b
	 */
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

/**
 * handles boat cargo
 */
class Cargo {
	// 0 means no cargo
	// 1 means orange
	// 2 means purple
	private int[] cargo;

	public Cargo(int num) {
		cargo = new int[num];
		fillRandom();
	}

	/**
	 * fill the cargo with random colors
	 */
	public void fillRandom() {
		for (int i = 0; i < cargo.length; i++) {
			cargo[i] = (int) (Math.random() * 2) + 1;
		}
	}

	/**
	 * attempts to clear a piece of cargo of the specified type true if successful
	 * false if no cargo of type left
	 * 
	 * @param type
	 * @return success
	 */
	public boolean clearOne(int type) {
		for (int i = 0; i < cargo.length; i++) {
			if (cargo[i] == type || (type == 10 && cargo[i] != 0)) {
				cargo[i] = 0;
				Game.scr++;
				return true;
			}
		}
		return false;
	}

	/**
	 * returns if the boat has cargo of the specified type
	 * 
	 * @param type
	 * @return success
	 */
	public boolean hasCargo(int type) {
		if (type == 10) {
			return hasCargo();
		}
		for (int i : cargo) {
			if (type == i) {
				return true;
			}
		}
		return false;
	}

	/**
	 * determines if the boat has any cargo on board still
	 * 
	 * @return boat has cargo
	 */
	public boolean hasCargo() {
		return hasCargo(1) || hasCargo(2);
	}

	/**
	 * get the array of cargo
	 * 
	 * @return array
	 */
	public int[] get() {
		return cargo;
	}

	public String toString() {
		return Arrays.toString(cargo);
	}
}

class DockStamp {
	int time;
	int type;
	boolean docking, docked;

	public DockStamp() {
		time = 0;
		type = 0;
		docking = false;
		docked = false;
	}

	/**
	 * boat enters the dock: set the time to 0, store the dock type, set docking
	 * false and docked true
	 * 
	 * @param docking polygon object
	 */
	public void enter(DockPoly d) {
		time = 0;
		type = d.type;
		docking = false;
		docked = true;
	}

	/**
	 * fire with every boat paint: increments time, and every Boat.unloadTime
	 * attempts to remove a piece of cargo
	 * 
	 * @param c
	 * @return success
	 */
	public boolean time(Cargo c) {
		if (docked && c.hasCargo(type)) {
			time++;
			if (time > Boat.unloadTime) {
				time %= Boat.unloadTime;
				c.clearOne(type);
				if (!c.hasCargo(type)) {
					return true;
				}
			}
		}
		return false;
	}
}

@SuppressWarnings("serial")
class WakeBubble extends Point {
	// colors generated here:
	// https://coolors.co/defffc-9bc9fd-7db2de-8dbce2-defffc
	// available colors for wake
	static final Color[] colors = { new Color(222, 255, 252), new Color(155, 201, 253), new Color(125, 178, 222),
			new Color(141, 188, 226), new Color(222, 255, 252) };
	int time;
	int colorID;

	public WakeBubble(int x, int y, int time) {
		super(x, y);
		this.time = time;
		colorID = (int) (Math.random() * colors.length);
	}

	public WakeBubble(int x, int y) {
		this(x, y, 200);
	}

	public WakeBubble(double x, double y, int time) {
		this((int) x, (int) y, time);
	}
}