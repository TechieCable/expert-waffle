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

	protected void rotate(double angle) {
		tx.rotate(angle, width / 2, height / 2);
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