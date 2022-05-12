import java.awt.Graphics;
import java.awt.Polygon;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Map extends Picture {
	ArrayList<EntrySector> entryPoints = new ArrayList<EntrySector>();
	ArrayList<LandPoly> landPolys = new ArrayList<LandPoly>();
	ArrayList<DockPoly> dockPolys = new ArrayList<DockPoly>();

	public Map(String fileName, String sectorFileName) {
		super(0, 0, fileName, 1);

		try {
			Scanner s = new Scanner(new File(Map.class.getResource("/map-sectors/" + sectorFileName).getPath()));

			while (s.hasNextLine()) {
				String x = s.next();
				if (x.equals("/")) {
					s.nextLine();
					continue;
				}
				if (x.equals("d")) {
					dockPolys.add(new DockPoly(s.next(), s.next(), s.next(), s.next(), s.next()));
					continue;
				}
				if (x.equals("e")) {
					entryPoints.add(new EntrySector(s.next(), s.next()));
					continue;
				}
				if (x.equals("l")) {
					landPolys.add(new LandPoly(s.next()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void paint(Graphics g) {
		super.paint(g);

		for (LandPoly x : landPolys) {
			g.drawPolygon(x);
		}
		for (DockPoly x : dockPolys) {
			g.drawPolygon(x);
		}

//		for (DockSector x : dockPoints) {
//			if (x.type == 1) {
//				g.setColor(Boat.orange);
//			} else {
//				g.setColor(Boat.purple);
//			}
//			g.fillRect(x.x - Sector.width / 2, x.y - Sector.width / 2, Sector.width, Sector.width);
//			g.setColor(Color.WHITE);
//			g.fillRect(x.dockX - Sector.width / 2, x.dockY - Sector.width / 2, Sector.width, Sector.width);
//		}
//		for (EntrySector x : entryPoints) {
//			g.fillRect(x.x - Sector.width / 2, x.y - Sector.width / 2, Sector.width, Sector.width);
//		}
	}

	public static int[] cors(String s) {
		return new int[] { Integer.valueOf(s.substring(0, Sector.z.length())),
				Integer.valueOf(s.substring(Sector.z.length())) };
	}

	public EntrySector randomEntry() {
		return entryPoints.get((int) (Math.random() * (entryPoints.size())));
	}

	public boolean overLand(Boat b) {
		for (LandPoly x : landPolys) {
			if (x.intersects(b.getRect())) {
				return true;
			}
		}
		return false;
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
}

class LandPoly extends EPolygon {
	public LandPoly(String points) {
		super(points);
	}
}

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

	public boolean isOver(Boat b) {
		return this.intersects(b.getRect());
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

@SuppressWarnings("serial")
class EntrySector extends Sector {
	public EntrySector(String x, String y) {
		super(x, y);
	}

}
