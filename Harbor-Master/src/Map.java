import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class Map extends Picture {
	HashMap<String, Sector> sectors = new HashMap<String, Sector>();

	public Map(String fileName, String sectorFileName) {
		super(0, 0, fileName, 1);

		try {
			Scanner s = new Scanner(new File(Map.class.getResource("/map-sectors/" + sectorFileName).getPath()));

			while (s.hasNextLine()) {
				String r = s.next(), c = s.next();
				sectors.put("00".substring(r.length()) + r + "00".substring(c.length()) + c, new Sector(r, c));
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
		}
	}

	public int[] cors(String s) {
		return new int[] { Integer.valueOf(s.substring(0, 2)), Integer.valueOf(s.substring(2)) };
	}

	public boolean overLand(int x, int y) {
		String cX = (x / Sector.width) + "", cY = (y / Sector.width) + "";
		return sectors.containsKey("00".substring(cX.length()) + cX + "" + "00".substring(cY.length()) + cY);
	}

	public boolean overLand(Boat b) {
		return overLand((int) (b.ax() - b.height), (int) (b.ay() - b.height))
				|| overLand((int) (b.ax() + b.height), (int) (b.ay() + b.height));
	}

}

@SuppressWarnings("serial")
class Sector extends Point {
	static int width = 60;

	public Sector(int x, int y) {
		super(x, y);
	}

	public Sector(String a, String b) {
		this(Integer.valueOf(a), Integer.valueOf(b));
	}

	public void paint(Graphics g) {

	}

	public boolean over(int x, int y) {
		if ((x > this.x) && (y > this.y) && (x < this.x + Sector.width) && (y < this.y + Sector.width)) {
			return true;
		}
		return false;
	}
}
