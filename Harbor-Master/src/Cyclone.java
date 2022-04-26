import java.util.ArrayList;

public class Cyclone extends RotatingPicture{
	public Cyclone() {
		this((int) (Math.random() * (Driver.screenW - 20 - 20 + 1)) + 20,
				(int) (Math.random() * (Driver.screenH - 20 - 20 + 1)) + 20, "Cyclone.png");
	}

	public Cyclone(int x, int y, String filename) {
		super(x, y, filename, 1.4);
		x = 0;
		y= 1080;

	}
	//x++ 16;
	//y--9; (slope to traverse screen) 1080/1920 -27/48 = -9/16 

	public int cx() {
		return (int) (x);
	}

	public int cy() {
		return (int) (y);
	}
}
