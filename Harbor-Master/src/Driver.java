import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
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

	public static int maxBoats = 1;

	static Label stat;
	Game game;
	Picture title1;
	Picture title2;
	boolean playing;
	boolean paused;
	boolean titleHover;

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenW, screenH);

		if (playing) {
			game.paint(g);
		} else {
			if (!titleHover) {
				title1.paint(g);
			} else {
				title2.paint(g);
			}
		}

		g.setColor(Color.white);

		g.drawPolygon(Xcors.stream().mapToInt(i -> i).toArray(), Ycors.stream().mapToInt(i -> i).toArray(),
				Xcors.size());
		display(Xcors.toString() + ", " + Ycors.toString());
	}

	public static void main(String[] arg) {
		@SuppressWarnings("unused")
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
		frame.getContentPane().add(BorderLayout.NORTH, stat = new Label());
		stat.setSize(frame.getSize().width, stat.getSize().height);
		t.start();

		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);

		generate();

		frame.setVisible(true);

//		try {
//			frame.getContentPane().setCursor(Toolkit.getDefaultToolkit()
//					.createCustomCursor(ImageIO.read(new File("Cursor.png")), new Point(0, 0), "blank cursor"));
//		} catch (Exception e) {
//		}
	}

	public void generate() {
		game = new Game();
		title1 = new Picture(0, 0, "Title.png", 1);
		title2 = new Picture(0, 0, "Title_Highlighted.png", 1);
		playing = true;
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

		if (!playing && m.getX() > 805 && m.getY() > 770 && m.getX() < 1275 && m.getY() < 980) {
			playing = true;
		}
	}

	public void mouseEntered(MouseEvent m) {
		paused = false;
	}

	public void mouseExited(MouseEvent m) {
		paused = true;
	}

	public void mousePressed(MouseEvent m) {
		game.boatClickHandler(m);
	}

	public void mouseReleased(MouseEvent m) {
		game.cursorDrag.end();
	}

	public void mouseDragged(MouseEvent m) {
		game.boatDragHandler(m);
	}

	public void mouseMoved(MouseEvent m) {
		if (!playing && m.getX() > 805 && m.getY() > 770 && m.getX() < 1275 && m.getY() < 980) {
			titleHover = true;
		} else {
			titleHover = false;
		}
	}

}