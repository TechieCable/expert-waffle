import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;
import java.util.ArrayList;

public class Picture {
	protected Image img;
	protected AffineTransform tx;

	/**
	 * x,y is corner of image
	 */
	protected int x, y;
	protected double scaleSize;
	protected double width, height;

	/**
	 * default
	 */
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
	 * takes x, y, a filename (in /imgs/), and a scale size
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
	 * changes the picture
	 * 
	 * @param fileName
	 */
	protected void changePicture(String fileName) {
		img = getImage("/imgs/" + fileName);
		init(x, y);
	}

	/**
	 * paint the picture
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
	 * gets an image at the given path
	 * 
	 * @param path
	 * @return image
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

	public Rectangle toRectangle() {
		return new Rectangle(x, y, (int) width, (int) height);
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

	/**
	 * paints the picture at the given index
	 * 
	 * @param g
	 * @param index
	 */
	public void paint(Graphics g, int index) {
		img = imgs[index];
		super.paint(g);
	}
}

class PictureScroller {
	static Rectangle frame = new Rectangle(0, 0, Driver.screenW, Driver.screenH);
	ArrayList<Picture> pics = new ArrayList<Picture>();
	int startX, startY;
	double scaleSize;
	int current;

	public PictureScroller(int startX, int startY, double scaleSize) {
		this.startX = startX;
		this.startY = startY;
		this.scaleSize = scaleSize;
		current = 0;
	}

	/**
	 * add a picture to the scroller
	 * 
	 * @param fileName
	 */
	public void add(String fileName) {
		if (pics.size() > 0) {
			Picture last = pics.get(pics.size() - 1);
			pics.add(new Picture(last.x + Driver.screenW * 3 / 4, last.y, fileName, scaleSize));
		} else {
			pics.add(new Picture(startX + Driver.screenW * 3 / 4, startY, fileName, scaleSize));
		}
	}

	/**
	 * load all the pictures to avoid temporary blank screen when the pictures are
	 * first called
	 * 
	 * @param g
	 */
	public void load(Graphics g) {
		for (Picture x : pics) {
			x.paint(g);
		}
	}

	/**
	 * paint all pictures that are on the screen and move the pictures to corrent
	 * position
	 * 
	 * @param g
	 */
	public void paint(Graphics g) {
		if (pics.size() == 0)
			return;
		int move = 90;
		if (pics.get(current).x > startX) {
			move *= -1;
		} else if (pics.get(current).x < startX) {
			move *= 1;
		} else {
			move *= 0;
		}
		for (Picture x : pics) {
			x.x += move;
			if (PictureScroller.frame.intersects(new Rectangle(x.x, x.y, (int) x.width, (int) x.height))) {
				g.setColor(Driver.woodBrown);
				g.fillRect(x.x - 4 - move, x.y - 4, (int) (x.width + 8), (int) (x.height + 8));
				x.paint(g);
			}
		}
	}

	/**
	 * specify a new picture to look at
	 * 
	 * @param direction
	 */
	public void changeCurrent(int direction) {
		if (pics.get(current).x != startX)
			return;
		current += direction;
		if (current < 0) {
			current = pics.size() - 1;
		} else if (current > pics.size() - 1) {
			current = 0;
		}
	}

	public void reset() {
		current = 0;
		for (int i = 0; i < pics.size(); i++) {
			pics.get(i).x = startX + Driver.screenW * (i + 1) * 3 / 4;
		}
	}

	public Picture getCurrent() {
		if (pics.size() == 0)
			return null;
		return pics.get(current);
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

	public void ax(int x) {
		this.x = (int) (x - width / 2);
	}

	public int ay() {
		return (int) (y + height / 2);
	}

	public void ay(int y) {
		this.y = (int) (y - height / 2);
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

		move();
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(angle - Math.PI / 2, x + width / 2, y + height / 2);
		g2.drawImage(img, tx, null);
		g2.rotate(-(angle - Math.PI / 2), x + width / 2, y + height / 2);

		update();
	}
}