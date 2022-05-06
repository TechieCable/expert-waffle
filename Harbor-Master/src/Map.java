import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Map extends Picture {
	HashMap<String, LandSector> sectors = new HashMap<String, LandSector>();
	ArrayList<DockSector> dockPoints = new ArrayList<DockSector>();
	ArrayList<EntrySector> entryPoints = new ArrayList<EntrySector>();

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
					dockPoints.add(new DockSector(s.next(), s.next(), s.next(), s.next(), s.next(), s.next()));
					continue;
				}
				if (x.equals("e")) {
					entryPoints.add(new EntrySector(s.next(), s.next()));
					continue;
				}
				String y = s.next();
				String d = s.next();
				sectors.put(Sector.z.substring(x.length()) + x + Sector.z.substring(y.length()) + y,
						new LandSector(x, y, d));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void paint(Graphics g) {
		super.paint(g);

//		for (Entry<String, LandSector> s : sectors.entrySet()) {
//			int[] cors = cors(s.getKey());
//			s.getValue().highlight--;
//			g.setColor(s.getValue().highlight > 0 ? Color.RED : Color.BLACK);
//			g.fillRect(cors[0] * Sector.width, cors[1] * Sector.width, Sector.width, Sector.width);
//		}
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

	public LandSector overLand(int x, int y) {
		String cX = (x / Sector.width) + "", cY = (y / Sector.width) + "";
		return sectors.get(Sector.z.substring(cX.length()) + cX + "" + Sector.z.substring(cY.length()) + cY);
	}

	public LandSector overLand(Boat b) {
		LandSector test = overLand((int) (b.ax() - b.width), (int) (b.ay() - b.width));
		if (test != null) {
			test.highlight = 100;
			return test;
		}
		test = overLand((int) (b.ax() + b.height), (int) (b.ay() + b.height));
		if (test != null) {
			test.highlight = 100;
			return test;
		}

		return null;
	}

	public EntrySector randomEntry() {
		return entryPoints.get((int) (Math.random() * (entryPoints.size())));
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

//	public boolean over(int x, int y) {
//		if ((x > this.x) && (y > this.y) && (x < this.x + Sector.width) && (y < this.y + Sector.width)) {
//			return true;
//		}
//		return false;
//	}
}

@SuppressWarnings("serial")
class DockSector extends Sector {
	int dockX, dockY;
	double angle;
	/**
	 * 1 = orange, 2 = purple
	 */
	int type;

	public DockSector(String x, String y, String dockX, String dockY, String angle, String type) {
		super(x, y);
		this.dockX = Integer.valueOf(dockX);
		this.dockY = Integer.valueOf(dockY);
		this.angle = Integer.valueOf(angle) * Math.PI / 180;
		this.type = Integer.valueOf(type);
	}

	public boolean dock(Boat b) {
		if (distanceFrom(b.ax(), b.ay()) < 50) {
			return true;
		}
		return false;
	}

}

@SuppressWarnings("serial")
class LandSector extends Sector {
	// 1: northeast
	// 2: northwest
	// 3: southwest
	// 4: southeast
	public int redirection;

	public LandSector(String x, String y, String redirection) {
		super(x, y);
		this.redirection = Integer.valueOf(redirection);
	}

}

@SuppressWarnings("serial")
class EntrySector extends Sector {
	public EntrySector(String x, String y) {
		super(x, y);
	}

}
