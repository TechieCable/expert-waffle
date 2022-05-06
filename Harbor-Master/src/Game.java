import java.awt.Graphics;
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
		if (boatGenTime == 0 && boats.size() < Driver.maxBoats) {
			EntrySector entry = m.randomEntry();
			Boat b = new Boat(entry.x, entry.y, Boat.randomBoatNum());
			b.clearMoves();
			b.addMove(new Position(Driver.screenW / 2, Driver.screenH / 2));
			b.addMove(new Position(Driver.screenW / 2, Driver.screenH / 2));
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
//				DockSector dock = x.dock(b);
				if (x.dock(b) && b.cargo.hasCargo(x.type)) {
					b.clearMoves();
					b.x = x.dockX;
					b.y = x.dockY;
					b.angle = x.angle;
					b.dockInfo.enter(x);
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
