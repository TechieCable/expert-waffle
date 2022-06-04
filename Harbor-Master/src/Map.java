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

	/**
	 * accepts an integer ID to find a map in the form map#.png and map#.txt
	 * 
	 * @param ID
	 */
	public Map(int id) {
		this("map" + id + ".png", "map" + id + ".txt");
	}

	/**
	 * creates a map, importing its image and information from the sector file
	 * 
	 * @param fileName
	 * @param sectorFileName
	 */
	public Map(String fileName, String sectorFileName) {
		super(0, 0, fileName, 1);

		try {
//			Scanner s = new Scanner(new File(Map.class.getResource("/map-sectors/" + sectorFileName).getPath()));
			Scanner s = new Scanner(new File("src/map-sectors/" + sectorFileName));

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

	/**
	 * returns a random entry point from the list of available points
	 * 
	 * @return random entry Sector
	 */
	public Sector randomEntry() {
		return entryPoints.get((int) (Math.random() * (entryPoints.size())));
	}

	/**
	 * returns true if a point is over land
	 * 
	 * @param Point
	 * @return is over land
	 */
	public boolean overLand(Point p) {
		for (EPolygon x : land) {
			if (x.contains(p)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns true if a boat is over land
	 * 
	 * @param Boat
	 * @return is over land
	 */
	public boolean overLand(Boat b) {
		for (EPolygon x : land) {
			if (x.intersects(b.getRect())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns true if a point is in water
	 * 
	 * @param Point
	 * @return is in water
	 */
	public boolean inWater(Point p) {
		for (EPolygon x : water) {
			if (x.contains(p)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * returns a random point that is in water
	 * 
	 * @return Point
	 */
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

	public boolean snap(Boat b) {
		return (Math.sqrt(Math.pow(b.ax() - dockX, 2) + Math.pow(b.ay() - dockY, 2))) < 8;
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
