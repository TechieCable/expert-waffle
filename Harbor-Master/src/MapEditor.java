import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class MapEditor extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
	static final int screenW = 1920, screenH = 1080;

	Map m = new Map(3);
	Rectangle infoSection = new Rectangle(10, 10, 250, 25);
	boolean dragger = false;
	Point mouseDragPoint = new Point();
	Point mouse = new Point();

	int currObject = 0;
	int currList = 0;

	@SuppressWarnings("rawtypes")
	ArrayList[] lists;

	ArrayList<Message> messages = new ArrayList<Message>();
	int currTime = 0;

	public void paint(Graphics g) {
		infoSection = new Rectangle(infoSection.x, infoSection.y, 230, 20 * messages.size());
		if (currList >= lists.length) {
			currList = 0;
		} else if (currList < 0) {
			currList = lists.length - 1;
		}
		if (currObject >= lists[currList].size()) {
			currObject = 0;
		} else if (currObject < 0) {
			currObject = lists[currList].size() - 1;
		}

		if (currTime > 10) {
			currTime %= 10;
			String m = "Displaying ";
			if (currList == 0) { // entryPoints
				m += "entryPoints";
			} else if (currList == 1) { // land
				m += "land";
			} else if (currList == 2) { // docks
				m += "docks";
			} else if (currList == 3) { // water
				m += "water";
			}
			messages.add(new Message(m + " #" + currObject, 11));
		}
		currTime++;

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenW, screenH);

		g.setColor(Color.WHITE);

		m.paint(g);

		for (int i = 0; i < lists.length; i++) {
			if (i == currList) {
				continue;
			}
			if (i == 0) { // entryPoints
				paintEntryPoints(g);
			} else if (i == 1) { // land
				paintLand(g);
			} else if (i == 2) { // docks
				paintDocks(g);
			} else if (i == 3) { // water
				paintWater(g);
			}
		}

		if (currList == 0) { // entryPoints
			paintEntryPoints(g);
		} else if (currList == 1) { // land
			paintLand(g);
		} else if (currList == 2) { // docks
			paintDocks(g);
		} else if (currList == 3) { // water
			paintWater(g);
		}

		g.setColor(Color.BLACK);
		g.fillRect(infoSection.x, infoSection.y, infoSection.width, infoSection.height);

		g.setFont(new Font("Dialog", Font.BOLD, 16));
		for (int i = 0; i < messages.size(); i++) {
			g.setColor(messages.get(i).color());
			g.drawString(messages.get(i).message(), infoSection.x + 5, infoSection.y + 15 + 20 * i);
			messages.get(i).incTime();
			if (messages.get(i).time() <= 0) {
				messages.remove(i);
				i--;
			}
		}
	}

	public void paintEntryPoints(Graphics g) {
		/* entry points ID 0 */
		for (int i = 0; i < lists[0].size(); i++) {
			g.setColor((currList == 0 && currObject == i) ? Color.GREEN : Color.WHITE);
			Sector x = (Sector) lists[0].get(i);
			g.drawOval(x.x - 200, x.y - 200, 400, 400);
		}
	}

	public void paintLand(Graphics g) {
		/* land ID 1 */
		for (int i = 0; i < lists[1].size(); i++) {
			g.setColor((currList == 1 && currObject == i) ? Color.GREEN : Color.WHITE);
			PointedPolygon x = (PointedPolygon) lists[1].get(i);
			g.drawPolygon(x.getPolygon());
			for (int j = 0; j < x.points.size(); j++) {
				g.drawRect(x.points.get(j).x - 5, x.points.get(j).y - 5, 10, 10);
			}
		}
	}

	public void paintDocks(Graphics g) {
		/* docks ID 2 */
		for (int i = 0; i < lists[2].size(); i++) {
			g.setColor((currList == 2 && currObject == i) ? Color.GREEN : Color.WHITE);
			PointedDockPoly x = (PointedDockPoly) lists[2].get(i);
			g.drawPolygon(x.getPolygon());
			g.drawOval(x.dockX - 5, x.dockY - 5, 10, 10);
			g.drawLine(x.dockX, x.dockY, (int) (x.dockX + Math.cos(Math.PI * 2 - x.angle) * 20),
					(int) (x.dockY + Math.sin(Math.PI * 2 - x.angle) * 20));
			for (int j = 0; j < x.points.size(); j++) {
				g.drawRect(x.points.get(j).x - 5, x.points.get(j).y - 5, 10, 10);
			}
		}
	}

	public void paintWater(Graphics g) {
		/* water ID 3 */
		for (int i = 0; i < lists[3].size(); i++) {
			g.setColor((currList == 3 && currObject == i) ? Color.GREEN : Color.WHITE);
			PointedPolygon x = (PointedPolygon) lists[3].get(i);
			g.drawPolygon(x.getPolygon());
			for (int j = 0; j < x.points.size(); j++) {
				g.drawRect(x.points.get(j).x - 5, x.points.get(j).y - 5, 10, 10);
			}
		}
	}

	public static void main(String[] arg) {
		@SuppressWarnings("unused")
		MapEditor d = new MapEditor();
	}

	public MapEditor() {
		JFrame frame = new JFrame("Harbor Master MapEditor");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(MapEditor.class.getResource("/imgs/map1.png")));
		frame.setSize(screenW, screenH);
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(this);
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
		t.start();

		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);

		ArrayList<Sector> list1 = new ArrayList<Sector>();
		while (m.entryPoints.size() > 0) {
			list1.add(m.entryPoints.remove(0));
		}
		ArrayList<PointedPolygon> list2 = new ArrayList<PointedPolygon>();
		while (m.land.size() > 0) {
			list2.add(new PointedPolygon(m.land.remove(0)));
		}
		ArrayList<PointedDockPoly> list3 = new ArrayList<PointedDockPoly>();
		while (m.docks.size() > 0) {
			list3.add(new PointedDockPoly(m.docks.remove(0)));
		}
		ArrayList<PointedPolygon> list4 = new ArrayList<PointedPolygon>();
		while (m.water.size() > 0) {
			list4.add(new PointedPolygon(m.water.remove(0)));
		}

		lists = new ArrayList[] { list1, list2, list3, list4 };

		frame.setVisible(true);

		messages.add(new Message("arrow keys change current", 250));
		messages.add(new Message("polygon selection", 250));
		messages.add(new Message("", 250));
		messages.add(new Message("drag this box to move it", 250));
		messages.add(new Message("", 250));
		messages.add(new Message("hit n to create a new polygon", 250));
		messages.add(new Message("", 250));
		messages.add(new Message("left click to add a point", 250));
		messages.add(new Message("right click to remove a point", 250));
	}

	Timer t = new Timer(16, this);

	public void actionPerformed(ActionEvent m) {
		repaint();
	}

	@SuppressWarnings("unchecked")
	public void keyPressed(KeyEvent m) {
		switch (m.getKeyCode()) {
		case 37: // left
			currList--;
			break;
		case 39: // right
			currList++;
			break;
		case 38: // up
			currObject++;
			break;
		case 40: // down
			currObject--;
			break;
		case 32: // space
			messages.add(new Message("Printing items...", 100));
			System.out.println("\n\n\n\n\n");
			for (Object x : lists[0]) { // entrySector
				Sector a = (Sector) x;
				System.out.println("e " + a.x + " " + a.y);
			}
			for (Object x : lists[1]) { // land
				PointedPolygon a = (PointedPolygon) x;
				System.out.println(
						"l " + (Arrays.toString(a.getPolygon().xpoints) + ":" + Arrays.toString(a.getPolygon().ypoints))
								.replaceAll(" ", ""));
			}
			for (Object x : lists[2]) { // docks
				PointedDockPoly a = (PointedDockPoly) x;
				System.out.println("d "
						+ (Arrays.toString(a.getPolygon().xpoints) + ":" + Arrays.toString(a.getPolygon().ypoints))
								.replaceAll(" ", "")
						+ " " + a.dockX + " " + a.dockY + " " + (int) (a.angle * 180 / Math.PI) + " " + a.type);
			}
			for (Object x : lists[3]) { // water
				PointedPolygon a = (PointedPolygon) x;
				System.out.println(
						"w " + (Arrays.toString(a.getPolygon().xpoints) + ":" + Arrays.toString(a.getPolygon().ypoints))
								.replaceAll(" ", ""));
			}
			break;
		case 78: // n
			messages.add(new Message("Added a new object", 100));
			messages.add(new Message("Access it with the arrow keys", 200));
			if (currList == 0) { // entryPoints
				lists[0].add(new Sector(MapEditor.screenW / 2, MapEditor.screenH / 2));
			} else if (currList == 1) { // land
				lists[1].add(new PointedPolygon());
			} else if (currList == 2) { // docks
				lists[2].add(new PointedDockPoly());
			} else if (currList == 3) { // water
				lists[3].add(new PointedPolygon());
			}
			break;
		case 68: // d
			messages.add(new Message("Removed an object", 100));
			lists[currList].remove(currObject);
			break;
		case 18:
			break;
		case 115:
			break;
		case 77: // m
			messages.add(new Message(mouse + "", 100));
			break;
		default:
			System.out.println(m);
			break;
		}
	}

	public void keyReleased(KeyEvent m) {
	}

	public void keyTyped(KeyEvent m) {
	}

	public void mouseClicked(MouseEvent m) {
	}

	public void mouseEntered(MouseEvent m) {
	}

	public void mouseExited(MouseEvent m) {
	}

	@SuppressWarnings("unchecked")
	public void mousePressed(MouseEvent m) {
		if (m.getButton() == 1) {
			if (m.isControlDown()) {
				if (currList == 0) { // entryPoints
				} else if (currList == 1) { // land
					PointedPolygon x = (PointedPolygon) lists[1].get(currObject);
					x.removePoint(m.getX(), m.getY());
				} else if (currList == 2) { // docks
					PointedDockPoly x = (PointedDockPoly) lists[2].get(currObject);
					x.removePoint(m.getX(), m.getY());
				} else if (currList == 3) { // water
					PointedPolygon x = (PointedPolygon) lists[3].get(currObject);
					x.removePoint(m.getX(), m.getY());
				}
			} else if (m.isAltDown()) {
				if (currList == 2) {
					PointedDockPoly x = (PointedDockPoly) lists[2].get(currObject);
					x.dockX = m.getX();
					x.dockY = m.getY();
				}
			} else {
				if (currList == 0) { // entryPoints
					lists[0].add(new Sector(m.getX(), m.getY()));
				} else if (currList == 1) { // land
					PointedPolygon x = (PointedPolygon) lists[1].get(currObject);
					x.addPoint(m.getX(), m.getY());
				} else if (currList == 2) { // docks
					PointedDockPoly x = (PointedDockPoly) lists[2].get(currObject);
					x.addPoint(m.getX(), m.getY());
				} else if (currList == 3) { // water
					PointedPolygon x = (PointedPolygon) lists[3].get(currObject);
					x.addPoint(m.getX(), m.getY());
				}
			}
		} else if (m.getButton() == 3) {
			dragger = true;
			mouseDragPoint = m.getPoint();
		}
	}

	public void mouseReleased(MouseEvent m) {
		dragger = false;
	}

	public void mouseDragged(MouseEvent m) {
		if (dragger) {
			if (infoSection.contains(m.getPoint())) {
				infoSection.x += m.getX() - mouseDragPoint.getX();
				infoSection.y += m.getY() - mouseDragPoint.getY();
			} else {
				if (currList != 0) {
					for (Point x : ((PointedPolygon) lists[currList].get(currObject)).points) {
						if (x.distance(m.getPoint()) < 10) {
							x.x += m.getX() - mouseDragPoint.getX();
							x.y += m.getY() - mouseDragPoint.getY();
							break;
						}
					}
				} else {
					Sector x = (Sector) lists[0].get(currObject);
					Rectangle a = new Rectangle(x.x - 200, x.y - 200, 400, 400);
					if (a.contains(m.getPoint())) {
						x.x += m.getX() - mouseDragPoint.getX();
						x.y += m.getY() - mouseDragPoint.getY();
					}
				}
			}
		}
		mouseDragPoint = m.getPoint();
	}

	public void mouseMoved(MouseEvent m) {
		mouse = m.getPoint();
	}

}

