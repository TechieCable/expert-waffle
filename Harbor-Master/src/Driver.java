import java.awt.Color;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Driver extends JPanel
		implements ActionListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	public static int screenW = 1920, screenH = 1080;
	public static Color woodBrown = new Color(98, 65, 51);
	public static Color seaBlue = new Color(0, 145, 221);

	Polygon titlePoly = new Polygon(
			new int[] { 838, 838, 828, 820, 816, 840, 919, 971, 985, 1017, 1039, 1119, 1224, 1244, 1234, 1238, 1226,
					1249, 1240, 1227, 1224, 1206, 1198, 1157, 1143, 1132, 1073, 1052, 1040, 955, 934, 906, 888, 873 },
			new int[] { 751, 792, 834, 872, 901, 898, 914, 911, 915, 928, 935, 935, 933, 926, 894, 865, 842, 783, 765,
					777, 797, 803, 784, 775, 782, 781, 773, 781, 773, 765, 767, 755, 754, 743 },
			34);

	public static int maxBoats = 10;

	static Label stat;
	Game game;
	MultiPicture titles;
	Picture gameOver;
	Picture mapSelection;
	Picture bubbles;
	boolean paused;
	boolean titleHover;
	int screenNumber = 0;

	PictureScroller mapThumbs;

	boolean run = false;

	int scrollSpeed = 20;

	public void paint(Graphics g) {
		if (!run) {
			titles.paint(g, 0);
			titles.paint(g, 1);
			mapThumbs.load(g);
			run = true;
		}

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenW, screenH);

		// screenNumber
		// 0: title screen
		// 1: map selection
		// 2: game play
		// 3: game over

		if (game.gameOver) {
			screenNumber = 3;
		} else if (game.playing) {
			screenNumber = 2;
		}

		// TODO: paint map thumbnails in map selection page

		switch (screenNumber) {
		case 0:
			if (!titleHover)
				titles.paint(g, 0);
			else
				titles.paint(g, 1);
			break;
		case 1:
			// map selection
			if (mapSelection.y > 0) {
				titles.y -= scrollSpeed;
				bubbles.y -= scrollSpeed;
				mapSelection.y -= scrollSpeed;
				titles.paint(g, 0);
				bubbles.paint(g);
			}
//			g.setColor(seaBlue);
//			g.drawRect(mapSelection.x, mapSelection.y - (mapSelection.y > 0 ? scrollSpeed : 0),
//					(int) mapSelection.width, (int) mapSelection.height);
			mapSelection.paint(g);
			if (mapSelection.y == 0) {
				mapThumbs.paint(g);
			}
			break;
		case 2:
			game.paint(g);
			break;
		case 3:
			// game over
			gameOver.paint(g);
		default:
			break;
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] arg) {
		Driver d = new Driver();
	}

	public Driver() {
		JFrame frame = new JFrame("Harbor Master");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Driver.class.getResource("/imgs/boat1-0.png")));
		frame.setSize(screenW, screenH);
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(this);
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
		frame.addMouseWheelListener(this);
		stat = new Label();
//		frame.getContentPane().add(BorderLayout.NORTH, stat);
		stat.setSize(frame.getSize().width, stat.getSize().height);
		t.start();

		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);

		titles = new MultiPicture(0, 0, new String[] { "Title.png", "Title_Highlighted.png" }, 1);
		gameOver = new Picture(0, 0, "GameOver_Screen.png", 1);
		mapSelection = new Picture(0, 1080 * 2, "MapSelect.png", 1);
		bubbles = new Picture(0, 1080, "Bubbles.png", 1);

		mapThumbs = new PictureScroller(520, 360, 0.4);
		mapThumbs.add("map1.png");
		mapThumbs.add("map2.png");

		generate();

		frame.setVisible(true);

		try {
			frame.getContentPane().setCursor(Toolkit.getDefaultToolkit()
					.createCustomCursor(ImageIO.read(new File("Cursor.png")), new Point(16, 16), "blank cursor"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generate() {
		game = new Game();
		paused = false;
		titleHover = false;
		screenNumber = 0;
		mapSelection.y = 1080 * 2;
		bubbles.y = 1080;
	}

	public static void display(String s) {
		System.out.println(s);
		stat.setText(s);
	}

	public static void setStatus(String s) {
		stat.setText(s);
	}

	Timer t = new Timer(16, this);

	public void actionPerformed(ActionEvent m) {
		repaint();
	}

	public void keyPressed(KeyEvent m) {
		if (m.getKeyCode() == 82) {
			generate();
		}

		if (screenNumber == 1) {
			System.out.println(m);
			if (m.getKeyCode() == 37) { // left
				mapThumbs.changeCurrent(-1);
			} else if (m.getKeyCode() == 39) { // right
				mapThumbs.changeCurrent(1);
			}
		}
	}

	public void keyReleased(KeyEvent m) {

	}

	public void keyTyped(KeyEvent m) {

	}

	public void mouseClicked(MouseEvent m) {
		if (screenNumber == 0 && titlePoly.contains(m.getPoint())) {
			screenNumber = 1;
		} else if (screenNumber == 1 && mapThumbs.getCurrent().toRectangle().contains(m.getPoint())) {
			game.m = new Map(mapThumbs.current + 1);
			screenNumber = 2;
		}
	}

	public void mouseEntered(MouseEvent m) {
		paused = false;
	}

	public void mouseExited(MouseEvent m) {
		paused = true;
	}

	public void mousePressed(MouseEvent m) {
		game.boatPressHandler(m);
	}

	public void mouseReleased(MouseEvent m) {
		game.cursorDrag.end();
	}

	public void mouseDragged(MouseEvent m) {
		game.boatDragHandler(m);
	}

	public void mouseMoved(MouseEvent m) {
		if (screenNumber == 0 && titlePoly.contains(m.getPoint())) {
			titleHover = true;
		} else {
			titleHover = false;
		}
	}

	public void mouseWheelMoved(MouseWheelEvent m) {
		if (screenNumber == 1) {
			mapThumbs.changeCurrent(m.getWheelRotation());
		}
	}

}