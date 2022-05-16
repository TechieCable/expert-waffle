import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Game {
	ArrayList<Boat> boats;
	CursorDrag cursorDrag;
	Map m;
	int boatGenTime;
	int cargo;
	boolean gameOver;
	boolean playing;

	public Game() {
		generate();
	}

	public void paint(Graphics g) {
		if (boatGenTime == 0 && boats.size() < Driver.maxBoats) {
			Sector entry = m.randomEntry();
			Boat b = new Boat(entry.x, entry.y, Boat.randomBoatNum());
			boats.add(b);
			boatGenTime = 500;
		}
		if (boatGenTime > 0)
			boatGenTime--;

		m.paint(g);

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
				for (DockPoly x : m.docks) {
					if (b.cargo.hasCargo(x.type) && x.isOver(b)) {
						b.clearMoves();
						b.x = x.dockX;
						b.y = x.dockY;
						b.angle = x.angle;
						b.dockInfo.enter(x);
					}
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
	}

	public void generate() {
		boats = new ArrayList<Boat>();
		cursorDrag = new CursorDrag();
		m = new Map("map1.png", "map1.txt");
		gameOver = false;
		playing = true;
	}

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

	public void boatDragHandler(MouseEvent e) {
		if (cursorDrag.setCurr(e)) {
			boats.get(cursorDrag.activeBoatID).addMove(new Position(cursorDrag.start));
		}
	}

}
