import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Map extends Picture {
	HashMap<String, Sector> sectors = new HashMap<String, Sector>();
	ArrayList<Position> dockPoints = new ArrayList<Position>();

	public Map(String fileName, String sectorFileName) {
		super(0, 0, fileName, 1);

		try {
			Scanner s = new Scanner(new File(Map.class.getResource("/map-sectors/" + sectorFileName).getPath()));

			while (s.hasNextLine()) {
				String x = s.next();
				if (x.equals("d")) {
					x = s.next();
					String y = s.next();
					dockPoints.add(new Position(Integer.valueOf(x), Integer.valueOf(y)));
					continue;
				}
				String y = s.next();
				String d = s.next();
				sectors.put(Sector.z.substring(x.length()) + x + Sector.z.substring(y.length()) + y,
						new Sector(x, y, d));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void paint(Graphics g) {
		super.paint(g);

		for (HashMap.Entry<String, Sector> s : sectors.entrySet()) {
			int[] cors = cors(s.getKey());
			g.fillRect(cors[0] * Sector.width, cors[1] * Sector.width, Sector.width, Sector.width);
			g.drawOval(cors[0] * Sector.width, cors[1] * Sector.width, Sector.width, Sector.width);
		}
	}

	public int[] cors(String s) {
		return new int[] { Integer.valueOf(s.substring(0, Sector.z.length())),
				Integer.valueOf(s.substring(Sector.z.length())) };
	}

	public Sector overLand(int x, int y) {
		String cX = (x / Sector.width) + "", cY = (y / Sector.width) + "";
		return sectors.get(Sector.z.substring(cX.length()) + cX + "" + Sector.z.substring(cY.length()) + cY);
	}

	public Sector overLand(Boat b) {
		Sector first = overLand((int) (b.ax() - b.height), (int) (b.ay() - b.height));
		if (first != null) {
			return first;
		} else {
			return overLand((int) (b.ax() + b.height), (int) (b.ay() + b.height));
		}
	}

}

@SuppressWarnings("serial")
class Sector extends Point {
	static int width = 20;
	static String z = "000";

	// 1: northeast
	// 2: northwest
	// 3: southwest
	// 4: southeast
	public int redirection;

	public Sector(int x, int y, int redirection) {
		super(x, y);
		this.redirection = redirection;
	}

	public Sector(String a, String b, String c) {
		this(Integer.valueOf(a), Integer.valueOf(b), Integer.valueOf(c));
	}

	public boolean over(int x, int y) {
		if ((x > this.x) && (y > this.y) && (x < this.x + Sector.width) && (y < this.y + Sector.width)) {
			return true;
		}
		return false;
	}
}
