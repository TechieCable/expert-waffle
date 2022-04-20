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
				String r = s.next();
				if (r.equals("d")) {
					r = s.next();
					String c = s.next();
					dockPoints.add(new Position(Integer.valueOf(r), Integer.valueOf(c)));
					continue;
				}
				String c = s.next();
				String d = s.next();
				sectors.put(Sector.z.substring(r.length()) + r + Sector.z.substring(c.length()) + c,
						new Sector(r, c, d));
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

	public boolean overLand(int x, int y) {
		String cX = (x / Sector.width) + "", cY = (y / Sector.width) + "";
		return sectors.containsKey(Sector.z.substring(cX.length()) + cX + "" + Sector.z.substring(cY.length()) + cY);
	}

	public boolean overLand(Boat b) {
		return overLand((int) (b.ax() - b.height), (int) (b.ay() - b.height))
				|| overLand((int) (b.ax() + b.height), (int) (b.ay() + b.height));
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
