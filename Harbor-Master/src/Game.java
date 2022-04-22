import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Game {
	ArrayList<Boat> boats;
	CursorDrag cursorDrag;
	Map m;

	public Game() {
		generate();
	}

	public void paint(Graphics g) {
		m.paint(g);

		for (int i = 0; i < boats.size(); i++) {
			Boat b = boats.get(i);
			if (b.checkTime == 0) {
				Sector over = m.overLand(b);
				if (over != null) {
					b.clearMoves();
					System.out.println("hello!");
					if (over.redirection == 1) {
						b.addMove(new Position(b.ax() + 50, b.ay() - 50));
					} else if (over.redirection == 2) {
						b.addMove(new Position(b.ax() - 50, b.ay() - 50));
					} else if (over.redirection == 3) {
						b.addMove(new Position(b.ax() - 50, b.ay() + 50));
					} else if (over.redirection == 4) {
						b.addMove(new Position(b.ax() + 50, b.ay() + 50));
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

		for (int i = 0; i < 1; i++) {
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
