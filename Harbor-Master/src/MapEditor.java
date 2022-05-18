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
	public static int screenW = 1920, screenH = 1080;

	Map m = new Map("Title.png", "blank.txt");
	ArrayList<PointedPolygon> polygons = new ArrayList<PointedPolygon>();
	ArrayList<Sector> entrySectors = new ArrayList<Sector>();
	Rectangle infoSection = new Rectangle(10, 10, 250, 25);
	boolean draggingInfoSection = false;
	Point mouseDragPoint = new Point();
	Point mouse = new Point();

	int currPolygon = 0;

	ArrayList<Message> messages = new ArrayList<Message>();

	public void paint(Graphics g) {
		infoSection = new Rectangle(infoSection.x, infoSection.y, 230, 20 * messages.size());
		if (currPolygon >= polygons.size()) {
			currPolygon = 0;
		} else if (currPolygon < 0) {
			currPolygon = polygons.size() - 1;
		}

		for (Message m : messages) {
			m.time--;
		}

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenW, screenH);

		g.setColor(Color.WHITE);

		m.paint(g);

		for (int i = 0; i < polygons.size(); i++) {
			g.setColor(i == currPolygon ? Color.GREEN : Color.WHITE);
			polygons.get(i).paint(g);
		}
		g.setColor(Color.WHITE);
		for (Sector x : entrySectors) {
			g.drawOval(x.x - 400/2, x.y - 400/2, 400, 400);
		}

		g.setColor(Color.WHITE);
		g.fillRect(infoSection.x, infoSection.y, infoSection.width, infoSection.height);

		g.setColor(Color.BLACK);
		g.setFont(new Font("Dialog", Font.BOLD, 16));
		for (int i = 0; i < messages.size(); i++) {
			messages.get(i).time--;
			if (messages.get(i).time <= 0) {
				messages.remove(i);
				i--;
				continue;
			}
			g.drawString(messages.get(i).message, infoSection.x + 5, infoSection.y + 15 + 20 * i);
		}

		messages.add(0, new Message("Currently editing polygon #" + currPolygon, 3));

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

		frame.setVisible(true);

		while (m.land.size() > 0) {
			Polygon x = m.land.remove(0);
			polygons.add(new PointedPolygon(x.xpoints, x.ypoints, x.npoints));
		}
		while (m.docks.size() > 0) {
			Polygon x = m.docks.remove(0);
			polygons.add(new PointedPolygon(x.xpoints, x.ypoints, x.npoints));
		}
		while (m.water.size() > 0) {
			Polygon x = m.water.remove(0);
			polygons.add(new PointedPolygon(x.xpoints, x.ypoints, x.npoints));
		}
		while (m.entryPoints.size() > 0) {
			entrySectors.add(m.entryPoints.remove(0));
		}

		messages.add(new Message("----------", 500));
		messages.add(new Message("arrow keys change current", 500));
		messages.add(new Message("polygon selection", 500));
		messages.add(new Message("", 500));
		messages.add(new Message("drag this box to move it", 500));
		messages.add(new Message("", 500));
		messages.add(new Message("hit n to create a new polygon", 500));
		messages.add(new Message("", 500));
		messages.add(new Message("left click to add a point", 500));
		messages.add(new Message("right click to remove a point", 500));
	}

	Timer t = new Timer(16, this);

	public void actionPerformed(ActionEvent m) {
		repaint();
	}

	public void keyPressed(KeyEvent m) {
		switch (m.getKeyCode()) {
		case 38: // up
			currPolygon++;
			break;
		case 40: // down
			currPolygon--;
			break;
		case 32: // space
			messages.add(new Message("Printing polygons...", 100));
			System.out.println("Printing all polygons:");
			for (PointedPolygon x : polygons) {
				System.out.println(x);
			}
			break;
		case 78: // n
			messages.add(new Message("Added a new polygon", 100));
			messages.add(new Message("Access it with the arrow keys", 200));
			polygons.add(new PointedPolygon());
		case 18:
			break;
		case 115:
			break;
		case 77:
			messages.add(new Message(mouse + "", 100));
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

	public void mousePressed(MouseEvent m) {
		if (infoSection.contains(m.getPoint())) {
			draggingInfoSection = true;
			mouseDragPoint = m.getPoint();
		} else if (m.getButton() == 1) {
			polygons.get(currPolygon).addPoint(m.getX(), m.getY());
		} else {
			polygons.get(currPolygon).removePoint(m.getX(), m.getY());
		}
	}

	public void mouseReleased(MouseEvent m) {
		draggingInfoSection = false;
	}

	public void mouseDragged(MouseEvent m) {
		if (draggingInfoSection) {
			infoSection.x += m.getX() - mouseDragPoint.getX();
			infoSection.y += m.getY() - mouseDragPoint.getY();
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

class Message {
	String message;
	int time;

	public Message(String message, int time) {
		this.message = message;
		this.time = time;
	}
}