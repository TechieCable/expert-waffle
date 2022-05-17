import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class Picture {
	protected Image img;
	protected AffineTransform tx;

	/**
	 * x,y is center of image
	 */
	protected int x, y;
	protected double scaleSize;
	protected double width, height;

	public Picture() {
		img = null;
		tx = null;
		x = 0;
		y = 0;
		scaleSize = 0;
		width = 0;
		height = 0;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param fileName
	 * @param scaleSize
	 */
	public Picture(int x, int y, String fileName, double scaleSize) {
		this.scaleSize = scaleSize;
		this.x = x;
		this.y = y;
		img = getImage("/imgs/" + fileName);
		tx = AffineTransform.getTranslateInstance(x, y);
		init(x, y);

		width = img.getWidth(null) * scaleSize;
		height = img.getHeight(null) * scaleSize;
	}

	/**
	 * 
	 * @param fileName
	 */
	protected void changePicture(String fileName) {
		img = getImage("/imgs/" + fileName);
		init(x, y);
	}

	/**
	 * 
	 * @param g
	 */
	public void paint(Graphics g) {
		width = img.getWidth(null) * scaleSize;
		height = img.getHeight(null) * scaleSize;

		move();
		// these are the 2 lines of code needed draw an image on the screen
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(img, tx, null);
		update();
	}

	/**
	 * 
	 */
	public void move() {
	}

	/**
	 * update the picture variable location
	 */
	protected void update() {
		tx.setToTranslation(x, y);
		tx.scale(scaleSize, scaleSize);
	}

	/**
	 * 
	 * @param a
	 * @param b
	 */
	protected void init(double a, double b) {
		tx.setToTranslation(a, b);
		tx.scale(scaleSize, scaleSize);
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	protected Image getImage(String path) {
		Image tempImage = null;
		try {
			URL imageURL = Picture.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}

	public static Image getImg(String path) {
		Image tempImage = null;
		try {
			URL imageURL = Picture.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}

	public String toString() {
		return "[x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
	}

	public int x() {
		return x;
	}

	public void x(int x) {
		this.x = x;
	}

	public int y() {
		return y;
	}

	public void y(int y) {
		this.y = y;
	}

	public double width() {
		return width;
	}

	public double height() {
		return height;
	}
}

class MultiPicture extends Picture {
	protected Image[] imgs;

	public MultiPicture(int x, int y, String[] fileNames, int scaleSize) {
		super(x, y, fileNames[0], scaleSize);
		imgs = new Image[fileNames.length];
		for (int i = 0; i < fileNames.length; i++) {
			imgs[i] = getImage("/imgs/" + fileNames[i]);
		}
	}

	public void paint(Graphics g, int index) {
		img = imgs[index];
		super.paint(g);
	}
}

class RotatingPicture extends Picture {
	protected double angle;

	public RotatingPicture() {
		super();
	}

	public RotatingPicture(int x, int y, String fileName, double scaleSize) {
		super(x, y, fileName, scaleSize);
	}

	public int ax() {
		return (int) (x + width / 2);
	}

	public int ay() {
		return (int) (y + height / 2);
	}

	public double aa() {
		return (Math.PI * 2 - angle) % (Math.PI * 2);
	}

	public void rotateTo(double angle) {
		this.angle = angle;
	}

	public void paint(Graphics g) {
		width = img.getWidth(null) * scaleSize;
		height = img.getHeight(null) * scaleSize;
		angle %= Math.PI * 2;
//		double tempAngle = Math.PI * 2 - angle;
//		tempAngle %= Math.PI * 2;

		move();
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(angle - Math.PI / 2, x + width / 2, y + height / 2);
		g2.drawImage(img, tx, null);
		g2.rotate(-(angle - Math.PI / 2), x + width / 2, y + height / 2);

//		int maxWidth = (int) Math.max(Math.abs(width * Math.sin(tempAngle)), Math.abs(height * Math.cos(tempAngle)));
//		int maxHeight = (int) Math.max(Math.abs(width * Math.cos(tempAngle)), Math.abs(height * Math.sin(tempAngle)));
//		g.drawRect(ax() - maxWidth / 2, ay() - maxHeight / 2, maxWidth, maxHeight);
		update();
	}
}