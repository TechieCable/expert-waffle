import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Driver extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
	public static int screenW = 1920, screenH = 1080;

	ArrayList<Integer> Xcors = new ArrayList<Integer>();
	ArrayList<Integer> Ycors = new ArrayList<Integer>();

	public static int maxBoats = 10;

	static Label stat;
	Game game;
	MultiPicture titles;
	Picture gameOver;
	Picture mapSelection;
	boolean paused;
	boolean titleHover;
	int screenNumber = 0;

	boolean run = false;

	public void paint(Graphics g) {
		if (!run) {
			titles.paint(g, 0);
			titles.paint(g, 1);
			run = true;
		}

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenW, screenH);

		// screenNumber
		// 0: title screen
		// 1: map selection
		// 2: game play
		// 3: game over

		if (screenNumber == 1 && mapSelection.y > 0) {
			titles.y -= 20;
			mapSelection.y -= 20;
		}

		if (game.gameOver) {
			screenNumber = 3;
		} else if (game.playing) {
			screenNumber = 2;
		}

		switch (screenNumber) {
		case 0:
			if (!titleHover)
				titles.paint(g, 0);
			else
				titles.paint(g, 1);
			break;
		case 1:
			// map selection
			titles.paint(g, 0);
			mapSelection.paint(g);
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
		stat = new Label();
//		frame.getContentPane().add(BorderLayout.NORTH, stat);
		stat.setSize(frame.getSize().width, stat.getSize().height);
		t.start();

		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);

		titles = new MultiPicture(0, 0, new String[] { "Title.png", "Title_Highlighted.png" }, 1);
		gameOver = new Picture(0, 0, "GameOver_Screen.png", 1);
		mapSelection = new Picture(0, 1080, "MapSelect.png", 1);

		generate();

		frame.setVisible(true);

		try {
			frame.getContentPane().setCursor(Toolkit.getDefaultToolkit()
					.createCustomCursor(ImageIO.read(new File("Cursor.png")), new Point(0, 0), "blank cursor"));
		} catch (Exception e) {
		}
	}

	public void generate() {
		game = new Game();
		paused = false;
		titleHover = false;
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
	}

	public void keyReleased(KeyEvent m) {

	}

	public void keyTyped(KeyEvent m) {

	}

	public void mouseClicked(MouseEvent m) {
//		String x = (m.getX() + 10) / Sector.width + "";
//		String y = (m.getY() + 10) / Sector.width + "";
//		if (m.getButton() == 1) {
//			sectors.put(Sector.z.substring(x.length()) + x + Sector.z.substring(y.length()) + y, new Sector(x, y));
//		} else {
//			sectors.remove(Sector.z.substring(x.length()) + x + Sector.z.substring(y.length()) + y);
//		}
		if (m.getButton() == 1) {
			Xcors.add(m.getX());
			Ycors.add(m.getY() - 22);
		}

		if (!game.playing && m.getX() > 805 && m.getY() > 770 && m.getX() < 1275 && m.getY() < 980) {
			screenNumber = 1;
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
		if (!game.playing && m.getX() > 805 && m.getY() > 770 && m.getX() < 1275 && m.getY() < 980) {
			titleHover = true;
		} else {
			titleHover = false;
		}
		// display(m.getPoint() + "");
	}

}