class PointedPolygon {
	ArrayList<Point> points = new ArrayList<Point>();

	public PointedPolygon(int[] xpoints, int[] ypoints, int npoints) {
		for (int i = 0; i < npoints; i++) {
			points.add(new Point(xpoints[i], ypoints[i]));
		}
	}

	public PointedPolygon() {
	}

	public PointedPolygon(Polygon x) {
		for (int i = 0; i < x.npoints; i++) {
			points.add(new Point(x.xpoints[i], x.ypoints[i]));
		}
	}

	public void addPoint(int x, int y) {
		points.add(new Point(x, y));
	}

	public Polygon getPolygon() {
		Polygon res = new Polygon();
		for (Point x : points) {
			res.addPoint(x.x, x.y);
		}
		return res;
	}

	public void paint(Graphics g) {
		g.drawPolygon(getPolygon());
		for (Point x : points) {
			g.drawRect(x.x - 5, x.y - 5, 10, 10);
		}
	}

	public void removePoint(int x, int y) {
		for (int i = 0; i < points.size(); i++) {
			if (Math.sqrt(Math.pow(x - points.get(i).x, 2) + Math.pow(y - points.get(i).y, 2)) < 10) {
				points.remove(i);
				return;
			}
		}
	}

	public String toString() {
		return Arrays.toString(getPolygon().xpoints).replace(" ", "") + ":"
				+ Arrays.toString(getPolygon().ypoints).replace(" ", "");
	}
}

class PointedDockPoly extends PointedPolygon {
	int dockX, dockY;
	double angle;
	int type;

	public PointedDockPoly() {
		super();
	}

	public PointedDockPoly(DockPoly x) {
		super(x);
		dockX = x.dockX;
		dockY = x.dockY;
		angle = x.angle;
		type = x.type;
	}
}
