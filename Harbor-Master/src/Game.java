import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Game {
	static int maxBoats = 10;
	static int scr = 0;

	ArrayList<Boat> boats;
	CursorDrag cursorDrag;
	Map m;
	int boatGenTime;
	int score;
	boolean gameOver;
	boolean playing;

	public Game() {
		generate();
	}

	/**
	 * generate the game information
	 */
	public void generate() {
		boats = new ArrayList<Boat>();
		cursorDrag = new CursorDrag();
		m = new Map("map1.png", "map1.txt");
		score = 0;
		gameOver = false;
		playing = false;
	}

	/**
	 * spawn boats, paint map, manage score, boat collisions, dock checks, land
	 * checks, border checks, paint boats, paint score
	 * 
	 * @param Graphics
	 */
	public void paint(Graphics g) {
		if (boatGenTime == 0 && boats.size() < maxBoats * (score + 10.0) / 10) {
			Sector entry = m.randomEntry();
			Boat b = new Boat(entry.x, entry.y, randomBoatNum());
			boats.add(b);
			boatGenTime = (int) (500 * Math.pow(Math.E, -(score / 500.0 + 1)) + 317);
		}
		if (boatGenTime > 0)
			boatGenTime--;

		m.paint(g);

		if (Game.scr > 0) {
			score += Game.scr;
			Game.scr = 0;
		}

		for (int i = 0; i < boats.size(); i++) {
			Boat b = boats.get(i);

			for (int j = i + 1; j < boats.size(); j++) {
				if (b.getRect().intersects(boats.get(j).getRect())) {
					gameOver = true;
					return;
				}
			}

			// dock checks
			if (b.cargo.hasCargo() && !b.dockInfo.docked) {
				boolean undock = true; // undock unless the boat is over a dock
				for (DockPoly x : m.docks) {
					if (b.cargo.hasCargo(x.type) && x.isOver(b)) {
						b.clearMoves();
						b.dockInfo.docking = true;
						undock = false; // don't undock
						if (x.snap(b)) {
							b.ax(x.dockX);
							b.ay(x.dockY);
							b.angle = x.angle;
							b.dockInfo.enter(x);
						} else {
							b.addWake(200);
							// drift boat into place
							if (x.dockX - b.ax() > 1) {
								b.x += b.speed / 2;
							} else if (b.ax() - x.dockX > 1) {
								b.x -= b.speed / 2;
							}
							if (x.dockY - b.ay() > 1) {
								b.y += b.speed / 2;
							} else if (b.ay() - x.dockY > 1) {
								b.y -= b.speed / 2;
							}
							b.rotateTo(x.angle);
						}
					}
				}
				if (b.dockInfo.docking && undock) {
					b.dockInfo.docking = false;
				}
			}

			// land checks
			if (b.checkTime == 0) {
				if (m.overLand(b)) {
					b.setFocus(redirectionPos(b));
					b.checkTime = 100;
				}
			}
			b.paint(g);

			// border checks
			if (b.moves.size() == 0
					&& (b.ax() < 20 || b.ax() > Driver.screenW - 20 || b.ay() < 20 || b.ay() > Driver.screenH - 40)) {
				if (!b.cargo.hasCargo()) {
					boats.remove(i);
					i--;
				} else {
					b.setFocus(redirectionPos(b));
				}
			}
		}

		g.setFont(Driver.defaultFont.deriveFont((float) 30));
		g.setColor(Color.BLACK);
		g.fillRect(Driver.screenW - 65, 5, 60, 30);
		g.setColor(Color.WHITE);
		g.drawString("000".substring((score + "").length()) + score, Driver.screenW - 60, 30);
		g.setFont(Driver.defaultFont);
	}

	/**
	 * returns a position either around the boat that is on the water, or a random
	 * water position
	 * 
	 * @param Boat
	 * @return valid position
	 */
	public Position redirectionPos(Boat b) {
		Position p = new Position((int) (b.ax() - 100 * Math.cos(b.angle - Math.PI / 4)),
				(int) (b.ay() - 100 * Math.sin(b.angle - Math.PI / 4)));
		int angleItr = 1;
		while (!m.inWater(p)) {
			p = new Position((int) (b.ax() - 200 * Math.cos(b.angle - Math.PI / 4 - Math.PI * 2 * angleItr)),
					(int) (b.ay() - 200 * Math.sin(b.angle - Math.PI / 4 - Math.PI * 2 * angleItr)));
			angleItr++;
			if (angleItr > 3) {
				p = new Position(m.randomWaterPoint());
			}
		}
		return p;
	}

	/**
	 * handles clicks and checks if the click is on a boat
	 * 
	 * @param MouseEvent
	 */
	public void boatPressHandler(MouseEvent e) {
		for (int i = 0; i < boats.size(); i++) {
			if (boats.get(i).clicked(e.getX(), e.getY())) {
				boats.get(i).clearMoves();
				cursorDrag.setStart(e);
				cursorDrag.activeBoatID = i;
				boats.get(i).addMove(new Position(cursorDrag.start));
			}
		}
	}

	/**
	 * handles drags after a boat has been clicked
	 * 
	 * @param MouseEvent
	 */
	public void boatDragHandler(MouseEvent e) {
		if (cursorDrag.setCurr(e)) {
			try {
				boats.get(cursorDrag.activeBoatID).addMove(new Position(cursorDrag.start));
			} catch (Exception e2) {
			}
		}
	}

	/**
	 * returns a random number from 1, 2, and 4
	 * 
	 * @return 1, 2, or 4, randomly
	 */
	public static int randomBoatNum() {
		int res = 0;
		while (!(res == 1 || res == 2 || res == 4)) {
			res = (int) (Math.random() * 4) + 1;
		}
		return res;
	}

}
