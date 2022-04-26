import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Game {
	ArrayList<Boat> boats;
	CursorDrag cursorDrag;
	Map m;
	int boatGenTime;

	public Game() {
		generate();
	}

	public void paint(Graphics g) {
		if (boatGenTime == 0 && boats.size() < 10) {
			EntrySector entry = m.randomEntry();
			Boat b = new Boat(entry.x, entry.y, "boat1-0.png");
			System.out.println(b);
			b.clearMoves();
			boats.add(b);
			boatGenTime = 500;
		}
		if (boatGenTime > 0)
			boatGenTime--;

		m.paint(g);

		for (int i = 0; i < boats.size(); i++) {
			Boat b = boats.get(i);

			// dock checks
			for (DockSector x : m.dockPoints) {
				DockSector dock = x.dock(b);
				if (dock != null) {
					b.clearMoves();
					b.x = dock.dockX;
					b.y = dock.dockY;
					b.angle = dock.angle;
					b.docked = true;
				}
			}

			if (b.checkTime == 0) {
				// land checks
				LandSector over = m.overLand(b);
				if (over != null) {
					b.clearMoves();
					int dist = 200;
					if (over.redirection == 1) {
						b.addMove(new Position(b.ax() + dist, b.ay() - dist));
						b.addMove(new Position(b.ax() + (dist + 20), b.ay() - (dist + 20)));
					} else if (over.redirection == 2) {
						b.addMove(new Position(b.ax() - dist, b.ay() - dist));
						b.addMove(new Position(b.ax() - (dist + 20), b.ay() - (dist + 20)));
					} else if (over.redirection == 3) {
						b.addMove(new Position(b.ax() - dist, b.ay() + dist));
						b.addMove(new Position(b.ax() - (dist + 20), b.ay() + (dist + 20)));
					} else if (over.redirection == 4) {
						b.addMove(new Position(b.ax() + dist, b.ay() + dist));
						b.addMove(new Position(b.ax() + (dist + 20), b.ay() + (dist + 20)));
					}
					b.checkTime = 50;
				}
			}
			b.paint(g);
		}
	}

	public void generate() {
		boats = new ArrayList<Boat>();
		cursorDrag = new CursorDrag();
		m = new Map("map1.png", "map1.txt");

		for (int i = 0; i < 0; i++) {
			boats.add(new Boat());
		}
	}

	public void boatClickHandler(MouseEvent e) {
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
