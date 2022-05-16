import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Map extends Picture {
	ArrayList<Sector> entryPoints = new ArrayList<Sector>();
	ArrayList<EPolygon> land = new ArrayList<EPolygon>();
	ArrayList<DockPoly> docks = new ArrayList<DockPoly>();
	ArrayList<EPolygon> water = new ArrayList<EPolygon>();

	public Map(int id) {
		this("map" + id + ".png", "map" + id + ".txt");
	}

	public Map(String fileName, String sectorFileName) {
		super(0, 0, fileName, 1);

		try {
			Scanner s = new Scanner(new File(Map.class.getResource("/map-sectors/" + sectorFileName).getPath()));

			while (s.hasNextLine()) {
				String x = s.next();
				if (x.equals("/")) {
					s.nextLine();
				} else if (x.equals("d")) {
					docks.add(new DockPoly(s.next(), s.next(), s.next(), s.next(), s.next()));
				} else if (x.equals("e")) {
					entryPoints.add(new Sector(s.next(), s.next()));
				} else if (x.equals("l")) {
					land.add(new EPolygon(s.next()));
				} else if (x.equals("w")) {
					water.add(new EPolygon(s.next()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void paint(Graphics g) {
		super.paint(g);

//		for (EntrySector x : entryPoints) {
//			g.setColor(Color.white);
//			g.fillOval(x.x - Sector.width / 2, x.y - Sector.width / 2, Sector.width, Sector.width);
//		}
	}

	public static int[] cors(String s) {
		return new int[] { Integer.valueOf(s.substring(0, Sector.z.length())),
				Integer.valueOf(s.substring(Sector.z.length())) };
	}

	public Sector randomEntry() {
		return entryPoints.get((int) (Math.random() * (entryPoints.size())));
	}

	public boolean overLand(Point p) {
		for (EPolygon x : land) {
			if (x.contains(p)) {
				return true;
			}
		}
		return false;
	}

	public boolean overLand(Boat b) {
		for (EPolygon x : land) {
			if (x.intersects(b.getRect())) {
				return true;
			}
		}
		return false;
	}

	public boolean inWater(Point p) {
		for (EPolygon x : water) {
			if (x.contains(p)) {
				return true;
			}
		}
		return false;
	}

	public Point randomWaterPoint() {
		EPolygon rand = water.get((int) (Math.random() * water.size()));
		Rectangle r = rand.getBounds();
		Point res = new Point(-100, -100);
		while (!rand.contains(res)) {
			res.x = (int) (Math.random() * r.width) + r.x;
			res.y = (int) (Math.random() * r.height) + r.y;
		}
		return res;
	}
}

@SuppressWarnings("serial")
class EPolygon extends Polygon {
	public EPolygon(String points) {
		String[] xPoints = points.substring(0, points.indexOf("]:")).replace("[", "").split(",");
		String[] yPoints = points.substring(points.indexOf("]:")).replace("]", "").replace(":[", "").split(",");
		if (xPoints.length != yPoints.length) {
			System.err.println("Polygon point arrays are not of equal length.");
		}
		for (int i = 0; i < xPoints.length; i++) {
			super.addPoint(Integer.valueOf(xPoints[i]), Integer.valueOf(yPoints[i]));
		}
	}

	public boolean isOver(Boat b) {
		return this.intersects(b.getRect());
	}
}

@SuppressWarnings("serial")
class DockPoly extends EPolygon {
	int dockX, dockY;
	double angle;
	int type;

	public DockPoly(String points, String dockX, String dockY, String angle, String type) {
		super(points);
		this.dockX = Integer.valueOf(dockX);
		this.dockY = Integer.valueOf(dockY);
		this.angle = Integer.valueOf(angle) * Math.PI / 180;
		this.type = Integer.valueOf(type);
	}

}

@SuppressWarnings("serial")
class Sector extends Position {
	static int width = 20;
	static String z = "000";
	int highlight = 0;

	public Sector(int x, int y) {
		super(x, y);
	}

	public Sector(String x, String y) {
		this(Integer.valueOf(x), Integer.valueOf(y));
	}
}
