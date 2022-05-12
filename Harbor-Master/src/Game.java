import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
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
			if (b.remove) {
				boats.remove(i);
				i--;
			}

			// dock checks
			if (b.cargo.hasCargo() && !b.dockInfo.docked) {
				for (DockPoly x : m.dockPolys) {
					if (b.cargo.hasCargo(x.type) && x.isOver(b)) {
						b.clearMoves();
						b.x = x.dockX;
						b.y = x.dockY;
						b.angle = x.angle;
						b.dockInfo.enter(x);
					}
				}
			}

			if (b.checkTime == 0) {
				// land checks
				if (m.overLand(b)) {
					Position p = new Position((int) (b.ax() - 100 * Math.cos(b.angle - Math.PI / 4)),
							(int) (b.ay() - 100 * Math.sin(b.angle - Math.PI / 4)));
					b.clearMoves();
					b.target = p;
					b.addMove(p);
					b.addMove(p);
					b.checkTime = 100;
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
