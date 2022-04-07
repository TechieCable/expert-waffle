import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class Map extends Picture {
	HashMap<Integer, Sector> sectors = new HashMap<Integer, Sector>();

	public Map(String fileName, String sectorFileName) {
		super(0, 0, fileName, 1);

		try {
			Scanner s = new Scanner(new File(sectorFileName));

			while (s.hasNextLine()) {
				String r = s.next(), c = s.next();
				sectors.put(Integer.valueOf(r + c), new Sector(r, c));
			}
		} catch (Exception e) {
		}
	}

	public boolean overLand(int x, int y) {
		return sectors.containsKey(Integer.valueOf((x / 20) + "" + (y / 20)));
	}

	public boolean overLand(Boat b) {
		return overLand(b.ax(), b.ay());
	}

}

class Sector extends Point {
	int width;

	public Sector(int x, int y) {
		super(x, y);
		this.width = 20;
	}

	public Sector(String a, String b) {
		this(Integer.valueOf(a), Integer.valueOf(b));
	}

	public void paint(Graphics g) {

	}

	public boolean over(int x, int y) {
		if ((x > this.x) && (y > this.y) && (x < this.x + this.width) && (y < this.y + this.width)) {
			return true;
		}
		return false;
	}
}
